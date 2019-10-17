package cc.hisens.hardboiled.patient.ui.activity.chat.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.chat.ChatPresenter.ChatPresenter;

//发送消息成功model
public class SendTextMessageModel {


    /**
     * from_user_id : 501
     * origin : http://10.0.1.113:8080/static/messages/images/501_501_G1733OL53B.jpg
     * thumb : http://10.0.1.113:8080/static/messages/images/501_501_thumb_G1733OL53B.jpg
     * time : 1553160881
     * to_user_id : 501
     */

    private int from_user_id;
    private String origin;
    private long create_time;
    private int expire_date;   //当前有效期时间
    private int current_index;  //当前使用次数
    private String text;
    private int to_user_id;
    private int msg_index;

    public int getMsg_index() {
        return msg_index;
    }

    public void setMsg_index(int msg_index) {
        this.msg_index = msg_index;
    }



    public int getFrom_user_id() {
        return from_user_id;
    }

    public String getText() {
        return text;
    }

    public int getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(int expire_date) {
        this.expire_date = expire_date;
    }

    public int getCurrent_index() {
        return current_index;
    }

    public void setCurrent_index(int current_index) {
        this.current_index = current_index;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFrom_user_id(int from_user_id) {
        this.from_user_id = from_user_id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(int to_user_id) {
        this.to_user_id = to_user_id;
    }


    //发送文本消息
    public void SendTextmessage(Context context, String text, String userId, ChatPresenter presenter){
        HashMap<String,Object>params=new HashMap<>();
        params.put("text",text);
        params.put("to_user_id",Integer.parseInt(userId));
        RequestUtils.post(context, Url.SendTextMessages, params,new HashMap<>(), new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result.result==0){
                    Gson gson=new Gson();
                    SendTextMessageModel messageModel=gson.fromJson(gson.toJson(result.getDatas()),SendTextMessageModel.class);
                    if (messageModel!=null){
                        presenter.SendTextSuccess(messageModel);
                    }else{
                        presenter.SendFaire(result.message);
                    }
                }

            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                presenter.SendFaire(errorMsg);
            }
        });



    }






    //发送语音消息
    public void SendAudioMessage(Context context, String voice,int time, String userId,String key, ChatPresenter presenter){

        List<File>fileList=new ArrayList<>();

            File file=new File(voice);
            fileList.add(file);

        RequestUtils.postVoiceMessage(context, Url.SendAudioMessages, fileList,key, time,userId,new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result.result==0){
                    Gson gson=new Gson();
                    SendTextMessageModel messageModel=gson.fromJson(gson.toJson(result.getDatas()),SendTextMessageModel.class);
                    if (messageModel!=null){
                        presenter.SendAudioSuccess(messageModel);
                    }else{
                        presenter.SendFaire(result.message);
                    }
                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                presenter.SendFaire(errorMsg);
            }
        });



    }



}
