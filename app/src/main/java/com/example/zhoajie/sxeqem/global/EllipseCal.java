package com.example.zhoajie.sxeqem.global;

import android.app.Application;
import android.icu.util.IslamicCalendar;
import android.util.Xml;

import com.example.zhoajie.sxeqem.Ellipse;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */

public class EllipseCal {
    /*
    *地震烈度 seismic intensity
    * 震级对应到最高烈度 VI ->6 对应成阿拉伯数字
     */
    public static   String  getSI(double level)
    {
        String si="0";
        if(level >= 7.8){

           si="11";
        }
        if(  level >=7.5 &&level<=7.7 ){

                si="10";
        }
        if(  level >=6.8 &&level<=7.4 ){

                si="9";
        }
        if(  level >=6.0 &&level<=6.7 ){

                si="8";
        }
        if(  level >=5.2 &&level<=5.9 ){

                si="7";
        }
        if(  level <5.2 ){

            si="7";
        }
        return si;
    }
    /*
    *计算轴长
    * 计算椭圆长轴 短轴的方法
    * 依据Mx-y的方式计算
     */
    public static double CalculateAxis(double x, double y, double M){
        double r=0;
        double temp=M*x-y;
        r=   Math.pow(Math.E,temp)*1000;
        return r;
    }
    /*
    * 求椭圆的 长短半轴 是最大烈度的
    * 求受灾面积是VI级的呦！
     */
    public static Ellipse[] ResolveXML(List<MetaIS> metalist,double level){

        Ellipse[] ells=new Ellipse[2];
        if(metalist==null){

            return null;
        }
        for (MetaIS meta : metalist) {
            String ld= meta.getMe();
            String zj= getSI(level);
            if(ld.equals(zj)){

                double maxAxis;
                double minAxis;
                 //此处开始按对应关系Mx-y的方式
                //极震区面积
                double hx1=Double.parseDouble( meta.getHa().split(",")[0]);
                double hy1=Double.parseDouble( meta.getHa().split(",")[1]);
                double hx2=Double.parseDouble( meta.getHb().split(",")[0]);
                double hy2=Double.parseDouble( meta.getHb().split(",")[1]);
                maxAxis=  CalculateAxis(hx1,hy1,level);
                minAxis=  CalculateAxis(hx2,hy2,level);
                ells[0]=new Ellipse();
                ells[0].setMax(maxAxis);
                ells[0].setMin(minAxis);
                //最大受灾面积
                double lx1=Double.parseDouble( meta.getLa().split(",")[0]);
                double ly1=Double.parseDouble( meta.getLa().split(",")[1]);
                double lx2=Double.parseDouble( meta.getLb().split(",")[0]);
                double ly2=Double.parseDouble( meta.getLb().split(",")[1]);

                 maxAxis=  CalculateAxis(hx1,hy1,level);
                 minAxis=  CalculateAxis(hx2,hy2,level);
                ells[1]=new Ellipse();
                ells[1].setMax(maxAxis);
                ells[1].setMin(minAxis);


                break;
            }

            //Log.i(TAG, person.toString());
        }

        return  ells;
    }

}


