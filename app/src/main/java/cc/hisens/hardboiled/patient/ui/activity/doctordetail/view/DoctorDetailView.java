package cc.hisens.hardboiled.patient.ui.activity.doctordetail.view;

import android.content.Context;


import cc.hisens.hardboiled.patient.ui.activity.doctordetail.model.ConSultionModel;
import cc.hisens.hardboiled.patient.ui.activity.searchdoctor.model.SearchDoctorModel;

public interface DoctorDetailView {

    Context getContext();   //需要的上下文对象
    int PageIndex(); //页数索引值
    int DoctorId(); //医生id


    /**
     * 查询成功
     * @param conSultionModel   评价列表model
     */
    void Success(ConSultionModel conSultionModel);
    /**
     * 查询成功,没有更多数据
     * @param str   没有跟他更多数据了
     *
     */

    void NoMoreData(String str);


    /**失败
     * @param str
     */
    void Fail(String str);





}
