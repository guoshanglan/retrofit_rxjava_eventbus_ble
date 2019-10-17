package cc.hisens.hardboiled.patient.model;

import java.util.List;

public class GetEdinfoBean {


    /**
     * result : 0
     * message : 请求成功
     * datas : [{"id":172,"start_time":1555560163,"strength":500},{"id":172,"start_time":1555560163,"strength":500}]
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
         * id : 172
         * start_time : 1555560163
         * strength : 500
         */

        private int id;
        private long start_time;
        private int strength;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getStart_time() {
            return start_time;
        }

        public void setStart_time(long start_time) {
            this.start_time = start_time;
        }

        public int getStrength() {
            return strength;
        }

        public void setStrength(int strength) {
            this.strength = strength;
        }
    }
}
