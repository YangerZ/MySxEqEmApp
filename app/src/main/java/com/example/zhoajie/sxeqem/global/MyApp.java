package com.example.zhoajie.sxeqem.global;

import android.app.Application;

/**
 * Created by Administrator on 2017/11/20.
 */

public class MyApp extends Application {
    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public void setLevel(Double level) {
        this.level = level;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getLevel() {
        return level;
    }

    private Double x;
    private Double y;
    private Double level;



}
