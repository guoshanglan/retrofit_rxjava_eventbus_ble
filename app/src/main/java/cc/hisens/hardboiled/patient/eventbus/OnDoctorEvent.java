package cc.hisens.hardboiled.patient.eventbus;


//是否关注医生的model类
public class OnDoctorEvent {
    public boolean isFollowed;

    public OnDoctorEvent(boolean isFollowed){
        this.isFollowed=isFollowed;
    }


}
