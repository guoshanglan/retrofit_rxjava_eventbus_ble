package cc.hisens.hardboiled.patient.eventbus;

//聊天页面发送的信息，当前会话回调的刷新的信息类
public class OnMessage {
    public boolean  isMessageCome;

    public OnMessage(boolean isMessageCome){
        this.isMessageCome=isMessageCome;
    }

}
