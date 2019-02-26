package com.example.mysports.database;

import com.google.gson.annotations.Expose;
import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Roc on 2018/4/17.
 */

public class Paths extends SugarRecord implements Serializable {

    @Expose
    private String users;
    @Expose
    private long times;
    @Expose
    private int num;
    @Expose
    private int passt;
    @Expose
    private String speed;
    @Expose
    private String distance;
    @Expose
    private String score;
    @Expose
    private String remark;
    @Expose
    private String paths;

    public String getPaths() {
        return paths;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPasst() {
        return passt;
    }

    public void setPasst(int passt) {
        this.passt = passt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getSpeed(){return speed;}
    public void setSpeed(String speed){this.speed = speed;}
    public String getDistance(){return distance;}
    public void setDistance(String distance){this.distance= distance;}
    public String getScore(){return score;}
    public void setScore(String score){this.score = score;}
}
