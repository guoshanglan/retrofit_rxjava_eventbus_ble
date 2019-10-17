package cc.hisens.hardboiled.patient.model;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import cc.hisens.hardboiled.patient.eventbus.UpLoadScore;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.score.presenter.UpLoadIIEFScorePresenter;

public class UploadIIEF {


    /**
     * result : 0
     * message : 请求成功
     * datas : {"scores":[2,5,5,5,5],"time":1566027079}
     */

    private int result;
    private String message;
    private IIEF5Score.DatasBean datas;

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

    public IIEF5Score.DatasBean getDatas() {
        return datas;
    }

    public void setDatas(IIEF5Score.DatasBean datas) {
        this.datas = datas;
    }




    //上传分数
    public void UploadIIEFScore(Context context, int[] score, UpLoadIIEFScorePresenter presenter) {
        HashMap<String, Object> IIEFMap = new HashMap<>();
        IIEFMap.put("scores", score);
        RequestUtils.post(context, Url.uploadIIEFScore, IIEFMap, new HashMap<>(), new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result != null) {

                    if (result.result == 0) {
                        Gson gson = new Gson();
                        UploadIIEF iief5Score = gson.fromJson(gson.toJson(result), UploadIIEF.class);
                        presenter.UploadSuccess(iief5Score);
                    } else {

                        presenter.loginFailed(result.message);
                    }

                }

            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                presenter.loginFailed(errorMsg);
            }
        });
    }
}
