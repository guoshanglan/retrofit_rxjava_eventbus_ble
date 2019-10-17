package cc.hisens.hardboiled.patient.ui.activity.ehsassess;

import android.content.Context;

import cc.hisens.hardboiled.patient.model.EHSScore;


public interface EHSView {

    Context getContext();
    String Userid();

    /**
     * 获取成功
     * @param score
     */
    void GetSuccessFul(EHSScore score);



    /**
     * 获取失败
     * @param str
     */
    void getFair(String str);

}
