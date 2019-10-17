package cc.hisens.hardboiled.patient.model;

import java.util.List;

public class GetEdRecordModel {


    /**
     * result : 0
     * message : 请求成功
     * datas : [{"id":23,"user_id":50003,"start_time":1555560163,"end_time":1566815074,"factors":"因素1","ave_strength":200,"type":1,"list":[{"start_time":1555560163,"strength":500}],"create_time":1551753076},{"id":24,"user_id":50003,"start_time":1555560163,"end_time":1566815074,"factors":"因素2","ave_strength":200,"type":1,"list":[{"start_time":1555560163,"strength":500},{"start_time":1555560163,"strength":500},{"start_time":1555560163,"strength":255}],"create_time":1551753076}]
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
         * id : 23
         * user_id : 50003
         * start_time : 1555560163
         * end_time : 1566815074
         * factors : 因素1
         * ave_strength : 200
         * type : 1
         * list : [{"start_time":1555560163,"strength":500}]
         * create_time : 1551753076
         */

        private int id;
        private int user_id;
        private long start_time;
        private long end_time;
        private String factors;
        private int ave_strength;
        private int type;
        private int create_time;
        private List<GetEdinfoBean.DatasBean> list;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public long getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public long getEnd_time() {
            return end_time;
        }

        public void setEnd_time(int end_time) {
            this.end_time = end_time;
        }

        public String getFactors() {
            return factors;
        }

        public void setFactors(String factors) {
            this.factors = factors;
        }

        public int getAve_strength() {
            return ave_strength;
        }

        public void setAve_strength(int ave_strength) {
            this.ave_strength = ave_strength;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public List<GetEdinfoBean.DatasBean> getList() {
            return list;
        }

        public void setList(List<GetEdinfoBean.DatasBean> list) {
            this.list = list;
        }



    }
}
