package com.example.zhoajie.sxeqem.global;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/29.
 */


 public class JsonParser {
    public static List<String> parse(String jsonString){
            List<String> results = new ArrayList<>();

            try{
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("persons");
                int length = jsonArray.length();
                for(int i = 0; i < length; i++){
                    JSONObject personObject = jsonArray.getJSONObject(i);
                    String id = personObject.getString("id");
                    String name = personObject.getString("name");
                    int age = personObject.getInt("age");
                    results.add("12");
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

            return results;
        }

    private String resolveJson(String p_json)
    {
        StringBuilder cur_sb=new StringBuilder();
        if(TextUtils.isEmpty(p_json))
        {
            return "";
        }
        try
        {
            JSONObject original = new JSONObject(p_json);
            JSONArray infArray = original.getJSONArray("results");
            //
            JSONObject inf_Array = infArray.getJSONObject(0);
            JSONArray tq_infArray=inf_Array.getJSONArray("daily");
            JSONObject _datajson=tq_infArray.getJSONObject(0);
            //天气内容
            String baitian=_datajson.getString("text_day");
            String yejian=_datajson.getString("text_night");
            String temper_hight=_datajson.getString("high");
            String temper_low=_datajson.getString("low");

            String wind_direction=_datajson.getString("wind_direction");
            String wind_speed=_datajson.getString("wind_speed");
            String wind_scale=_datajson.getString("wind_scale");
            cur_sb.append( "实时天气："+"\n"+"白天"+baitian+",夜间"+yejian+ ","+"最高温度"+temper_hight+"°C，最低温度："+temper_low+"°C"+","+wind_direction+"风"+wind_scale+ "级，风速 "+wind_speed+"m/s"+"\n");
            return  cur_sb.toString();
        }
        catch(Exception ex)
        {
            return  "";
        }

    }
 }

