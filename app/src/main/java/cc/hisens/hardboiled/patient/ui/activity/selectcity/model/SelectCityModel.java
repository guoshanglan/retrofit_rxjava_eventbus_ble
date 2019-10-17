package cc.hisens.hardboiled.patient.ui.activity.selectcity.model;

import java.util.List;

public class SelectCityModel {


    /**
     * result : 0
     * message : 请求成功
     * datas : [{"province":"广东省","cities":["深圳市","广州市"]}]
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
         * province : 广东省
         * cities : ["深圳市","广州市"]
         */

        private String province;
        private List<String> cities;
        private boolean isClick=false;

        public boolean isClick() {
            return isClick;
        }

        public void setClick(boolean click) {
            isClick = click;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public List<String> getCities() {
            return cities;
        }

        public void setCities(List<String> cities) {
            this.cities = cities;
        }
    }
}
