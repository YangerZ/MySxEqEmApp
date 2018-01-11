package com.example.zhoajie.sxeqem;

/**
 * Created by Administrator on 2018/1/2.
 */

public class EarthArchiveNode {
    //表名
    public static final String TABLE="earchHappened";

    //表的各个域名
    public static final String KEY_id="ID";
    public static final String KEY_year="Year";
    public static final String KEY_date="Date";
    public static final String KEY_time="Time";
    public static final String KEY_position="Position";
    public static final String KEY_level="Level";
    //受灾人口
    public static final String KEY_death1="death1";
    public static final String KEY_death2="death2";
    public static final String KEY_death3="death3";
    //破坏无
    public static final String KEY_destroy1="destroy1";
    public static final String KEY_destroy2="destroy2";
    public static final String KEY_destroy3="destroy3";
    public static final String KEY_destroy4="destroy4";
    //经济损失
    public static final String KEY_lost="lost";

    //属性
    public int id;
    public String year;
    public String  date;
    public String  time;
    public String  position;
    public double level;
    public int death1;
    public int death2;
    public int death3;
    public int destroy1;
    public int destroy2;
    public int destroy3;
    public int destroy4;
    public double lost;

    public String GetTime(){

      return year+"年  "+date+""+ time;
    }


}
