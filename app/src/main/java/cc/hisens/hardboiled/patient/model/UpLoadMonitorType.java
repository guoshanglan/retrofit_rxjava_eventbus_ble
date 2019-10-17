package cc.hisens.hardboiled.patient.model;

public class UpLoadMonitorType {


    /**
     * result : 0
     * message : 请求成功
     * datas : {"id":0,"user_id":50003,"type":1,"factors":"bbb","create_time":1566888339}
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
         * id : 0
         * user_id : 50003
         * type : 1
         * factors : bbb
         * create_time : 1566888339
         */

        private int id;
        private int user_id;
        private int type;
        private String factors;
        private int create_time;

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getFactors() {
            return factors;
        }

        public void setFactors(String factors) {
            this.factors = factors;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }
    }
}
