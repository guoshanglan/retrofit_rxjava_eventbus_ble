package cc.hisens.hardboiled.patient.ui.activity.doctordetail.model;

import java.util.List;

import cc.hisens.hardboiled.patient.db.bean.Doctor;


//用户关注列表
public class FollowModel {


    /**
     * result : 0
     * message : 请求成功
     * datas : [{"id":16,"user_id":50006,"doctor_id":511,"created_time":1564816146}]
     */

    private int result;
    private String message;
    private List<Doctor> datas;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Doctor> getDatas() {
        return datas;
    }

    public void setDatas(List<Doctor> datas) {
        this.datas = datas;
    }


}
