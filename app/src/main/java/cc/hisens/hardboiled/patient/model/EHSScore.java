package cc.hisens.hardboiled.patient.model;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.ehsassess.EHSPresenter;

public class EHSScore {


    /**
     * result : 0
     * message : 请求成功
     * datas : [{"user_id":500,"score":1,"time":1551925055}]
     */

    private int result;
    private String message;
    private List<DatasBean> datas;



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

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * user_id : 500
         * score : 1
         * time : 1551925055
         */

        private int user_id;
        private int score;
        private int time;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }


    //获取用户的EHS的分数
    public void GetEHSScore(Context context,int Userid, EHSPresenter presenter){
        Map<String,Object> params=new HashMap<>();
        params.put("user_id",Userid);
        RequestUtils.get(context, Url.getEhs, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {

                if (result.result==0){
                    Gson gson = new Gson();
                    EHSScore ehsScore = gson.fromJson(gson.toJson(result), EHSScore.class);
                    presenter.getSuccessFul(ehsScore);
                }else{
                    presenter.getFair(result.getMessage());
                }


            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                presenter.getFair(errorMsg);
            }
        });


    }


}
