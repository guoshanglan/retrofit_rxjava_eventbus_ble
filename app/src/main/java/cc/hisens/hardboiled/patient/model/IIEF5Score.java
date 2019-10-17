package cc.hisens.hardboiled.patient.model;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.hisens.hardboiled.patient.eventbus.UpLoadScore;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.iief_5.GetIIEFPresenter;
import cc.hisens.hardboiled.patient.ui.activity.score.presenter.UpLoadIIEFScorePresenter;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.model
 * @fileName IIEF5Score
 * @date on 2017/6/6 11:48
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public class IIEF5Score   {


    /**
     * result : 0
     * message : 请求成功
     * datas : [{"user_id":500,"scores":[1,2,3],"time":1551925055}]
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
         * scores : [1,2,3]
         * time : 1551925055
         */

        private int user_id;
        private int create_time;
        private List<Integer> scores;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public List<Integer> getScores() {
            return scores;
        }

        public void setScores(List<Integer> scores) {
            this.scores = scores;
        }
    }

    //获取用户的IIEF的分数
    public void GetIIEFScore(Context context,int Userid, GetIIEFPresenter presenter){
        Map<String,Object>params=new HashMap<>();
        params.put("user_id",Userid);
        RequestUtils.get(context, Url.getIIEF_5, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {

                if (result.result==0){
                    Gson gson = new Gson();
                    IIEF5Score iief5Score = gson.fromJson(gson.toJson(result), IIEF5Score.class);
                    presenter.getSuccessFul(iief5Score);
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
