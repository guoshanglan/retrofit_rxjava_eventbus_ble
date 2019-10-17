package cc.hisens.hardboiled.patient.ui.activity.consultion;

import android.content.Context;

import java.io.File;
import java.util.List;

import cc.hisens.hardboiled.patient.ui.activity.doctor_introduce.DoctorInduceModel;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.model.ConSultionModel;

public interface ConsultTypeView {
    Context getContext();   //需要的上下文对象

    int DoctorId(); //医生id
    String desc(); //商品描述
    String product_no(); //资费包编号
    int Total_pack(); //资费包的数量
    List<String>image(); //图片数组
    String Content(); //首条消息的文字输入




    /**
     * 查询成功
     * @param msg 上传图片成功
     */
    void Success(String msg);
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
