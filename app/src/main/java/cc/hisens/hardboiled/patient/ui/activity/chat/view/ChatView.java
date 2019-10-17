package cc.hisens.hardboiled.patient.ui.activity.chat.view;

import android.content.Context;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import cc.hisens.hardboiled.patient.ui.activity.chat.model.SendImageMessageModel;
import cc.hisens.hardboiled.patient.ui.activity.chat.model.SendTextMessageModel;


//聊天view

public interface ChatView {

    String TextMessage();  //电话号码
    List<LocalMedia> Image();  //图片信息
    String AudioPath(); //录音
    int AudioTime(); //录音时长
    Context getContext();   //需要的上下文对象
    String to_User_id(); //发送给谁的用户id


    /**
     * 发送成功
     * @param message
     */
    void SendTextSuccessFul(SendTextMessageModel message);

    void SendImageSuccessFul(SendImageMessageModel message);
    void SendAudioSuccessFul(SendTextMessageModel message);
    /**
     * 发送失败
     * @param errormessage
     */
    void SendFailedError(String errormessage);


}
