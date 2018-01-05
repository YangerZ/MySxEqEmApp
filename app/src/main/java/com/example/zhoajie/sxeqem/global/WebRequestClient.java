package com.example.zhoajie.sxeqem.global;

/**
 * Created by Administrator on 2018/1/4.
 */
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.entity.mime.Header;

public class WebRequestClient  {
    private static final String BASE_URL = "http://123.206.55.150:6080/arcgis/rest/services/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler ) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


}

