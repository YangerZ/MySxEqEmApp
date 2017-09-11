package com.example.zhoajie.sxeqem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/9/5.
 */

public class SMS_JZYJ implements Parcelable {

    private String _date;
    private String _palce;
    private String _deep;
    private String _bw;
    private String _dj;

    public void set_Date(String date){
       this._date=date;
    }
    public void set_Place(String palce){
        this._palce=palce;
    }
    public void  set_Deep(String deep){
        this._deep=deep;
    }
    public void set_BW(String date){
        this._bw=date;
    }
    public void set_DJ(String dj){
        this._dj=dj;
    }


    public String get_Date(){
        return   this._date ;
    }
    public String get_Place(){
        return this._palce ;
    }
    public String get_Deep(){
        return  this._deep;
    }
    public String get_BW(){
        return this._bw;
    }
    public String get_DJ(){
        return  this._dj;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_date);
        dest.writeString(_palce);
        dest.writeString(_deep);
        dest.writeString(_bw);
        dest.writeString(_dj);

    }


    public static final Parcelable.Creator<SMS_JZYJ> CREATOR = new Creator<SMS_JZYJ>()
    {
        @Override
        public SMS_JZYJ[] newArray(int size)
        {
            return new SMS_JZYJ[size];
        }

        @Override
        public SMS_JZYJ createFromParcel(Parcel in)
        {
            SMS_JZYJ sms_jzyj_=new SMS_JZYJ();
            sms_jzyj_._date=in.readString();
            sms_jzyj_._palce=in.readString();
            sms_jzyj_._deep=in.readString();
            sms_jzyj_._bw=in.readString();
            sms_jzyj_._dj=in.readString();
            return sms_jzyj_;
        }
    };


}
