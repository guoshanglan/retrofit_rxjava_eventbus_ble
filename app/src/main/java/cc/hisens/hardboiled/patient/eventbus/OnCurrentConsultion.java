package cc.hisens.hardboiled.patient.eventbus;


//是否在当前会话的界面
public class OnCurrentConsultion {
    public boolean isOnResum;
    public OnCurrentConsultion(boolean onResum){
        this.isOnResum=onResum;
    }
}
