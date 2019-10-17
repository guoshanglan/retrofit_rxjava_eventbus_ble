package cc.hisens.hardboiled.patient.ui.activity.searchdoctor.view;

import android.content.Context;

import cc.hisens.hardboiled.patient.MyApplication;
import cc.hisens.hardboiled.patient.ui.activity.searchdoctor.model.SearchDoctorModel;

public interface SearchDoctorView {

    Context getContext();   //需要的上下文对象
    String SearchKey();  //查询的医生姓名和医院名称
    int PageIndex(); //页数索引值
    String City();  //选择的城市
    MyApplication application();

    /**
     * 查询成功
     * @param searchDoctorModel   查询医生列表的model类
     */
    void SearchSuccess(SearchDoctorModel searchDoctorModel);
    /**
     * 查询成功,没有更多数据
     * @param str   没有跟他更多数据了
     *
     */

    void SearNomessage(String str);


    /**查询失败
     * @param str
     */
    void SearchFail(String str);

}
