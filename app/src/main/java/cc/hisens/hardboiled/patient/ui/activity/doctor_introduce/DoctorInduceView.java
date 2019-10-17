package cc.hisens.hardboiled.patient.ui.activity.doctor_introduce;

import android.content.Context;

import cc.hisens.hardboiled.patient.ui.activity.doctordetail.model.ConSultionModel;

public interface DoctorInduceView {
    Context getContext();   //需要的上下文对象

    int DoctorId(); //医生id


    /**
     * 查询成功
     * @param doctorInduceModel   医生履历的model
     */
    void Success(DoctorInduceModel doctorInduceModel);
    /**
     * 查询成功,没有更多数据
     * @param str   没有跟他更多数据了
     *
     */

    /**失败
     * @param str
     */
    void Fail(String str);


}
