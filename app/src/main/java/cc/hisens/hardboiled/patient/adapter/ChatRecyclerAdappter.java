package cc.hisens.hardboiled.patient.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import cc.hisens.hardboiled.patient.MyApplication;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.db.ChatMsgRepo;
import cc.hisens.hardboiled.patient.db.bean.ChatMessage;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.db.impl.ChatMsgRepoImpl;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.DoctorDetailActivity;
import cc.hisens.hardboiled.patient.ui.activity.healthrecord.PersonHealthRecordActivity;
import cc.hisens.hardboiled.patient.ui.activity.preview_photo.PreviewPictureActivity;
import cc.hisens.hardboiled.patient.utils.MediaManager;
import cc.hisens.hardboiled.patient.utils.ScreenUtil;
import cc.hisens.hardboiled.patient.utils.ScreenUtils;
import cc.hisens.hardboiled.patient.utils.TimeUtils;
import cc.hisens.hardboiled.patient.wideview.CircleImageView;

public class ChatRecyclerAdappter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public Context mContext;
    public List<ChatMessage> list;
    public String headUrl; //用户头像
    public ChatMsgRepoImpl chatMsgRepo;
    public String doctorid; //医生id;
    public String username,age;
    public MyApplication application;




    public ChatRecyclerAdappter(Context mContext,String headurl,String doctorid,String username,String age,MyApplication application) {
        this.mContext = mContext;
        chatMsgRepo=new ChatMsgRepoImpl();
        this.headUrl=headurl;
        this.doctorid=doctorid;
        this.username=username;
        this.age=age;
        this.application=application;
    }

     public void addData(List<ChatMessage>list){
        this.list=list;
     }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = list.get(position);
        if (chatMessage.isSend()) {   //先判定是否是发送的消息还是接收的消息

            if (chatMessage.getMessageType() == 0) {  //发送文本


                return 0;
            } else if (chatMessage.getMessageType() == 1) {  //发送图片

                return 1;
            } else if (chatMessage.getMessageType() == 2) {  //发送语言

                return 2;
            } else {   //顶部head默认展示的用户信息

                return 3;
            }
        } else {

            if (chatMessage.getMessageType() == 0) {  //接收文本


                return 4;
            } else if (chatMessage.getMessageType() == 1) {  //接收图片

                return 5;
            } else if (chatMessage.getMessageType() == 2) {  //接收语言

                return 6;
            }


        }

        return 7;   //8这种类型是ehs和IIEF评分的
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //viewtype就是 getItemViewType(int position)的返回值
        switch (viewType){
            case 0:  //发送文本消息

                View view= LayoutInflater.from(mContext).inflate(R.layout.chat_text_message_send,parent,false);
                return new SendTextViewHolder(view);

            case 1: //发送图片消息
                View view1 =LayoutInflater.from(mContext).inflate(R.layout.chat_image_message_send,parent,false);

                return new SendImageViewHolder(view1);

            case 2:  //发送语音消息
                View view2 =LayoutInflater.from(mContext).inflate(R.layout.chat_voice_message_send,parent,false);

                return new SendVoiceViewHolder(view2);

            case 3:  //默认首条的消息
                View view3 =LayoutInflater.from(mContext).inflate(R.layout.chat_text_message_head,parent,false);

                return new TextAndImageViewHolder(view3);

            case 4:  //接收文本消息
                View view4 =LayoutInflater.from(mContext).inflate(R.layout.chat_text_message_received,parent,false);

                return new ReceivedTextViewHolder(view4);
            case 5:  //接收图片消息
                View view5 =LayoutInflater.from(mContext).inflate(R.layout.chat_image_message_received,parent,false);

                return new ReceivedImageViewHolder(view5);
            case 6:  //接收语音消息
                View view6 =LayoutInflater.from(mContext).inflate(R.layout.chat_voice_message_received,parent,false);

                return new ReceivedVoiceViewHolder(view6);


        }



        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int type = getItemViewType(position);

        switch (type){
            case 0:  //发送文本消息
                SendTextViewHolder sendTextViewHolder= (SendTextViewHolder) holder;

                ChatMessage message = list.get(position);
                if (position!=0&& TimeUtils.MoreThan5Millis(list.get(position - 1).getTimestamp(), message.getTimestamp())) {
                    sendTextViewHolder.tvTime.setVisibility(View.VISIBLE);
                    sendTextViewHolder.tvTime.setText(TimeUtils.format(message.getTimestamp()));
                } else {
                    sendTextViewHolder.tvTime.setVisibility(View.GONE);
                }


                sendTextViewHolder.tvMessage.setText(message.getTextMessage());

                break;
            case 1:   //发送图片消息
                SendImageViewHolder sendImageViewHolder= (SendImageViewHolder) holder;
                ChatMessage message2 = list.get(position);

                //是否显示时间,后一天消息比前一条消息超过5分钟就显示
                if (position!=0&&TimeUtils.MoreThan5Millis(list.get(position - 1).getTimestamp(), message2.getTimestamp())) {
                    sendImageViewHolder.tvTime.setVisibility(View.VISIBLE);
                    sendImageViewHolder.tvTime.setText(TimeUtils.format(message2.getTimestamp()));
                } else {
                    sendImageViewHolder.tvTime.setVisibility(View.GONE);
                }


                ViewGroup.LayoutParams lp =    sendImageViewHolder.ivSend.getLayoutParams();
                lp.width = (int) ScreenUtils.dp2px(mContext,150);
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                sendImageViewHolder.ivSend.setLayoutParams(lp);

                sendImageViewHolder.ivSend.setMaxWidth((int) ScreenUtils.dp2px(mContext,150));
                sendImageViewHolder.ivSend.setMaxHeight((int) ScreenUtils.dp2px(mContext,200));

                if (message2.getThumbImagePath() != null && message2.getThumbImagePath().size() > 0) {
                    Glide.with(mContext).load(message2.getThumbImagePath().get(0)).into(sendImageViewHolder.ivSend);
                    sendImageViewHolder.tvImageCount.setText(message2.getImagePath().size() + "");
                    sendImageViewHolder.ivSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, PreviewPictureActivity.class);
                            intent.putExtra("index", 0 + "");
                            ArrayList<String> list = new ArrayList<>();
                            list.addAll(message2.getImagePath());
                            intent.putStringArrayListExtra("path", list);
                            mContext.startActivity(intent);
                        }
                    });
                }
                break;

            case 2:  //发送语音消息
                SendVoiceViewHolder sendVoiceViewHolder= (SendVoiceViewHolder) holder;

                ChatMessage message3 = list.get(position);
                //是否显示时间
                if (position!=0&&TimeUtils.MoreThan5Millis(list.get(position - 1).getTimestamp(), message3.getTimestamp())) {
                    sendVoiceViewHolder.tvTime.setVisibility(View.VISIBLE);
                    sendVoiceViewHolder.tvTime.setText(TimeUtils.format(message3.getTimestamp()));
                } else {
                    sendVoiceViewHolder.tvTime.setVisibility(View.GONE);
                }

                if (application.indexPlay!=position){
                    sendVoiceViewHolder.iv_voice.setBackgroundResource(R.drawable.talk_voice_right01);
                }else{
                    sendVoiceViewHolder.iv_voice.setBackgroundResource(R.drawable.audio_animation_right_list);
                    AnimationDrawable drawable = (AnimationDrawable) sendVoiceViewHolder.iv_voice.getBackground();
                    drawable.start();
                }

                sendVoiceViewHolder.tvVoiceTime.setText(message3.getVoiceTime() + "s");
                SendVoiceViewHolder finalSendVoiceViewHolder = sendVoiceViewHolder;
                sendVoiceViewHolder.rlVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        application.indexPlay=position;
                        MediaManager.reset();

                        MediaManager.playSound(mContext, message3.getVoicePath(), new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {

                                finalSendVoiceViewHolder.iv_voice.setBackgroundResource(R.drawable.talk_voice_right03);
                                MediaManager.release();

                            }
                        });
                        notifyDataSetChanged();
                    }
                });


                break;


            case 3:   //默认首条支付跳转过来的界面
                TextAndImageViewHolder textAndImageViewHolder= (TextAndImageViewHolder) holder;
                ChatMessage message4 = list.get(position);
                //是否显示时间

                textAndImageViewHolder.tvTime.setVisibility(View.VISIBLE);
                textAndImageViewHolder.tvTime.setText(TimeUtils.format(message4.getTimestamp()));
                textAndImageViewHolder.tvMessage.setText(message4.getTextMessage());
                textAndImageViewHolder.tvUserName.setText(username + "       " + age+"岁");
                textAndImageViewHolder.rlToMyFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, PersonHealthRecordActivity.class));
                    }
                });

                if (message4.getImagePath() != null && message4.getImagePath().size() > 0) {
                    textAndImageViewHolder.rl_image.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(message4.getThumbImagePath().get(0)).into(textAndImageViewHolder.iv);
                    textAndImageViewHolder.imageCount.setText(message4.getImagePath().size() + "");
                    textAndImageViewHolder.iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, PreviewPictureActivity.class);
                            intent.putExtra("index", 0 + "");
                            ArrayList<String> list = new ArrayList<>();
                            list.addAll(message4.getImagePath());
                            intent.putStringArrayListExtra("path", list);
                            mContext.startActivity(intent);
                        }
                    });
                }else{
                    textAndImageViewHolder.rl_image.setVisibility(View.GONE);
                }

                break;

            case 4://接受的文本消息
                ReceivedTextViewHolder receivedTextViewHolder= (ReceivedTextViewHolder) holder;
                ChatMessage message5 = list.get(position);
                if (position!=0&&TimeUtils.MoreThan5Millis(list.get(position - 1).getTimestamp(), message5.getTimestamp())) {
                    receivedTextViewHolder.tvTime.setVisibility(View.VISIBLE);
                    receivedTextViewHolder.tvTime.setText(TimeUtils.format(message5.getTimestamp()));
                } else {
                    receivedTextViewHolder.tvTime.setVisibility(View.GONE);
                }

                receivedTextViewHolder.tvMessage.setText(message5.getTextMessage());
                Glide.with(mContext).load(headUrl).placeholder(R.drawable.doctor_head_100).into(receivedTextViewHolder.ivHead);
                receivedTextViewHolder.ivHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext, DoctorDetailActivity.class);
                        intent.putExtra("id",Integer.parseInt(message5.getReceiverId()));
                        mContext.startActivity(intent);
                    }
                });

                break;

            case 5:  //接收的图片消息
                ReceivedImageViewHolder receivedImageViewHolder= (ReceivedImageViewHolder) holder;
                ChatMessage message6 = list.get(position);

                //是否显示时间,后一天消息比前一条消息超过5分钟就显示
                if (position!=0&&TimeUtils.MoreThan5Millis(list.get(position - 1).getTimestamp(), message6.getTimestamp())) {
                    receivedImageViewHolder.tvTime.setVisibility(View.VISIBLE);
                    receivedImageViewHolder.tvTime.setText(TimeUtils.format(message6.getTimestamp()));
                } else {
                    receivedImageViewHolder.tvTime.setVisibility(View.GONE);
                }
                Glide.with(mContext).load(headUrl).placeholder(R.drawable.doctor_head_100).into(receivedImageViewHolder.ivHead);
                if (message6.getThumbImagePath() != null && message6.getThumbImagePath().size() > 0) {
//                    int width=0;
//                    int height=0;
//                    if (message6.getImageWidth()>=1500) {
//                        width = message6.getImageWidth() / 5;
//                        height = message6.getImageHeight() / 5;
//
//                    } else if (message6.getImageWidth()>=900) {
//                         width = message6.getImageWidth() / 2;
//                         height = message6.getImageHeight() / 2;
//                    }else{
//                        width = message6.getImageWidth() ;
//                        height = message6.getImageHeight() ;
//                    }
//
//                    Log.e("tupian",message6.getImageWidth()+"........"+message6.getImageHeight());
//                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( width,height);
//                    //设置左右边距
//                    params.leftMargin =  ScreenUtils.px2dip(mContext, 0);
//                    params.rightMargin =  ScreenUtils.px2dip(mContext, 0);
//
//                    receivedImageViewHolder.ivSend.setLayoutParams(params);


                    ViewGroup.LayoutParams lp2 =  receivedImageViewHolder.ivSend.getLayoutParams();
                    lp2.width = (int) ScreenUtils.dp2px(mContext,150);
                    lp2.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    receivedImageViewHolder.ivSend.setLayoutParams(lp2);

                    receivedImageViewHolder.ivSend.setMaxWidth((int) ScreenUtils.dp2px(mContext,150));
                    receivedImageViewHolder.ivSend.setMaxHeight((int) ScreenUtils.dp2px(mContext,200));




                    Glide.with(mContext).load(message6.getThumbImagePath().get(0)).into(receivedImageViewHolder.ivSend);
                    receivedImageViewHolder.tvImageCount.setText(message6.getThumbImagePath().size() + "");
                    receivedImageViewHolder.ivSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, PreviewPictureActivity.class);
                            intent.putExtra("index", 0 + "");
                            ArrayList<String> list = new ArrayList<>();
                            list.addAll(message6.getImagePath());
                            intent.putStringArrayListExtra("path", list);
                            mContext.startActivity(intent);
                        }
                    });
                }
                receivedImageViewHolder.ivHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext, DoctorDetailActivity.class);
                        intent.putExtra("id",Integer.parseInt(message6.getReceiverId()));
                        mContext.startActivity(intent);
                    }
                });


                break;
            case 6:    //接受的语音消息
                ReceivedVoiceViewHolder receivedVoiceViewHolder= (ReceivedVoiceViewHolder) holder;
                ChatMessage message7 = list.get(position);
                //是否显示时间
                if (position!=0&&TimeUtils.MoreThan5Millis(list.get(position - 1).getTimestamp(), message7.getTimestamp())) {
                    receivedVoiceViewHolder.tvTime.setVisibility(View.VISIBLE);
                    receivedVoiceViewHolder.tvTime.setText(TimeUtils.format(message7.getTimestamp()));
                } else {
                    receivedVoiceViewHolder.tvTime.setVisibility(View.GONE);
                }
                if (message7.isClickVoice()) {
                    receivedVoiceViewHolder.ivRed.setVisibility(View.GONE);
                } else {
                    receivedVoiceViewHolder.ivRed.setVisibility(View.VISIBLE);
                }

                if (application.indexPlay!=position){
                    receivedVoiceViewHolder.iv_voice.setBackgroundResource(R.drawable.talk_voice_left01);
                }else{
                    receivedVoiceViewHolder.iv_voice.setBackgroundResource(R.drawable.audio_animation_left_list);
                    AnimationDrawable drawable = (AnimationDrawable) receivedVoiceViewHolder.iv_voice.getBackground();
                    receivedVoiceViewHolder.ivRed.setVisibility(View.GONE);
                    drawable.start();
                }
                Glide.with(mContext).load(headUrl).placeholder(R.drawable.doctor_head_100).into(receivedVoiceViewHolder.ivHead);
                receivedVoiceViewHolder.tvVoiceTime.setText(message7.getVoiceTime() + "s");
                ReceivedVoiceViewHolder finalReceivedVoiceViewHolder = receivedVoiceViewHolder;
                receivedVoiceViewHolder.lyVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        application.indexPlay=position;
                        chatMsgRepo.setDoctorUnreadVoiceMessageState(doctorid,true,position);
                        MediaManager.reset();
                        for (int i=0;i<list.size();i++){
                            if (i==position) {
                                list.get(i).setClickVoice(true);
                            }
                        }

                        MediaManager.playSound(mContext, message7.getVoicePath(), new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {

                                finalReceivedVoiceViewHolder.iv_voice.setBackgroundResource(R.drawable.talk_voice_left03);

                                MediaManager.release();

                            }
                        });
                        notifyDataSetChanged();
                    }
                });

                receivedVoiceViewHolder.ivHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext, DoctorDetailActivity.class);
                        intent.putExtra("id",Integer.parseInt(message7.getReceiverId()));
                        mContext.startActivity(intent);
                    }
                });

                break;

        }



    }



    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }



    //发送文本
    class SendTextViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvMessage, tvTime;

        public SendTextViewHolder(View view) {
            super(view);
            tvMessage = view.findViewById(R.id.chat_item_content_send);
            tvTime = view.findViewById(R.id.tv_time_send);
        }
    }

    //发送图片
    class SendImageViewHolder extends RecyclerView.ViewHolder{
        public TextView tvImageCount, tvTime;
        public ImageView ivSend;

        public SendImageViewHolder(View view) {
            super(view);
            tvImageCount = view.findViewById(R.id.chat_item_image_count_send);
            tvTime = view.findViewById(R.id.tv_time_send);
            ivSend = view.findViewById(R.id.chat_item_image_send);

        }

    }

    //发送语音
    class SendVoiceViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv_voice;//红点和语音图标
        public TextView tvTime, tvVoiceTime; //发送语音消息时间，语音时长
        public RelativeLayout rlVoice;

        public SendVoiceViewHolder(View view) {
            super(view);

            iv_voice = view.findViewById(R.id.iv_voice);
            tvTime = view.findViewById(R.id.tv_time_send);
            tvVoiceTime = view.findViewById(R.id.chat_item_vocie_time_send);
            rlVoice = view.findViewById(R.id.rl_voice);
        }
    }

    //接收文本消息
    class ReceivedTextViewHolder extends RecyclerView.ViewHolder{
        public TextView tvMessage, tvTime; //接收的文本消息和接收消息时间
        public CircleImageView ivHead; //头像

        public ReceivedTextViewHolder(View view) {
            super(view);
            tvMessage = view.findViewById(R.id.chat_item_content_received);
            tvTime = view.findViewById(R.id.tv_time_received);
            ivHead = view.findViewById(R.id.iv_doctor_head);
        }
    }


    //接收图片
    class ReceivedImageViewHolder extends RecyclerView.ViewHolder{
        public TextView tvImageCount, tvTime;
        public ImageView ivSend;
        public CircleImageView ivHead; //头像

        public ReceivedImageViewHolder(View view) {
            super(view);
            tvImageCount = view.findViewById(R.id.chat_item_image_count_received);
            tvTime = view.findViewById(R.id.tv_time_received);
            ivSend = view.findViewById(R.id.chat_item_image_received);
            ivHead=view.findViewById(R.id.iv_doctor_head);

        }

    }


    //接收语音
    class ReceivedVoiceViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivRed, iv_voice;//红点和语音图标
        public TextView tvTime, tvVoiceTime; //发送语音消息时间，语音时长
        public LinearLayout lyVoice;
        public CircleImageView ivHead; //头像

        public ReceivedVoiceViewHolder(View view) {
            super(view);
            ivRed = view.findViewById(R.id.iv_voice_red_received);
            iv_voice = view.findViewById(R.id.iv_voice_received);
            tvTime = view.findViewById(R.id.tv_time_received);
            tvVoiceTime = view.findViewById(R.id.chat_item_vocie_time_received);
            lyVoice=view.findViewById(R.id.ly_received_voice);
            ivHead=view.findViewById(R.id.iv_doctor_head);
        }
    }


    //默认首条图文问诊消息
    class TextAndImageViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rlToMyFile,rl_image;  //跳转到我的档案
        public TextView tvUserName;   //用户姓名
        public ImageView iv;
        public TextView imageCount; //图片张数
        public TextView tvMessage, tvTime; //消息,时间

        public TextAndImageViewHolder(View view) {
            super(view);
            rlToMyFile = view.findViewById(R.id.rl_chat_item_send);
            tvUserName = view.findViewById(R.id.head_chat_item_content_name_send);
            tvMessage = view.findViewById(R.id.head_chat_item_content_desc_send);
            imageCount = view.findViewById(R.id.head_chat_item_image_count_send);
            iv = view.findViewById(R.id.head_chat_item_image_send);
            tvTime = view.findViewById(R.id.tv_time_send);
            rl_image=view.findViewById(R.id.rl_image);  //图片，如果有的话就显示，没有就隐藏

        }


    }



}
