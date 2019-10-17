package cc.hisens.hardboiled.patient.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.MyBaseAdapter;
import cc.hisens.hardboiled.patient.db.bean.ChatMessage;
import cc.hisens.hardboiled.patient.db.bean.Conversation;
import cc.hisens.hardboiled.patient.db.impl.ChatMsgRepoImpl;
import cc.hisens.hardboiled.patient.utils.TimeUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ConversationAdapter extends BaseAdapter {
    public List<Conversation>list;
    public Context mContext;
    public ChatMsgRepoImpl chatMsgRepo;



    public ConversationAdapter(Context context, List<Conversation> list) {
        this.list=list;
        this.mContext=context;
        chatMsgRepo=new ChatMsgRepoImpl();
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("CheckResult")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ConversationViewHolder vh=null;

         if (convertView==null){
             convertView=View.inflate(mContext,R.layout.conversation_item,null);
             vh=new ConversationViewHolder(convertView);
             convertView.setTag(vh);
         }else{
             vh= (ConversationViewHolder) convertView.getTag();
         }
         //todo  这里需要赋值，目前咱们没有内容，稍后再弄
        Conversation conversation=list.get(position);

        Glide.with(mContext).load(conversation.getImageUrl()).placeholder(R.drawable.doctor_head_100).into(vh.ivAvator);

        vh.tvName.setText(conversation.getFriendName());
        if (conversation.getLastMessage().getMessageType()==0) {
            vh.tvConversation.setText(conversation.getLastMessage().getTextMessage());
        }else if (conversation.getLastMessage().getMessageType()==1){
            vh.tvConversation.setText("【图片】");
        }else if (conversation.getLastMessage().getMessageType()==2){
            vh.tvConversation.setText("【语音】");
        }else {
            vh.tvConversation.setText(conversation.getLastMessage().getTextMessage());
        }
        vh.tvTime.setText(TimeUtils.format(conversation.getLastMessage().getTimestamp()));


        ConversationViewHolder finalVh = vh;
        chatMsgRepo.getChatMessageList(conversation.getFriendId()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<ChatMessage>>() {
                @Override
                public void accept(List<ChatMessage> chatMessages) throws Exception {
                    if (chatMessages != null) {
                       int unread=0;
                        for (int a=0;a<chatMessages.size();a++){
                            if (chatMessages.get(a).isRead()==false){
                                unread++;
                            }
                        }
                        if (unread!=0){
                            finalVh.tvRed.setText(unread +"");
                            finalVh.tvRed.setVisibility(View.VISIBLE);
                        }else{
                            finalVh.tvRed.setVisibility(View.GONE);
                        }

                    }else{
                        finalVh.tvRed.setVisibility(View.GONE);
                    }


                }
            });







        return convertView;
    }


     class ConversationViewHolder{
        public ImageView ivAvator; //头像
         public TextView tvName,tvTime,tvConversation,tvRed;//名字，时间，会话内容
         public ConversationViewHolder(View view){
             ivAvator=view.findViewById(R.id.iv_avator);
             tvName=view.findViewById(R.id.tv_username);
             tvTime=view.findViewById(R.id.tv_time);
             tvConversation=view.findViewById(R.id.tv_desc);
             tvRed=view.findViewById(R.id.tv_doctor_message_count);
         }


     }

}
