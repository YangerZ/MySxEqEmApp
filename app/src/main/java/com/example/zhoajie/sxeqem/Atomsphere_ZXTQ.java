package com.example.zhoajie.sxeqem;

import android.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SignatureException;
import java.util.Date;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/9/7.
 *提供天气查询的接口
 * from zhixin 天气
 */

public class Atomsphere_ZXTQ {
    private String TIANQI_DAILY_WEATHER_URL = "https://api.seniverse.com/v3/weather/daily.json";

    private String TIANQI_API_SECRET_KEY = "j1y6e5rb9clz9yjf"; //key

    private String TIANQI_API_USER_ID = "UA32DA6E1A"; //userid

    /**
     * Generate HmacSHA1 signature with given data string and key
     * @param data
     * @param key
     * @return
     * @throws SignatureException
     */
    private String generateSignature(String data, String key) throws SignatureException {
        String result;
        try {
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));
            result =android.util.Base64.encodeToString(rawHmac,1000);
        }
        catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    /**
     * Generate the URL to get diary weather
     * @param location
     * @param language
     * @param unit
     * @param start
     * @param days
     * @return
     */
    public String generateGetDiaryWeatherURL(
            String location,
            String language,
            String unit,
            String start,
            String days,
            double x,
            double y
    )
    {
        try
        {
            //此处调用的参数  userid没有使用直接使用的是key来调用
            String timestamp = String.valueOf(new Date().getTime());
            String params = "ts=" + timestamp + "&ttl=30";//&uid=" + TIANQI_API_USER_ID;
            String signature ="j1y6e5rb9clz9yjf";// URLEncoder.encode(generateSignature(params, TIANQI_API_SECRET_KEY), "UTF-8");
            return TIANQI_DAILY_WEATHER_URL + "?" + params + "&key=" + signature + "&location=" +x+":"+y + "&language=" + language + "&unit=" + unit + "&start=" + start + "&days=" + days;

        }
        catch(Exception ex){

            return "error:"+ex.toString();
        }
    }


}
