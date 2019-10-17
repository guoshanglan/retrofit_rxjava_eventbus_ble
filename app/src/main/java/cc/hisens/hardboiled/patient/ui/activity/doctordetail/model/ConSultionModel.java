package cc.hisens.hardboiled.patient.ui.activity.doctordetail.model;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.presenter.DoctorDetailPresenter;
import cc.hisens.hardboiled.patient.ui.activity.login.GetVoliatCodeActivity;
import cc.hisens.hardboiled.patient.ui.activity.login.LoginActivity;
import cc.hisens.hardboiled.patient.ui.activity.login.model.User;

public class ConSultionModel {


    /**
     * result : 0
     * message : 请求成功
     * datas : {"list":[{"score_id":1,"doctor_id":512,"content":"666","score":60,"user_id":50012,"phone":"13049858600","created_time":1562121976},{"score_id":2,"doctor_id":512,"content":"666","score":60,"user_id":50014,"phone":"13049858600","created_time":1562122040},{"score_id":3,"doctor_id":512,"content":"666","score":60,"user_id":50014,"phone":"13049858600","created_time":1562124147},{"score_id":4,"doctor_id":512,"content":"666","score":60,"user_id":50014,"phone":"13049858600","created_time":1562124884}],"total_count":4}
     */

    private int result;
    private String message;
    private DatasBean datas;

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

    public DatasBean getDatas() {
        return datas;
    }

    public void setDatas(DatasBean datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * list : [{"score_id":1,"doctor_id":512,"content":"666","score":60,"user_id":50012,"phone":"13049858600","created_time":1562121976},{"score_id":2,"doctor_id":512,"content":"666","score":60,"user_id":50014,"phone":"13049858600","created_time":1562122040},{"score_id":3,"doctor_id":512,"content":"666","score":60,"user_id":50014,"phone":"13049858600","created_time":1562124147},{"score_id":4,"doctor_id":512,"content":"666","score":60,"user_id":50014,"phone":"13049858600","created_time":1562124884}]
         * total_count : 4
         */

        private int total_count;
        private List<ListBean> list;

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public List<ListBean> getList() {
            return list;
        }



        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * score_id : 1
             * doctor_id : 512
             * content : 666
             * score : 60
             * user_id : 50012
             * phone : 13049858600
             * created_time : 1562121976
             */

            private int score_id;
            private int doctor_id;
            private String content;
            private int score;
            private int user_id;
            private String phone;
            private int created_time;

            public int getScore_id() {
                return score_id;
            }

            public void setScore_id(int score_id) {
                this.score_id = score_id;
            }

            public int getDoctor_id() {
                return doctor_id;
            }

            public void setDoctor_id(int doctor_id) {
                this.doctor_id = doctor_id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public int getCreated_time() {
                return created_time;
            }

            public void setCreated_time(int created_time) {
                this.created_time = created_time;
            }
        }
    }



    //获取数据
    public void getData(Context context, int doctorid, int pageIndex, DoctorDetailPresenter presenter){
        Map<String,Object>params=new HashMap<>();
        params.put("doctor_id",doctorid);
        params.put("page",pageIndex);

        RequestUtils.get(context, Url.Pinlun, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result.result == 0) {
                    Gson gson = new Gson();
                    ConSultionModel model = gson.fromJson(gson.toJson(result), ConSultionModel.class);
                    presenter.Success(model);
                } else {
                    if (result.result==1002){
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }else {
                        presenter.Fair(result.message);
                    }
                    presenter.Fair(result.message);
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
