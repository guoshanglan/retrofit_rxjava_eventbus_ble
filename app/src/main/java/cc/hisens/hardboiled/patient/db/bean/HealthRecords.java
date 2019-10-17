package cc.hisens.hardboiled.patient.db.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.model
 * @fileName HealthRecords
 * @date on 2017/6/15 16:23
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */


//身体健康地bean

public class HealthRecords  implements Serializable {


    /**
     * result : 0
     * message : 请求成功
     * datas : {"user_id":50000,"name":"lucy","birthday":19991019,"height":168,"weight":60,"marriage":1,"breeding":1,"sexual_life":1,"morning_wood":1,"sexual_stimulus":1,"duration":1,"disease":["12a","12b"],"trauma":["a1","a2","a3"],"medicine":["b1","b2","b3"],"smoke":2,"drink":3,"create_time":1553075773,"update_time":1553075773}
     */

    private int result;
    private String message;
    private DatasBean datas;


    @Override
    public String toString() {
        return "HealthRecords{" +
                "result=" + result +
                ", message='" + message + '\'' +
                ", datas=" + datas +
                '}';
    }

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
         * user_id : 50006
         * name :
         * birthday : 0
         * height : 0
         * weight : 0
         * marriage : 0
         * breeding : 1
         * sexual_life : 1
         * morning_wood : 0
         * sexual_stimulus : 0
         * duration : 0
         * disease : [""]
         * trauma : [""]
         * medicine : [""]
         * smoke : 0
         * drink : 0
         * create_time : 1567221982
         * update_time : 1567750050
         */

        private int user_id;
        private String name;
        private long birthday;
        private int height;
        private int weight;
        private int marriage;
        private int breeding;
        private int sexual_life;
        private int morning_wood;
        private int sexual_stimulus;
        private long duration;
        private int smoke;
        private int drink;
        private int create_time;
        private int update_time;
        private List<String> disease;
        private List<String> trauma;
        private List<String> medicine;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getBirthday() {
            return birthday;
        }

        public void setBirthday(long birthday) {
            this.birthday = birthday;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getMarriage() {
            return marriage;
        }

        public void setMarriage(int marriage) {
            this.marriage = marriage;
        }

        public int getBreeding() {
            return breeding;
        }

        public void setBreeding(int breeding) {
            this.breeding = breeding;
        }

        public int getSexual_life() {
            return sexual_life;
        }

        public void setSexual_life(int sexual_life) {
            this.sexual_life = sexual_life;
        }

        public int getMorning_wood() {
            return morning_wood;
        }

        public void setMorning_wood(int morning_wood) {
            this.morning_wood = morning_wood;
        }

        public int getSexual_stimulus() {
            return sexual_stimulus;
        }

        public void setSexual_stimulus(int sexual_stimulus) {
            this.sexual_stimulus = sexual_stimulus;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public int getSmoke() {
            return smoke;
        }

        public void setSmoke(int smoke) {
            this.smoke = smoke;
        }

        public int getDrink() {
            return drink;
        }

        public void setDrink(int drink) {
            this.drink = drink;
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

        public List<String> getDisease() {
            return disease;
        }

        public void setDisease(List<String> disease) {
            this.disease = disease;
        }

        public List<String> getTrauma() {
            return trauma;
        }

        public void setTrauma(List<String> trauma) {
            this.trauma = trauma;
        }

        public List<String> getMedicine() {
            return medicine;
        }

        public void setMedicine(List<String> medicine) {
            this.medicine = medicine;
        }

        /**
         * user_id : 50000
         * name : lucy
         * birthday : 19991019
         * height : 168
         * weight : 60
         * marriage : 1
         * breeding : 1
         * sexual_life : 1
         * morning_wood : 1
         * sexual_stimulus : 1
         * duration : 1
         * disease : ["12a","12b"]
         * trauma : ["a1","a2","a3"]
         * medicine : ["b1","b2","b3"]
         * smoke : 2
         * drink : 3
         * create_time : 1553075773
         * update_time : 1553075773
         */
        @Override
        public String toString() {
            return "DatasBean{" +
                    "user_id=" + user_id +
                    ", name='" + name + '\'' +
                    ", birthday=" + birthday +
                    ", height=" + height +
                    ", weight=" + weight +
                    ", marriage=" + marriage +
                    ", breeding=" + breeding +
                    ", sexual_life=" + sexual_life +
                    ", morning_wood=" + morning_wood +
                    ", sexual_stimulus=" + sexual_stimulus +
                    ", duration=" + duration +
                    ", smoke=" + smoke +
                    ", drink=" + drink +
                    ", create_time=" + create_time +
                    ", update_time=" + update_time +
                    ", disease=" + disease +
                    ", trauma=" + trauma +
                    ", medicine=" + medicine +
                    '}';
        }
    }
}
