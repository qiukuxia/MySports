package com.example.mysports.database;

import com.google.gson.annotations.Expose;
import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Roc on 2018/4/17.
 */

public class Pos extends SugarRecord implements Serializable {

    @Expose
    private int num;
    @Expose
    private double lon;
    @Expose
    private double lat;
    @Expose
    private String path;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
