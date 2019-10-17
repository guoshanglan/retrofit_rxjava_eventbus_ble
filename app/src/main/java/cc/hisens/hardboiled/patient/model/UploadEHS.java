package cc.hisens.hardboiled.patient.model;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;

import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.score.presenter.UpLoadIIEFScorePresenter;

public class UploadEHS {


    /**
     * result : 0
     * message : 请求成功
     * datas : {"score":1,"time":1552978132}
     */

    private int result;
    private String message;
    private EHSScore.DatasBean datas;

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

    public EHSScore.DatasBean getDatas() {
        return datas;
    }

    public void setDatas(EHSScore.DatasBean datas) {
        this.datas = datas;
    }



    //上传分数
    public void UploadEHSScore(Context context, int score, UpLoadIIEFScorePresenter presenter) {
        HashMap<String, Object> ehsMap = new HashMap<>();
        ehsMap.put("score", score);
        RequestUtils.post(context, Url.uploadEhsScore, ehsMap, new HashMap<>(), new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result != null) {

                    if (result.result == 0) {
                        Gson gson = new Gson();
                        UploadEHS ehsScore = gson.fromJson(gson.toJson(result), UploadEHS.class);
                        presenter.UploadEHSSuccess(ehsScore);
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
