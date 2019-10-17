package cc.hisens.hardboiled.patient.db.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.model
 * @fileName Ed
 * @date on 2017/6/6 17:02
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 * 蓝牙的数据接收类bean，一次测量下
 */


public class Ed extends RealmObject implements Serializable{

    public static final String START_SLEEP = "startTimestamp";
    public static final String NOTES = "notes";

    //开始睡眠时间
    @PrimaryKey
    private long startTimestamp;
    private int id;
    //结束睡眠时间
    private long endTimestamp;

    //是否在睡眠状态下测试
    private boolean isSleep;

    //是否有干扰,无干扰是NPT,有干扰是AVSS
    private boolean isInterferential;

    //干扰因素（可为药物和其他因素，多个选项时中间用逗号隔开）
    private String interferenceFactor;

    //一次测量状态下的所有勃起数据
    private RealmList<EdInfo> rlist;    //这样设置是为了在数据库中插入其他的数据对象

    //最大强度
    private int maxStrength;

    //最大强度持续时间
    private long maxDuration;

    private int average;  //平均强度
    private boolean isSync;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public boolean isSleep() {
        return isSleep;
    }

    public void setSleep(boolean sleep) {
        isSleep = sleep;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public RealmList<EdInfo> getEdInfos() {
        return rlist;
    }

    public void setEdInfos(List<EdInfo> edInfos) {
        this.rlist = new RealmList<>();
        this.rlist.addAll(edInfos);
    }

    public boolean isInterferential() {
        return isInterferential;
    }

    public void setInterferential(boolean interferential) {
        isInterferential = interferential;
    }

    public String getInterferenceFactor() {
        return interferenceFactor;
    }

    public void setInterferenceFactor(String interferenceFactor) {
        this.interferenceFactor = interferenceFactor;
    }

    public int getMaxStrength() {
        return maxStrength;
    }

    public void setMaxStrength(int maxStrength) {
        this.maxStrength = maxStrength;
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(long maxDuration) {
        this.maxDuration = maxDuration;
    }



    @Override
    public String toString() {
        return "Ed{" +
                "startTimestamp=" + startTimestamp +
                ", endTimestamp=" + endTimestamp +
                ", edInfos=" + rlist +
                '}';
    }



}
