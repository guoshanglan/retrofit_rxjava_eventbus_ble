package cc.hisens.hardboiled.patient.db.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Doctor extends RealmObject {
    @PrimaryKey
    private int doctor_id;
    private String phone;
    private String name;
    private String head_url;
    private String thumb_url;
    private int level;  //医生等级  医生的职称1:主任医师;2:副主任医师;3:主治医师;4:住院医师;5:助理医师
    private String speciality;
    private String id_number;
    private String id_card;
    private int pass_validation;
    private String rejection_reason;
    private String authentication_files;
    private int authentication_time;
    private int created_time;
    private int update_time;
    private float ave_score;
    private int score;  // 医生评分信息
    private int number; //医生总咨询人数
    private String once_product_no; //单次资费包编号
    private String pack_product_no; //多次资费包编号
    private IntroBean intro;
    private RealmList<WorkplacesBean> workplaces;
    private boolean isConsultation=false;  //是否咨询过
    private String title; //标题 这两个字段是在我关注的列表中使用的效果
    private String titleId; //标题id
    private int type;  //用这个标记来标记是否问诊和关注都存在,1代表我关注的，2：代表历史问诊，3：代表两者都有过


    public Doctor(String title, String titleId) {
        this.title = title;
        this.titleId = titleId;
    }

    public Doctor(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public boolean isConsultation() {
        return isConsultation;
    }

    public void setConsultation(boolean consultation) {
        isConsultation = consultation;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public int getScore() {
        return score;
    }

    public float getAve_score() {
        return ave_score;
    }

    public void setAve_score(float ave_score) {
        this.ave_score = ave_score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getOnce_product_no() {
        return once_product_no;
    }

    public void setOnce_product_no(String once_product_no) {
        this.once_product_no = once_product_no;
    }

    public String getPack_product_no() {
        return pack_product_no;
    }

    public void setPack_product_no(String pack_product_no) {
        this.pack_product_no = pack_product_no;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public int getPass_validation() {
        return pass_validation;
    }

    public void setPass_validation(int pass_validation) {
        this.pass_validation = pass_validation;
    }

    public String getRejection_reason() {
        return rejection_reason;
    }

    public void setRejection_reason(String rejection_reason) {
        this.rejection_reason = rejection_reason;
    }

    public String getAuthentication_files() {
        return authentication_files;
    }

    public void setAuthentication_files(String authentication_files) {
        this.authentication_files = authentication_files;
    }

    public int getAuthentication_time() {
        return authentication_time;
    }

    public void setAuthentication_time(int authentication_time) {
        this.authentication_time = authentication_time;
    }

    public int getCreated_time() {
        return created_time;
    }

    public void setCreated_time(int created_time) {
        this.created_time = created_time;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }

    public IntroBean getIntro() {
        return intro;
    }

    public void setIntro(IntroBean intro) {
        this.intro = intro;
    }

    public List<WorkplacesBean> getWorkplaces() {
        return workplaces;
    }

    public void setWorkplaces(RealmList<WorkplacesBean> workplaces) {
        this.workplaces = workplaces;
    }





}
