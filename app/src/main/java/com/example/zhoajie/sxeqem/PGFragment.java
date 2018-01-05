package com.example.zhoajie.sxeqem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhoajie.sxeqem.global.BufferPointTask;
import com.example.zhoajie.sxeqem.global.CenterDistanceTask;
import com.example.zhoajie.sxeqem.global.EllipseCal;
import com.example.zhoajie.sxeqem.global.MetaIS;
import com.example.zhoajie.sxeqem.global.MetaParserImp;
import com.example.zhoajie.sxeqem.global.MyApp;
import com.example.zhoajie.sxeqem.global.PopDensityTask;
import com.example.zhoajie.sxeqem.global.RestAsyncTask;
import com.example.zhoajie.sxeqem.global.WebRequestClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PGFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PGFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PGFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private OnFragmentInteractionListener mListener;

    /**
     * 记录全局页面的点位置
     */
    private MyApp appState;

    public PGFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PGFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PGFragment newInstance(String param1, String param2) {
        PGFragment fragment = new PGFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.mContext = getActivity();
        //计算椭圆参数及面积


    }
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_pg, container, false);

        tv1=(TextView) v.findViewById(R.id.cont1);
        tv2=(TextView) v.findViewById(R.id.cont2);
        tv3=(TextView) v.findViewById(R.id.cont3);
        tv4=(TextView) v.findViewById(R.id.cont4);
        //都得从椭圆开始计算
        CalculateEllipse();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    /**
     * 计算震级对应的 烈度 长短轴 椭圆面积 Gp调用等
     */
    public void CalculateEllipse() {

        MyApp appState = (MyApp) getActivity().getApplicationContext();
        double x = 0;//是经度
        double y = 0;//是纬度
        double level = 0;
         y= appState.getX();
         x = appState.getY();
        level = appState.getLevel();
        //检查坐标及震级是否传回
        if (x == 0 || y == 0 || level == 0) {

            return;
        }
        //请求参数已准备好
        List<MetaIS> metaISList = null;
        try {
            //开始解析坐标点内容 dz.xml是一个记录震级和烈度的参考表 计算长短轴的公式
            InputStream IsSm = getActivity().getAssets().open("dz.xml");
            MetaParserImp metaParserImp = new MetaParserImp();
            metaISList = metaParserImp.parse(IsSm);
            //两个数组 第一个是 极震区椭圆  第二个是受灾范围椭圆
            Ellipse[] ells = EllipseCal.ResolveXML(metaISList, level);
            //烈度
            String ld = EllipseCal.getSI(level);
            //长轴 短轴
            String a = String.valueOf(ells[0].a);
            String b = String.valueOf(ells[0].b);
            //集镇区面积
            String area = String.valueOf(ells[0].getArea());
            //灾区面积
            String areaZQ = String.valueOf(ells[1].getArea());
            //添加极震区坐标点Gp rest get http
            ProductCenterPoint(x,y,String.valueOf(level),ells[0].a,ells[0].b);
            //sqllite读取条件A<M<B中的地震案例
            GetArchiveByLevel(level);


        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    /**
     *  生成震中点要素 及属性内容 请求GP
     * 返回 受影响的结果
     * rest ->createpoint
     * http://123.206.55.150:6080/arcgis/rest/services/createpoint/GPServer/%E8%84%9A%E6%9C%AC/submitJob?x=112.768867&y=36.950138&level=0&a=400&b=50&env%3AoutSR=&env%3AprocessSR=&returnZ=false&returnM=false&f=pjson
     */
    public void ProductCenterPoint(double x,double y,String level,double a,double b) {
        String parturl="makeZhenzhong/GPServer/makeZhenzhong/execute?";
        String quest="x="+x+"&y="+y+"&level="+level+"&a="+a+"&b="+b+"&env%3AoutSR=&env%3AprocessSR=&returnZ=false&returnM=false&f=pjson";
        String purl=parturl+quest;
        WebRequestClient.get(purl, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)  {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    String tweetText = response.getString("results");
                    //开始下几个调用
                    //确认同步已经完成
                    //后边几个可以同时异步分析gp
                    GetMakeEllipseBuffer();




                }catch (JSONException je){
                    String error=je.toString();

                }
                finally {

                }

            }
            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

        //AsyncTask 是默认串行执行得
        //级别要小于handler
        //可以写成并行 但是android并不推荐那么做
        //restAsyncTask Ellipse 和 handler传入方法中可以么
        //输入参数  后台执行百分比  返回参
        //输入参数为action , url, 及其get 参数
        //结果数据在PostExe中可以查看 于生成极震点要素
        //"action:get ,url :gp 地址 ，params:x,y，level ,a,b"
//        RestAsyncTask createAsyncTask = new RestAsyncTask();
//
//        String insertPoint = "http://123.206.55.150:6080/arcgis/rest/services/makeZhenzhong/GPServer/makeZhenzhong/execute?";
//        String quest="x="+x+"&y="+y+"&level="+level+"&a="+a+"&b="+b+"&env%3AoutSR=&env%3AprocessSR=&returnZ=false&returnM=false&f=pjson";
//
//        createAsyncTask.execute("GET", insertPoint+quest);//url是拼接好的get gp 点请求

    }

    /**
     * 通过震中点去计算椭圆并求出缓冲区内县 乡镇 点数据  请求GP
     * 返回 受影响的结果
     * rest-> makeZhenzhong
     * http://123.206.55.150:6080/arcgis/rest/services/getXianZhenInJizhenqu/GPServer/makeTuoyuan/execute?env%3AoutSR=&env%3AprocessSR=&returnZ=false&returnM=false&f=pjson
     */
    public void GetMakeEllipseBuffer() {
        String bufferCityTown = "getXianZhenInJizhenqu/GPServer/makeTuoyuan/execute?";
       String questStr="env%3AoutSR=&env%3AprocessSR=&returnZ=false&returnM=false&f=pjson";
        String purl=bufferCityTown+questStr;
        WebRequestClient.get(purl, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)  {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    String tweetText = response.getString("results");
                    String backresult="";
                    tv1.setText(tweetText);
                    GetPopDensity();


                }catch (Exception ex){
                    String error=ex.toString();

                }
                finally {

                }

            }
            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

    /**
     * 通过椭圆计算所覆盖的人口密度 Gp服务
     * 返回所在极震区人口密度
     * rest->getPopInzhenzhong
     * http://123.206.55.150:6080/arcgis/rest/services/getPopInzhenzhong/GPServer/getPopInzhenzhong/submitJob?env%3AoutSR=&env%3AprocessSR=&returnZ=false&returnM=false&f=html
     */
    public void GetPopDensity(){

        String urlStr="getPopInzhenzhong/GPServer/getPopInzhenzhong/execute?env%3AoutSR=&env%3AprocessSR=&returnZ=false&returnM=false&f=pjson";
        WebRequestClient.get(urlStr, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)  {
                // If the response is JSONObject instead of expected JSONArray
                try {
                   String tweetText = response.getString("results");
                    String backresult="";
                    tv2.setText(tweetText);
                    GetGPzzDistance();
                }catch (Exception ex){

                    String error=ex.toString();
                }
                finally {

                }

            }
            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

       }

    /**
     * 用于获取震中距 历史地震相关结果
     * 用于获取50km里内的历史地震
     * 包括调用gp及页面UI内容的刷新 此项调用返回的结果是两项内容 包括 震中距和震中50km内的历史地震
     * rest->getZhenzhongju
     * http://123.206.55.150:6080/arcgis/rest/services/getZhenzhongju/GPServer/getZhenzhongju/submitJob?env%3AoutSR=&env%3AprocessSR=&returnZ=false&returnM=false&f=html
     */
    public void GetGPzzDistance() {
        //
        String urlStr="getZhenzhongju/GPServer/getZhenzhongju/execute?env%3AoutSR=&env%3AprocessSR=&returnZ=false&returnM=false&f=pjson";
        WebRequestClient.get(urlStr, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)  {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    String tweetText = response.getString("results");
                    String backresult="";
                    tv3.setText(tweetText);
                }catch (Exception ex){

                    String error=ex.toString();
                }
                finally {

                }

            }
            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });


    }



    /**
     * add 20180104  add web http client for async request
     * 用于请求rest 地址上的gp服务
     * url 传入的是 已经调用过gp服务之后  jobid中的 字符串
     * 参数为空
     * 返回值写到dataadapter这种数据绑定的MVVM?类里
     *
     */
    public void GetRESTfulData(String funcurl, final int qaction) throws JSONException {
        WebRequestClient.get(funcurl, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)  {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    String tweetText = response.getString("text");
                    switch (qaction)
                    {
                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
                            break;

                    }

                }catch (JSONException je){


                }
                finally {

                }

            }
            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    /**
     * 查询的sqlite 中震级符合条件的地震案例
     * @param level 地震级别 计算出来的
     */
    public void GetArchiveByLevel(double level)
    {
        ArchiveEarthquake arvchEarth=new ArchiveEarthquake(this.mContext);
        List<EarthArchiveNode> archiveEarthquakes=    arvchEarth.getNearArchivelBylevel(level);
        //绑定数据到UI List列表中地震案例
        if(archiveEarthquakes==null||archiveEarthquakes.isEmpty()){

            return;
        }
        String content ="";
        for (EarthArchiveNode node:archiveEarthquakes){


            content+=node.position+"/n";
            tv4.setText(content);
        }

    }






}
