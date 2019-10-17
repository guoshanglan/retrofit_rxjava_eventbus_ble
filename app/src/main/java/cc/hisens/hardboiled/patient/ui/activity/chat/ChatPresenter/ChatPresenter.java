package cc.hisens.hardboiled.patient.ui.activity.chat.ChatPresenter;



import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.ui.activity.chat.model.SendImageMessageModel;
import cc.hisens.hardboiled.patient.ui.activity.chat.model.SendTextMessageModel;
import cc.hisens.hardboiled.patient.ui.activity.chat.view.ChatView;

public class ChatPresenter  extends BasePresenter<ChatView>{

    public SendTextMessageModel messageModel;
    public SendImageMessageModel SendImagemessage2;

    public ChatPresenter(){
        messageModel=new SendTextMessageModel();
        SendImagemessage2=new SendImageMessageModel();
    }

    //发送文本信息
    public void SendTextMessage(){
        messageModel.SendTextmessage(mView.getContext(),mView.TextMessage(),mView.to_User_id(),this);
    }

    //发送图片
    public void  SendImageMessage(){
        SendImagemessage2.SendImagemessage2(mView.getContext(),mView.Image(),mView.to_User_id(),"images",this);
    }

    //发送录音
    public void  SendAudio(){
        messageModel.SendAudioMessage(mView.getContext(),mView.AudioPath(),mView.AudioTime(),mView.to_User_id(),"audio",this);
    }


    //发送文本成功
    public void  SendTextSuccess(SendTextMessageModel messageModel){
        mView.SendTextSuccessFul(messageModel);

    }
    //发送图片成功
    public void  SendImageSuccess(SendImageMessageModel messageModel){
        mView.SendImageSuccessFul(messageModel);
    }
    //发送录音文件成功
    public void  SendAudioSuccess(SendTextMessageModel messageModel){
        mView.SendAudioSuccessFul(messageModel);
    }

    //发送失败
    public void SendFaire(String errormsg){
        mView.SendFailedError(errormsg);
    }




}
