package cc.hisens.hardboiled.patient.ui.activity.ehsassess;

import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.model.EHSScore;
import cc.hisens.hardboiled.patient.model.IIEF5Score;

public class EHSPresenter extends BasePresenter<EHSView> {
    public EHSScore ehsScore;

    public EHSPresenter(){
        ehsScore=new EHSScore();
    }

    public void getScore(){
        ehsScore.GetEHSScore(mView.getContext(), Integer.parseInt(mView.Userid()),this);
    }

    //获取成功
    public void  getSuccessFul(EHSScore ehsScore){
        mView.GetSuccessFul(ehsScore);
    }

    //获取失败
    public void getFair(String errormsg){
        mView.getFair(errormsg);
    }


}
