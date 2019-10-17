package cc.hisens.hardboiled.patient.ui.activity.doctordetail.presenter;

import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.model.ConSultionModel;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.view.DoctorDetailView;

public class DoctorDetailPresenter extends BasePresenter<DoctorDetailView> {
    public ConSultionModel model;

    public DoctorDetailPresenter() {
        this.model = new ConSultionModel();
    }

    //获取数据
    public void getScoreList(){
         model.getData(mView.getContext(),mView.DoctorId(),mView.PageIndex(),this);
    }

    //获取关注状态
    public void getFollowed(){

    }

    //评价列表返回成功
    public  void  Success(ConSultionModel model){
         mView.Success(model);
    }

    public void  getFollowedSuccess(boolean type){}


    public void Fair(String str){
        mView.Fail(str);
    }







}
