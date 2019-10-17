package cc.hisens.hardboiled.patient.eventbus;

import cc.hisens.hardboiled.patient.db.bean.ChatMessage;

//websocket的有信息来的类
public class OnMessageCome {
    public boolean  isMessageCome;
    public ChatMessage chatMessage;

    public OnMessageCome(boolean isMessageCome,ChatMessage message){
        this.isMessageCome=isMessageCome;
        chatMessage=message;
    }

}
