package com.example.zhoajie.sxeqem;

/**
 * Created by Administrator on 2017/12/28.
 */

public class Ellipse {
    public double a;
    public  double b;
    public double area;
    //function=Mx-y 参数x y 是XML中si节点中的值
    //M就是对应的烈度

    public  double getArea()
    {
        return Math.PI*a*b;
    }
    public void setMax(double _a)
    {

        a=_a;
    }
    public void setMin(double _b){

        b=_b;
    }

    public String  label;
}
