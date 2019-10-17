package cc.hisens.hardboiled.patient.ui.activity.score.presenter;

import android.text.TextUtils;

import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.eventbus.UpLoadScore;
import cc.hisens.hardboiled.patient.model.IIEF5Score;
import cc.hisens.hardboiled.patient.model.UploadEHS;
import cc.hisens.hardboiled.patient.model.UploadIIEF;
import cc.hisens.hardboiled.patient.ui.activity.score.view.ScoreView;

public class UpLoadIIEFScorePresenter extends BasePresenter<ScoreView> {
    public UploadIIEF iief5Score;
    public UploadEHS ehsScore;

    public UpLoadIIEFScorePresenter(){
        if (iief5Score==null){
            iief5Score=new UploadIIEF();
        }
        ehsScore=new UploadEHS();
    }

    public void  UpLoadIIefScore(){
        iief5Score.UploadIIEFScore(mView.getContext(),mView.IIEFScore(),this);
    }


    //登录成功,这边的mview，已经在basePresenter中定义过了
    public void UploadSuccess(UploadIIEF iief5Score) {
        if (iief5Score!=null)
            mView.UploadIIEFSuccess(iief5Score);

    }


    //获取失败
    public void loginFailed(String str) {
        if (!TextUtils.isEmpty(str)){
            mView.FailedIIEFError(str);
        }

    }

    public void  UpLoadehsScore(){
        ehsScore.UploadEHSScore(mView.getContext(),mView.EHSScore(),this);
    }


    //登录成功,这边的mview，已经在basePresenter中定义过了
    public void UploadEHSSuccess(UploadEHS ehsScore) {
        if (ehsScore!=null)
            mView.UploadEHSSuccess(ehsScore);

    }







}
