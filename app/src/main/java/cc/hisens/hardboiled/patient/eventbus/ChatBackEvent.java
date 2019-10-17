package cc.hisens.hardboiled.patient.eventbus;


//从聊天页面返回的eventbus的model类
public class ChatBackEvent {
    public boolean isChatBack;

    public ChatBackEvent(boolean isSuccess){
        this.isChatBack=isSuccess;
    }


}
