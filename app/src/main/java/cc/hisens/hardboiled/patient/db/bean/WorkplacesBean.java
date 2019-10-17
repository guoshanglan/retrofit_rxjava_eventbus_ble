package cc.hisens.hardboiled.patient.db.bean;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;


//医生工作地点的bean类

public class WorkplacesBean extends RealmObject {
    /**
     * area : lg
     * class : 1
     * name : 3
     */

    private String area;
    private int index;
    private String name;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getClassX() {
        return index;
    }

    public void setClassX(int classX) {
        this.index = classX;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
