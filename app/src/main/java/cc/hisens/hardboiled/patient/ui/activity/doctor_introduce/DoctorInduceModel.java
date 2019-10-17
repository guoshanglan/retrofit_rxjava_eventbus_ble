package cc.hisens.hardboiled.patient.ui.activity.doctor_introduce;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.hisens.hardboiled.patient.db.bean.Doctor;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.presenter.DoctorDetailPresenter;
import cc.hisens.hardboiled.patient.ui.activity.login.GetVoliatCodeActivity;
import cc.hisens.hardboiled.patient.ui.activity.login.LoginActivity;

public class DoctorInduceModel {
    private int result;
    private String message;
    private List<Doctor> datas;


    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Doctor> getDatas() {
        return datas;
    }





    public void getData(Context context, int doctorId, DoctorInducePresenter presenter){
        List<Integer>params=new ArrayList<>();
        params.add(doctorId);



        RequestUtils.get3(context, Url.DoctorInfo, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result.result==0){
                    Gson gson=new Gson();
                    DoctorInduceModel model=gson.fromJson(gson.toJson(result),DoctorInduceModel.class);
                    presenter.Success(model);


                }else{
                    if (result.result==1002){
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }else{
                        presenter.Fair(result.message);
                    }

                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                if (errorMsg.equals("retrofit2.adapter.rxjava2.HttpException: HTTP 401 Unauthorized")){
                    presenter.Fair("未授权访问");
                }else {
                    errorMsg="网络断开，请检查网络设置";
                    presenter.Fair(errorMsg);
                }

            }
        });
    }




}
