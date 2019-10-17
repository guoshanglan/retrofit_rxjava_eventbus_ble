package cc.hisens.hardboiled.patient.db.bean;

import io.realm.RealmObject;


//医生介绍的bean类
public  class IntroBean  extends RealmObject  {
    /**
     * post_title : bbb
     * work_experience : 1
     * academic_experience : a
     */

    private String post_title;
    private String work_experience;
    private String academic_experience;

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getWork_experience() {
        return work_experience;
    }

    public void setWork_experience(String work_experience) {
        this.work_experience = work_experience;
    }

    public String getAcademic_experience() {
        return academic_experience;
    }

    public void setAcademic_experience(String academic_experience) {
        this.academic_experience = academic_experience;
    }
}
