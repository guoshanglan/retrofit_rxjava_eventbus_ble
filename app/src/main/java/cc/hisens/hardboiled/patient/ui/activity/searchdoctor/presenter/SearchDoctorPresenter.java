package cc.hisens.hardboiled.patient.ui.activity.searchdoctor.presenter;

import android.text.TextUtils;

import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.ui.activity.searchdoctor.model.SearchDoctorModel;
import cc.hisens.hardboiled.patient.ui.activity.searchdoctor.view.SearchDoctorView;

public class SearchDoctorPresenter extends BasePresenter<SearchDoctorView> {
    public SearchDoctorModel searchDoctorModel;


    public SearchDoctorPresenter(){
        searchDoctorModel=new SearchDoctorModel();
    }

    //查询医生
    public void SearchDoctor(){
        searchDoctorModel.Search(mView.getContext(),mView.SearchKey(),mView.City(),mView.application(),mView.PageIndex(),this);
    }



    //查询成功回调
    public void SearchSuccess(SearchDoctorModel searchDoctorModel){
        if (searchDoctorModel!=null)
        mView.SearchSuccess(searchDoctorModel);
    }

    //查询成功回调
    public void SearchNoDoctor(String message){
        mView.SearNomessage(message);

    }

    //查询失败回调
    public void SearchFail(String str){
        if (!TextUtils.isEmpty(str))
            mView.SearchFail(str);

    }

}
