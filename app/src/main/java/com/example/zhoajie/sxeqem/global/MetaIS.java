package com.example.zhoajie.sxeqem.global;

/**
 * Created by Administrator on 2017/12/28.
 */

public class MetaIS {

    private String Me;
    //最大震级 长轴短轴 系数 x,y
    //受灾震级VI级 长短轴系数 x,y
    private String Ha;
    //
    private String Hb;

    //
    private String La;
    //
    private String Lb;



    public String getMe() {
        return Me;
    }

    public void setMe(String me) {
        this.Me= me;
    }

    public String getHa() {
        return Ha;
    }

    public void setHa(String ha) {
        this.Ha= ha;
    }

    public String getHb() {
        return Hb;
    }

    public void setHb(String hb) {
        this.Hb = hb;
    }

    public String getLa() {
        return La;
    }

    public void setLa(String la) {
        this.La= la;
    }

    public String getLb() {
        return Lb;
    }

    public void setLb(String lb) {
        this.Lb = lb;
    }
}
