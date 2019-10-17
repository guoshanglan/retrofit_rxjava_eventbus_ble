package cc.hisens.hardboiled.patient.ui.activity.iief_5;

import android.content.Context;

import cc.hisens.hardboiled.patient.model.IIEF5Score;
import cc.hisens.hardboiled.patient.ui.activity.login.model.User;

public interface IIEFView {
    Context getContext();
    String Userid();

    /**
     * 获取成功
     * @param score
     */
    void GetSuccessFul(IIEF5Score score);



    /**
     * 获取失败
     * @param str
     */
    void getFair(String str);

}
