package cc.hisens.hardboiled.patient.ui.activity.searchdoctor.model;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

import cc.hisens.hardboiled.patient.MyApplication;
import cc.hisens.hardboiled.patient.base.ActivityCollector;
import cc.hisens.hardboiled.patient.db.bean.Doctor;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.login.GetVoliatCodeActivity;
import cc.hisens.hardboiled.patient.ui.activity.login.LoginActivity;
import cc.hisens.hardboiled.patient.ui.activity.searchdoctor.presenter.SearchDoctorPresenter;

public class SearchDoctorModel {


    private List<Doctor> list;


    public List<Doctor> getList() {
        return list;
    }

    public void setList(List<Doctor> list) {
        this.list = list;
    }





    public void Search(Context context, String searchkey, String city, MyApplication application, int pageIndex, SearchDoctorPresenter presenter){
        HashMap<String, Object> params = new HashMap<>();
        params.put("page", pageIndex);
        params.put("city",city);
        params.put("longitude",application.location.getLongitude());
        params.put("latitude",application.location.getLatitude());
        params.put("name",searchkey);
        RequestUtils.get(context, Url.SearchDoctor, params,new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result != null) {

                    if (result.result == 0) {
                        Gson gson = new Gson();
                        SearchDoctorModel searchDoctorModel = new Gson().fromJson(gson.toJson(result.getDatas()), SearchDoctorModel.class);
                        presenter.SearchSuccess(searchDoctorModel);
                    } else {
                        if (result.result==1002){
                            context.startActivity(new Intent(context, LoginActivity.class));
                        }else {
                            presenter.SearchNoDoctor(result.message);
                        }
                    }

                }

            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                if (errorMsg.equals("retrofit2.adapter.rxjava2.HttpException: HTTP 401 Unauthorized")){
                  presenter.SearchFail("未授权访问");
                }else {
                    presenter.SearchFail("网络错误，请检查网络设置");
                }
            }
        });


    }

}
