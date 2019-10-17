package cc.hisens.hardboiled.patient.ui.activity.score.view;

import android.content.Context;

import cc.hisens.hardboiled.patient.eventbus.UpLoadScore;
import cc.hisens.hardboiled.patient.model.EHSScore;
import cc.hisens.hardboiled.patient.model.IIEF5Score;
import cc.hisens.hardboiled.patient.model.UploadEHS;
import cc.hisens.hardboiled.patient.model.UploadIIEF;

public interface ScoreView {
    int EHSScore();
    Context getContext();   //需要的上下文对象
    /**
     * 上传成功
     * @param ehsScore  ehs评分的model类
     */
    void UploadEHSSuccess(UploadEHS ehsScore);


    /**上传失败
     * @param str
     */
    void FailedEHSError(String str);



//下面是IIEF所需要的数据
    int [] IIEFScore();

    /**
     * 上传成功
     * @param iief5Score  IIEF评分的model类
     */
    void UploadIIEFSuccess(UploadIIEF iief5Score);


    /**上传失败
     * @param str
     */
    void FailedIIEFError(String str);

}
