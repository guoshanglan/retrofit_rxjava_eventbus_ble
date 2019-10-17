package cc.hisens.hardboiled.patient.model;

import android.content.Intent;

import java.io.Serializable;
import java.util.List;


//用户订单信息model
public class OrderInfoModel implements Serializable {


    /**
     * result : 0
     * message : 请求成功
     * datas : {"list":[{"id":364,"order_no":"SJo9TOfsnC7g0s9MksuWO2oR2ISUnZXQ0","status":3012,"user_id":50003,"doctor_id":512,"current_index":0,"expire_date":1564646831,"msg_index":["33","34"],"create_time":1564479842,"update_time":1564483097},{"id":365,"order_no":"SJo9TOfsnC7g0s9MksuWO2oR2ISUnZXQ1","status":3010,"user_id":50003,"doctor_id":512,"current_index":0,"expire_date":0,"msg_index":[],"create_time":1564479842,"update_time":1564479842},{"id":367,"order_no":"09WG3aSZ8OZOc4XZoKpb2xiyP4viWNZR0","status":3010,"user_id":50003,"doctor_id":512,"current_index":0,"expire_date":0,"msg_index":[],"create_time":1564482784,"update_time":1564482784}],"use_count":1,"unused_count":2}
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

    public static class DatasBean implements Serializable {
        /**
         * list : [{"id":364,"order_no":"SJo9TOfsnC7g0s9MksuWO2oR2ISUnZXQ0","status":3012,"user_id":50003,"doctor_id":512,"current_index":0,"expire_date":1564646831,"msg_index":["33","34"],"create_time":1564479842,"update_time":1564483097},{"id":365,"order_no":"SJo9TOfsnC7g0s9MksuWO2oR2ISUnZXQ1","status":3010,"user_id":50003,"doctor_id":512,"current_index":0,"expire_date":0,"msg_index":[],"create_time":1564479842,"update_time":1564479842},{"id":367,"order_no":"09WG3aSZ8OZOc4XZoKpb2xiyP4viWNZR0","status":3010,"user_id":50003,"doctor_id":512,"current_index":0,"expire_date":0,"msg_index":[],"create_time":1564482784,"update_time":1564482784}]
         * use_count : 1
         * unused_count : 2
         */

        private int use_count;
        private int unused_count;
        private List<ListBean> list;

        public int getUse_count() {
            return use_count;
        }

        public void setUse_count(int use_count) {
            this.use_count = use_count;
        }

        public int getUnused_count() {
            return unused_count;
        }

        public void setUnused_count(int unused_count) {
            this.unused_count = unused_count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean  implements Serializable{
            /**
             * id : 364
             * order_no : SJo9TOfsnC7g0s9MksuWO2oR2ISUnZXQ0
             * status : 3012
             * user_id : 50003
             * doctor_id : 512
             * current_index : 0
             * expire_date : 1564646831
             * msg_index : ["33","34"]
             * create_time : 1564479842
             * update_time : 1564483097
             */

            private int id;
            private String order_no;
            private int status;
            private int user_id;
            private int doctor_id;
            private int current_index; //当前使用次数
            private int expire_date; //当前有效期时间
            private int create_time;
            private int update_time;
            private List<Integer> msg_index;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOrder_no() {
                return order_no;
            }

            public void setOrder_no(String order_no) {
                this.order_no = order_no;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getDoctor_id() {
                return doctor_id;
            }

            public void setDoctor_id(int doctor_id) {
                this.doctor_id = doctor_id;
            }

            public int getCurrent_index() {
                return current_index;
            }

            public void setCurrent_index(int current_index) {
                this.current_index = current_index;
            }

            public int getExpire_date() {
                return expire_date;
            }

            public void setExpire_date(int expire_date) {
                this.expire_date = expire_date;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public List<Integer> getMsg_index() {
                return msg_index;
            }

            public void setMsg_index(List<Integer> msg_index) {
                this.msg_index = msg_index;
            }
        }
    }
}
