package cc.hisens.hardboiled.patient.ui.activity.consultion;

import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.model.ConSultionModel;

public class ConsultPresenter  extends BasePresenter<ConsultTypeView>{
    ConsultTypeModel model;
    public ConsultPresenter(){
        model=new ConsultTypeModel();
    }

    public void  getOrde(){
        model.getOrderNo(mView.getContext(),mView.DoctorId(),mView.Content(),mView.image(),this);
    }

    public void Success(String msg){
        mView.Success(msg);
    }

    public void Fair(String msg){
        mView.Fail(msg);
    }


}
