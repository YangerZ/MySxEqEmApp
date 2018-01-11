package com.example.zhoajie.sxeqem.global;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    public static HashMap resolveJson(String gpjson)
    {
        HashMap resultItem=new HashMap();

        if(TextUtils.isEmpty(gpjson))
        {
            return null;
        }
        try
        {
            JSONObject resultJson = new JSONObject(gpjson);
            JSONArray resutsArray = resultJson.getJSONArray("results");
            //results 中可能有多个类型的结果

             for (Integer i=0;i<resutsArray.length();i++)
             {
                 //result中对应显示UI业务的结果
                 GpResult gpResult=new GpResult();
                 //每个results 里包括 很多  paramName datetype value
                 JSONObject cont_joson = resutsArray.getJSONObject(i);
                 String paramName=cont_joson.getString("paramName");

                 //按照paramName的名字找对应的rest结果

                     //values 里包括 fields and features
                     JSONObject valuesJson=cont_joson.getJSONObject("value");

                     //解析fields
                     JSONArray fieldsArray=valuesJson.getJSONArray("fields");
                     for (Integer j=0;j<fieldsArray.length();j++)
                     {
                         JSONObject itemObj=fieldsArray.getJSONObject(j);
                         String[] fields=new String[3];
                         fields[0]=itemObj.getString("name");
                         fields[1]=itemObj.getString("type");
                         fields[2]=itemObj.getString("alias");
                         gpResult.AddfieldItem(fields);

                     }
                     //解析features
                     JSONArray featuresArray=valuesJson.getJSONArray("features");
                     for (Integer k=0;k<featuresArray.length();k++)
                     {
                         Feature f=new  Feature();
                        JSONObject featJson=   featuresArray.getJSONObject(k);
                        JSONObject attriJson=   featJson.getJSONObject("attributes");
                        Iterator<String> iterator= attriJson.keys();
                         while (iterator.hasNext())
                         {
                             String fname=iterator.next().toString();
                             Object fvalue=attriJson.get(fname);
                             f.AddAttributeItem(fname,fvalue);

                         }
                         gpResult.AddfeatureItem(f);

                     }
                 /**
                  * 这个是用gp结果参数来和result里的结果项匹配
                  * 也就是在模拟结果的json结构
                  */
                 resultItem.put(paramName,gpResult);

             }




        }
        catch(Exception ex)
        {
            String temp=ex.toString();
            return null;
        }
        finally {
            return resultItem;
        }

    }
 }

