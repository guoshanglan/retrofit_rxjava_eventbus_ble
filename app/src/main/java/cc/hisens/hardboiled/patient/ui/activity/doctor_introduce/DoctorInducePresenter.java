package cc.hisens.hardboiled.patient.ui.activity.doctor_introduce;

import cc.hisens.hardboiled.patient.base.BasePresenter;

public class DoctorInducePresenter extends BasePresenter<DoctorInduceView> {
    public DoctorInduceModel model;

    public DoctorInducePresenter (){
        model=new DoctorInduceModel();
    }

    public void  getData(){
        model.getData(mView.getContext(),mView.DoctorId(),this);
    }

    public void Success(DoctorInduceModel model){
        mView.Success(model);
    }

    public void Fair(String msg){
        mView.Fail(msg);
    }



}
