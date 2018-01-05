package com.example.zhoajie.sxeqem.global;

import android.content.res.AssetManager;
import android.content.res.ObbInfo;
import android.os.AsyncTask;
import android.os.Message;
import android.widget.TextView;

import com.example.zhoajie.sxeqem.Ellipse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/29.
 */

public class BufferPointTask extends AsyncTask<String, Integer, Map<String, Object>> {
    //NETWORK_GET表示发送GET请求
    public static final String NETWORK_GET = "GET";
    //NETWORK_POST_KEY_VALUE表示用POST发送键值对数据
    public static final String NETWORK_POST_KEY_VALUE = "POST";

    //设置传入的UI参数
    private Object UIContent;

    public Object getUIContent() {
        return UIContent;

    }

    public void SetUIContent(Object uicom) {
        if (uicom == null) {

            return;
        }
        this.UIContent = uicom;

    }

    //用于UI绑定的DataClass Model
    private Object DataAdapter;

    public Object getDataAdapter() {
        return UIContent;

    }

    public void SetDataAdapter(Object dataAdapter) {
        if (dataAdapter == null) {

            return;
        }
        this.DataAdapter = dataAdapter;

    }

    protected Map<String, Object> doInBackground(String... params) {
        Map<String, Object> result = new HashMap<>();
        URL url = null;//请求的URL地址
        HttpURLConnection conn = null;
        String requestHeader = null;//请求头
        byte[] requestBody = null;//请求体
        String responseHeader = null;//响应头
        byte[] responseBody = null;//响应体
        String action = params[0];//http请求的操作类型
        if (action == "") {
            return null;

        }

//this is from our classmates sunqun perfect
        try {
            if (NETWORK_GET.equals(action)) {
                //通过String params 组合url地址请求的
                //发送GET请求 params[1] url
                String restString =params[1]; //"http://123.206.55.150:6080/arcgis/rest/services/SampleWorldCities/MapServer/0/query?where=1%3D1&text=&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=&returnGeometry=true&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=false&f=pjson";
                //拼接参数
                url = new URL(restString);
                //

                conn = (HttpURLConnection) url.openConnection();
                //HttpURLConnection默认就是用GET发送请求，所以下面的setRequestMethod可以省略
                conn.setRequestMethod("GET");
                //HttpURLConnection默认也支持从服务端读取结果流，所以下面的setDoInput也可以省略
                conn.setDoInput(true);
                //用setRequestProperty方法设置一个自定义的请求头:action，由于后端判断
                conn.setRequestProperty("action", NETWORK_GET);
                //禁用网络缓存
                conn.setUseCaches(false);
                //获取请求头
                requestHeader = getReqeustHeader(conn);
                //在对各种参数配置完成后，通过调用connect方法建立TCP连接，但是并未真正获取数据
                //conn.connect()方法不必显式调用，当调用conn.getInputStream()方法时内部也会自动调用connect方法
                conn.connect();
                //调用getInputStream方法后，服务端才会收到请求，并阻塞式地接收服务端返回的数据
                InputStream is = conn.getInputStream();
                //将InputStream转换成byte数组,getBytesByInputStream会关闭输入流
                responseBody = getBytesByInputStream(is);
                //获取响应头
                responseHeader = getResponseHeader(conn);
            } else if (NETWORK_POST_KEY_VALUE.equals(action)) {
                //用POST发送键值对数据
                url = new URL("http://123.206.55.150:6080/arcgis/rest/services/SampleWorldCities/MapServer/0");
                conn = (HttpURLConnection) url.openConnection();
                //通过setRequestMethod将conn设置成POST方法
                conn.setRequestMethod("POST");
                //调用conn.setDoOutput()方法以显式开启请求体
                conn.setDoOutput(true);
                //用setRequestProperty方法设置一个自定义的请求头:action，由于后端判断
                conn.setRequestProperty("action", NETWORK_POST_KEY_VALUE);
                //获取请求头
                requestHeader = getReqeustHeader(conn);
                //获取conn的输出流
                OutputStream os = conn.getOutputStream();
                //获取两个键值对name=孙群和age=27的字节数组，将该字节数组作为请求体
                requestBody = new String("name=孙群&age=27").getBytes("UTF-8");
                //将请求体写入到conn的输出流中
                os.write(requestBody);
                //记得调用输出流的flush方法
                os.flush();
                //关闭输出流
                os.close();
                //当调用getInputStream方法时才真正将请求体数据上传至服务器
                InputStream is = conn.getInputStream();
                //获得响应体的字节数组
                responseBody = getBytesByInputStream(is);
                //获得响应头
                responseHeader = getResponseHeader(conn);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //最后将conn断开连接
            if (conn != null) {
                conn.disconnect();
            }
        }

        //返回结果
        result.put("responseBody", responseBody);
        return result;
    }
//UI 界面内容

    @Override
    protected void onPostExecute(Map<String, Object> result) {
        super.onPostExecute(result);

        // String url = (String)result.get("url");//请求的URL地址
        String action = (String) result.get("action");//http请求的操作类型
        //String requestHeader = (String) result.get("requestHeader");//请求头
        //byte[] requestBody = (byte[]) result.get("requestBody");//请求体
        String responseHeader = (String) result.get("responseHeader");//响应头
        byte[] responseBody = (byte[]) result.get("responseBody");//响应体
        String response = getStringByBytes(responseBody);
        //更新tvResponseBody，显示响应体

            TextView tv=(TextView) this.getUIContent();
            tv.setText(response);


    }

    //读取请求头
    private String getReqeustHeader(HttpURLConnection conn) {
        //https://github.com/square/okhttp/blob/master/okhttp-urlconnection/src/main/java/okhttp3/internal/huc/HttpURLConnectionImpl.java#L236
        Map<String, List<String>> requestHeaderMap = conn.getRequestProperties();
        Iterator<String> requestHeaderIterator = requestHeaderMap.keySet().iterator();
        StringBuilder sbRequestHeader = new StringBuilder();
        while (requestHeaderIterator.hasNext()) {
            String requestHeaderKey = requestHeaderIterator.next();
            String requestHeaderValue = conn.getRequestProperty(requestHeaderKey);
            sbRequestHeader.append(requestHeaderKey);
            sbRequestHeader.append(":");
            sbRequestHeader.append(requestHeaderValue);
            sbRequestHeader.append("\n");
        }
        return sbRequestHeader.toString();
    }

    //读取响应头
    private String getResponseHeader(HttpURLConnection conn) {
        Map<String, List<String>> responseHeaderMap = conn.getHeaderFields();
        int size = responseHeaderMap.size();
        StringBuilder sbResponseHeader = new StringBuilder();
        for (int i = 0; i < size; i++) {
            String responseHeaderKey = conn.getHeaderFieldKey(i);
            String responseHeaderValue = conn.getHeaderField(i);
            sbResponseHeader.append(responseHeaderKey);
            sbResponseHeader.append(":");
            sbResponseHeader.append(responseHeaderValue);
            sbResponseHeader.append("\n");
        }
        return sbResponseHeader.toString();
    }

    //根据字节数组构建UTF-8字符串
    private String getStringByBytes(byte[] bytes) {
        String str = "";
        try {
            str = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    //从InputStream中读取数据，转换成byte数组，最后关闭InputStream
    private byte[] getBytesByInputStream(InputStream is) {
        byte[] bytes = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        byte[] buffer = new byte[1024 * 8];
        int length = 0;
        try {
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    //根据文件名，从asserts目录中读取文件的字节数组
//读取xml 或json文件
    private byte[] getBytesFromAssets(String fileName) {
        byte[] bytes = null;
        AssetManager assetManager = null;//getAssets();
        InputStream is = null;
        try {
            is = assetManager.open(fileName);
            bytes = getBytesByInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    //将表示xml的字节数组进行解析
    private String parseXmlResultByBytes(byte[] bytes) {
        InputStream is = new ByteArrayInputStream(bytes);
        StringBuilder sb = new StringBuilder();

//        List<Person> persons = XmlParser.parse(is);
//        for (Person person : persons) {
//            sb.append(person.toString()).append("\n");
//        }
        return sb.toString();
    }

    //将表示json的字节数组进行解析
    private String parseJsonResultByBytes(byte[] bytes) {
        String jsonString = getStringByBytes(bytes);
        List<String> results = JsonParser.parse(jsonString);
        StringBuilder sb = new StringBuilder();
        for (String str : results) {
            sb.append(str.toString()).append("\n");
        }
        return sb.toString();
    }

}

