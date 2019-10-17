package cc.hisens.hardboiled.patient.ui.activity.login.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.hisens.hardboiled.patient.db.bean.ChatMessage;
import cc.hisens.hardboiled.patient.db.bean.Conversation;
import cc.hisens.hardboiled.patient.db.bean.Doctor;
import cc.hisens.hardboiled.patient.db.bean.Ed;
import cc.hisens.hardboiled.patient.db.bean.EdInfo;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.db.impl.ChatMsgRepoImpl;
import cc.hisens.hardboiled.patient.db.impl.ConversationRepoImpl;
import cc.hisens.hardboiled.patient.db.impl.EdRepositoryImpl;
import cc.hisens.hardboiled.patient.model.ChatHistoryModel;
import cc.hisens.hardboiled.patient.model.GetEdRecordModel;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.model.FollowModel;
import cc.hisens.hardboiled.patient.ui.activity.login.present.GetVoliatCodePresenter;
import cc.hisens.hardboiled.patient.ui.activity.login.present.LoginPresenter;
import cc.hisens.hardboiled.patient.utils.ToastUtils;
import cc.hisens.hardboiled.patient.websocket.MessageModel;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.patient.data.net.model.result
 * @fileName PaientUser
 * @date on 2017/8/8 14:44
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 * 患者用户的model类
 */
@RealmClass
public class User implements RealmModel {

    /**
     * id : 50015
     * phone : 18025494242
     * user_name : init_name
     * head_url :
     * thumb_url :
     * birthday : 0
     * gender : 0
     * country :
     * state :
     * city :
     * created_time : 1558496892
     * update_time : 1558496892
     */
    @PrimaryKey
    @SerializedName("id")
    private int id;     //用户id


    //用户手机号
    @SerializedName("phone")
    private String phone;

    //用户姓名
    @SerializedName("user_name")
    private String user_name;

    //用户头像
    @SerializedName("head_url")
    private String head_url;
    //缩略图
    @SerializedName("thumb_url")
    private String thumb_url;

    //生日
    @SerializedName("birthday")
    private int birthday;
    //性别 ： 1男 2女
    @SerializedName("gender")
    private int gender;
    //国家
    @SerializedName("country")
    private String country;

    //洲
    @SerializedName("state")
    private String state;

    //城市
    @SerializedName("city")
    private String city;

    //用户创建时间
    @SerializedName("created_time")
    private int created_time;

    //用户更新时间
    @SerializedName("update_time")
    private int update_time;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCreated_time() {
        return created_time;
    }

    public void setCreated_time(int created_time) {
        this.created_time = created_time;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", user_name='" + user_name + '\'' +
                ", head_url='" + head_url + '\'' +
                ", thumb_url='" + thumb_url + '\'' +
                ", birthday=" + birthday +
                ", gender=" + gender +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", created_time=" + created_time +
                ", update_time=" + update_time +
                '}';
    }

    //登录的网络请求
    public void login(final Context context, String number, String VerificationCode, final LoginPresenter listener) {

        HashMap<String, Object> params = new HashMap<>();

        params.put("phone", number);
        params.put("code", VerificationCode);

        RequestUtils.post(context, Url.paientLogin, params, new HashMap<>(), new MyObserver<BaseResponse>(context) {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result != null) {

                    if (result.result == 0) {
                        Gson gson = new Gson();
                        User paientUser = new Gson().fromJson(gson.toJson(result.getDatas()), User.class);
                        listener.loginSuccess(paientUser);
                    } else {

                        listener.loginFailed(result.message);
                    }
                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                errorMsg="无法连接网络，请检查网络是否打开！";
                listener.loginFailed(errorMsg);
            }
        });
    }

    //发送验证码的网络请求
    public void getVerificationCode(final Context context, String number, final GetVoliatCodePresenter listener) {
        HashMap<String, Object> params = new HashMap<>();

        params.put("phone", number);
        RequestUtils.post(context, Url.getVerificationCode,params, new HashMap<>(), new MyObserver<BaseResponse>(context) {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result != null) {
                    if (result.result == 0) {
                        listener.getSuccess("发送成功");
                    } else {
                         if (result.result==1003){
                             result.message="获取验证码超过次数!";
                         }
                        listener.getFailed(result.message);
                    }
                }
            }
            @Override
            public void onFailure(Throwable e, String errorMsg) {
                Log.e("11111",errorMsg);
                String msg="无法连接网络，请检查网络是否打开！";
                listener.getFailed(msg);
            }
        });

    }



    //发送验证码的网络请求
    public void getVerificationCode2(final Context context, String number, final LoginPresenter listener) {
        HashMap<String, Object> params = new HashMap<>();

        params.put("phone", number);
        RequestUtils.post(context, Url.getVerificationCode,params , new HashMap<>(), new MyObserver<BaseResponse>(context) {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result != null) {
                    if (result.result == 0) {
                        ToastUtils.show(context, "发送成功");
                    } else {
                        if (result.result==1003){
                            result.message="获取验证码超过次数!";
                        }
                        listener.loginFailed(result.message);
                    }
                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                String msg="无法连接网络，请检查网络是否打开！";
                listener.loginFailed(msg);
            }
        });

    }



    //获取当前问诊的列表
    public void getCurrentConsultionList(Context context,final LoginPresenter listener){
        Map<String,Object>params=new HashMap<>();
        params.put("status",1);
        RequestUtils.get(context, Url.getConsultionHistory, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result.result==0){
                    Gson gson=new Gson();
                    FollowModel model=gson.fromJson(gson.toJson(result),FollowModel.class);
                    if (model.getDatas()!=null&&model.getDatas().size()>0) {  //判断是否由聊天记录，没有聊天记录就跳过
                        getChatHistory(context,model.getDatas(),listener);

                    }else{
                        getEdRecord(context,listener);
                    }

                }

            }
            @Override
            public void onFailure(Throwable e, String errorMsg) {

                listener.getFollwedFair(errorMsg);
            }
        });
    }



    //获取用户的Ed记录
    public void getEdRecord(Context context, final LoginPresenter listener){
        Map<String,Object>params=new HashMap<>();
        params.put("user_id", UserConfig.UserInfo.getUid());
        RequestUtils.get(context, Url.getEdRecord, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result.result==0){
                    Gson gson=new Gson();
                   GetEdRecordModel bean=gson.fromJson(gson.toJson(result),GetEdRecordModel.class);
                    EdRepositoryImpl edRepository=new EdRepositoryImpl();
                    for (int i=0;i<bean.getDatas().size();i++){
                        GetEdRecordModel.DatasBean datasBean=bean.getDatas().get(i);

                        Ed ed=new Ed();
                        ed.setId(datasBean.getId());
                        ed.setStartTimestamp(datasBean.getStart_time()*1000);
                        ed.setEndTimestamp(datasBean.getEnd_time()*1000);


                        ed.setSync(true);
                        ed.setInterferenceFactor(datasBean.getFactors());
                        if (datasBean.getType()==1){
                            ed.setInterferential(true);
                        }else {
                            ed.setInterferential(false);
                        }

                        ed.setAverage(datasBean.getAve_strength());

                       edRepository.saveEd(ed);
                    }

                }

                listener.getFollowedSuccess(true);

            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                listener.getFollwedFair(errorMsg);
            }
        });

    }




    //获取聊天记录
    public void getChatHistory(Context context, List<Doctor> doctorId, final LoginPresenter listener){

        ConversationRepoImpl conversationRepo=new ConversationRepoImpl();
        ChatMsgRepoImpl chatMsgRepo=new ChatMsgRepoImpl();
        if (doctorId.size()!=0&&doctorId!=null) {
            for (int i = 0; i < doctorId.size(); i++) {

                int finalI1 = i;
//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
                        Map<String,Object>params=new HashMap<>();
                        params.put("to_id", doctorId.get(finalI1).getDoctor_id());
                        params.put("page_count",20);

                        int finalI = finalI1;

                        RequestUtils.get(context, Url.chatHistory, params, new MyObserver<BaseResponse>() {
                            @Override
                            public void onSuccess(BaseResponse result) {

                                Gson gson = new Gson();
                                ChatHistoryModel model = gson.fromJson(gson.toJson(result), ChatHistoryModel.class);
                                ChatHistoryModel.DatasBean bean = model.getDatas();


                                      if (bean.getList().size() > 0)
                                          SaveConversions(bean, conversationRepo, chatMsgRepo, doctorId.get(finalI));

                                  
                                if (finalI ==doctorId.size()-1){
                                   getEdRecord(context,listener);
                                }

                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg) {
                                listener.getFollwedFair(errorMsg);

                            }
                        });

//                    }
//                }.start();


            }
        }

    }



    //存储发送图片消息
    public void SaveConversions(ChatHistoryModel.DatasBean bean, ConversationRepoImpl conversationRepo, ChatMsgRepoImpl chatMsgRepo,Doctor doctor) {

        MessageModel model = bean.getList().get(0);

        //会话消息
        Conversation conversation = new Conversation();
        conversation.setFriendName(doctor.getName());
        if (!(model.getFrom()+"").equals(UserConfig.UserInfo.getUid())) {
            conversation.setFriendId(model.getFrom() + "");
        }else{
            conversation.setFriendId(model.getTo() + "");
        }
        conversation.setRead(true);  //是否阅读
        conversation.setImageUrl(doctor.getHead_url());  //头像
        conversation.setLastMessageTime(model.getCreate_time());
        conversation.setLevel(doctor.getLevel()); //医生等级
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageType(model.getType());//消息类型
        chatMessage.setMsgIndex(model.getMsg_index());

        chatMessage.setRead(true);
        if (!(model.getFrom()+"").equals(UserConfig.UserInfo.getUid())){
            chatMessage.setSend(false);
            chatMessage.setSenderId(String.valueOf(model.getTo()));
            chatMessage.setReceiverId(String.valueOf(model.getFrom()));
        }else{
            chatMessage.setSenderId(String.valueOf(model.getFrom()));
            chatMessage.setReceiverId(String.valueOf(model.getTo()));
        }

        RealmList<String> imagelist = new RealmList<>();
        RealmList<String> thumbList = new RealmList<>();
        imagelist.clear();
        thumbList.clear();

        if (model.getType() == 0) {
            if (model.getContent().size()>0) {
                chatMessage.setTextMessage(model.getContent().get(0).getText());
            }
        } else if (model.getType() == 2) {
            if (model.getContent().size()>0) {
                chatMessage.setVoicePath(model.getContent().get(0).getUrl());
                chatMessage.setVoiceTime(model.getContent().get(0).getDuration());
            }
        } else if (model.getType() == 1) {

        for (int i = 0; i <model.getContent().size(); i++) {
            imagelist.add(model.getContent().get(i).getUrl());
            thumbList.add(model.getContent().get(i).getThumb_url());
        }
            chatMessage.setImagePath(imagelist);
            chatMessage.setThumbImagePath(thumbList);
            chatMessage.setImageWidth(model.getContent().get(0).getWidth());
            chatMessage.setImageHeight(model.getContent().get(0).getHeight());
        }else if (model.getType()==3){
            chatMessage.setTextMessage(model.getContent().get(0).getText());
            for (int i = 0; i <model.getContent().size(); i++) {
                if (!TextUtils.isEmpty(model.getContent().get(i).getUrl())){
                    imagelist.add(model.getContent().get(i).getUrl());
                    thumbList.add(model.getContent().get(i).getThumb_url());

                }

                if(TextUtils.isEmpty(model.getContent().get(i).getUrl())&&!TextUtils.isEmpty(model.getContent().get(i).getText())){
                    chatMessage.setTextMessage(model.getContent().get(i).getText());
                }
            }
            chatMessage.setImagePath(imagelist);
            chatMessage.setThumbImagePath(thumbList);

        }

        chatMessage.setTimestamp(model.getCreate_time());
        SaveMessage(bean,chatMsgRepo);

        conversation.setLastMessage(chatMessage);
        conversationRepo.saveConversation(conversation);

    }


    public void SaveMessage(ChatHistoryModel.DatasBean bean, ChatMsgRepoImpl chatMsgRepo){

        //聊天消息
        for (int i=1;i<bean.getList().size();i++) {
            MessageModel model=bean.getList().get(i);
            ChatMessage chatMessage2 = new ChatMessage();
            chatMessage2.setMsgIndex(model.getMsg_index());
            chatMessage2.setMessageType(model.getType());//消息类型
            chatMessage2.setRead(true);
            if (!(model.getFrom()+"").equals(UserConfig.UserInfo.getUid())){
                chatMessage2.setSend(false);
                chatMessage2.setSenderId(String.valueOf(model.getTo()));
                chatMessage2.setReceiverId(String.valueOf(model.getFrom()));
            }else{
                chatMessage2.setSenderId(String.valueOf(model.getFrom()));
                chatMessage2.setReceiverId(String.valueOf(model.getTo()));
            }



            RealmList<String> imagelist = new RealmList<>();
            RealmList<String> thumbList = new RealmList<>();
            imagelist.clear();
            thumbList.clear();

            if (model.getType() == 0) {
                if (model.getContent().size()>0)
                chatMessage2.setTextMessage(model.getContent().get(0).getText());
            } else if (model.getType() ==2 ) {
                if (model.getContent().size()>0) {
                    chatMessage2.setVoicePath(model.getContent().get(0).getUrl());
                    chatMessage2.setVoiceTime(model.getContent().get(0).getDuration());
                }
            } else if (model.getType() == 1) {

                for (int a = 0; a <model.getContent().size(); a++) {
                    imagelist.add(model.getContent().get(a).getUrl());
                    thumbList.add(model.getContent().get(a).getThumb_url());
                }
                chatMessage2.setImagePath(imagelist);
                chatMessage2.setThumbImagePath(thumbList);
                if (model.getContent().size()>0) {
                    chatMessage2.setImageWidth(model.getContent().get(0).getWidth());
                    chatMessage2.setImageHeight(model.getContent().get(0).getHeight());
                }
            }else if (model.getType()==3){
                for (int a = 0; a <model.getContent().size(); a++) {
                    if(!TextUtils.isEmpty(model.getContent().get(a).getUrl())){
                        imagelist.add(model.getContent().get(a).getUrl());
                        thumbList.add(model.getContent().get(a).getThumb_url());

                    }
                    if(TextUtils.isEmpty(model.getContent().get(a).getUrl())&&!TextUtils.isEmpty(model.getContent().get(a).getText())){
                        chatMessage2.setTextMessage(model.getContent().get(a).getText());
                    }


                }
                chatMessage2.setImagePath(imagelist);
                chatMessage2.setThumbImagePath(thumbList);


            }
            chatMessage2.setTimestamp(model.getCreate_time());
            chatMsgRepo.saveChatMsg(chatMessage2);
        }
    }




}
