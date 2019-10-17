package cc.hisens.hardboiled.patient.ui.activity.iief_5;

import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.model.IIEF5Score;

public class GetIIEFPresenter extends BasePresenter<IIEFView>{
   public IIEF5Score iief5Score;

   public GetIIEFPresenter(){
       iief5Score=new IIEF5Score();
   }


  public void getIIEFSCore(){
       iief5Score.GetIIEFScore(mView.getContext(), Integer.parseInt(mView.Userid()),this);
  }


  //获取成功
  public void  getSuccessFul(IIEF5Score iief5Score){
       mView.GetSuccessFul(iief5Score);
  }

  //获取失败
    public void getFair(String errormsg){
       mView.getFair(errormsg);
    }


}
