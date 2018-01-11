package com.example.zhoajie.sxeqem;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhoajie.sxeqem.global.MessageEvent;
import com.example.zhoajie.sxeqem.global.MyApp;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.OnGetPoiResultListener;
import com.tianditu.android.maps.PoiInfo;
import com.tianditu.android.maps.PoiSearch;
import com.tianditu.android.maps.TErrorCode;
import com.tianditu.android.maps.TGeoAddress;
import com.tianditu.android.maps.TGeoDecode;
import com.tianditu.android.maps.TMapLayerManager;
import com.tianditu.android.maps.overlayutil.PoisOverlay;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends  android.support.v4.app.Fragment  implements View.OnClickListener,TGeoDecode.OnGeoResultListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //用于控制fragment所在的activity上下文对象
    private Context mContext;
    //存储短信数据类
    private SMS_JZYJ sms_jzyj;
    private AlertDialog.Builder alb;
    //显示地理信息textview
    private TextView geoMsg;
    //显示气象信息textview
    private TextView atomMsg;
    private LinearLayout layout_bottom;
    //textview controller
    private ExpandableTextView expTv;

    private OnFragmentInteractionListener mListener;
    //浮动按钮
    private FloatingActionButton fab_btn;
    //搜索按钮
    private Button queryBtn;
    //搜索输入框 searchtextview
    private android.support.v7.widget.SearchView searchView;
    //切换地图类型按钮
    private Switch tb_map;
    //地图对象
    private MapUtil mMapUtil;
    private MapView mapView;
    private MapController mMapCtl;

    //短信
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    private SmsObserver smsObserver;//37.49 36.3度，东经112.42

    private static String msm_="中国地震台网正式测定：8月23日11时38分在山西太原市清徐县(北纬36.3,东经111.7)发生6.1级地震,震源深度5公里。距清徐县15公里,距太谷县14公里.山西省地震局";
    private String curNumber="";
    //存储查询到的地理位置和气象信息字符串
    private String requestAtomsphereUrl="";//存储天气信息结果字符串
    private String curAtomresult="";
    private String curGeoinforesult="";

    //搜索功能
    // 搜索条件
    private String mKeyword;
    private int mSearchType = 0;
    // 搜索类
    private PoiSearch mPoiSearch;
    // 搜索结果
    private ArrayList<PoiInfo> mPoiList;
    private ArrayList<PoiSearch.ProvinceInfo> mCityList;
    // 覆盖物
    private PoisOverlay mPoisOverlay;

    //自义定图层功能
    private TMapLayerManager mapLayer_manage;
    private ImageButton imgBtn;
    private String define_service_url;

    //全局 用于记录第一个tab页中发生地震的点位
    private MyApp appState;
    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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
        //设置全局变量用于存储读取的短信或是从界面UI输入的地震信息
         appState =  (MyApp) getActivity().getApplicationContext();
       //String temp= appState.getLevel();
        //读取短信内容
        //若是自己手机测试 使用 myphone字符串资源
        //若是接收地震局短信号码使用 public字符串资源
        sms_jzyj =new SMS_JZYJ();
        curNumber=this.mContext.getResources().getString(R.string.myphone_sms_number);
        //自定义图层服务地址
        define_service_url=this.mContext.getResources().getString(R.string.webserverurl);
        //获取内容填充到短信sms的数据类中
        getSmsFromPhone(curNumber);
        //监听短信

       getActivity().getContentResolver().registerContentObserver(
                Uri.parse("content://sms"), true, new SmsObserver(mContext,new Handler()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_map, container, false);
        alb=new AlertDialog.Builder(getActivity());
        //弹出信息内容textview初始化
        // sample code snippet to set the text content on the ExpandableTextView
          expTv = (ExpandableTextView) v.findViewById(R.id.expand_text_view);
        layout_bottom=(LinearLayout)v.findViewById(R.id.bottomInfo_layout);
        geoMsg=(TextView) v.findViewById(R.id.geo_info);
        atomMsg=(TextView) v.findViewById(R.id.atom_info);
        layout_bottom.setVisibility(View.INVISIBLE);
        layout_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_bottom.setVisibility(View.INVISIBLE);
            }
        });
        //初始右下角按钮
        initFloatButton(v);
        //获取搜索输入框
        searchView=(android.support.v7.widget.SearchView)v.findViewById(R.id.sv_query);
        //这就是那个搜索框设置按钮或文本focus 之后立即显示 不是默认的点完icon图标才能用
        searchView.setIconifiedByDefault(false);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
            // 输入法如果是显示状态，那么就隐藏输入法
        }
        searchView.clearFocus(); // 不获取焦点
        //初始化搜索按钮
        queryBtn=(Button)v.findViewById(R.id.btn_queryPOI);
        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //执行查询功能
                String str_key=searchView.getQuery().toString();
                if(TextUtils.isEmpty(str_key))
                {
                    if (mPoisOverlay != null) {
                        mapView.removeOverlay(mPoisOverlay);
                        mPoisOverlay = null;
                    }
                }
                else
                {

                    searchKey(str_key,1);
                }

            }
        });
        //添加自定义图层按钮
        imgBtn=(ImageButton)v.findViewById(R.id.addlayer_imgBtn);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                showMapLayerList();
            }
        });

        //地图初始内容
        initMapParam(v);
        //设置右上角地图选择按钮
        initMapTypeChange(v);
        //默认位置
        updatePoint();
        return  v;
    }
    public void updatePoint(){
        double x=39.90923;
        double y= 116.397428;
        double level=0.0;
        if(!TextUtils.isEmpty(sms_jzyj.get_BW()))
        {
            x= Double.parseDouble(sms_jzyj.get_BW());
        }
        if(!TextUtils.isEmpty(sms_jzyj.get_DJ()))
        {
            y= Double.parseDouble(sms_jzyj.get_DJ());
        }
        if(!TextUtils.isEmpty(sms_jzyj.get_Level()))
        {
            level= Double.parseDouble(sms_jzyj.get_Level());
        }
        //
        appState.setX(x);
        appState.setY(y);
        appState.setLevel(level);
        //geocoding or environment atomsphere
        get_GeoInfo(mContext,x,y);
        get_Atomsphere(x,y);
        //定位到坐标点
        SetMapCenterByPoint(x,y);

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

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGeoDecodeResult(TGeoAddress tGeoAddress, int i) {

        if(i != 0)
        {
            Toast.makeText(mContext, "获取地址失败！", Toast.LENGTH_LONG).show();
            return;
        }
        String str = "地理信息:\n";


        str += "地址:" + tGeoAddress.getAddress() + "\n";
        str += "位置:" + tGeoAddress.getFullName() + "\n";
        str += "距离POI:" + tGeoAddress.getPoiName()+tGeoAddress.getPoiDistance() +"米"+"\n";
        str += "距离最近的公路:" + tGeoAddress.getRoadName()  +tGeoAddress.getRoadDistance()+"米" +"\n";


        curGeoinforesult=str;//str += "实时天气:" + curAtomresult+ "\n";
        expTv.setText(curGeoinforesult+expTv.getText());
        //Toast.makeText(mContext,str, Toast.LENGTH_LONG).show();

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
     * 初始右下角按钮及点击事件
     * 输入的当前UI的界面View对象
     * @param curV
     */
    private void initFloatButton(View curV)
    {
        fab_btn=(FloatingActionButton) curV.findViewById(R.id.btn_add_locate);
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ct=(Context) mListener;
                initDialog_PositonInput();
            }
        });

    }

    /**
     * 初始化弹出对话框
     * 点击添加按钮事件和取消事件
     */
    private void initDialog_PositonInput()
    {

        LayoutInflater layoutInflater =LayoutInflater.from(getActivity());
        View cx = layoutInflater.inflate(R.layout.posi_dialog, null);
        final      TextView tv1=(TextView) cx.findViewById(R.id.input_x);
        final     TextView tv2=(TextView) cx.findViewById(R.id.input_y);
        final TextView tv3=(TextView)cx.findViewById(R.id.input_level);
        alb.setTitle("请输入坐标点");
        alb.setView(cx);
        final AlertDialog dialog= alb.create();
        //按钮事件
        Button btnOk=(Button) cx.findViewById(R.id.btn_add_xy);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入点的数据坐标 没有输入坐标点时按默认的xy来读取的

                double x=39.90923;
                double y= 116.397428;
                double level=6.2;
                //清除文本内容
                expTv.setText("");
                dialog.cancel();
                if(!TextUtils.isEmpty(tv1.getText().toString()))
                {
                    x= Double.parseDouble(tv1.getText().toString());
                }
                if(!TextUtils.isEmpty(tv2.getText().toString()))
                {
                    y= Double.parseDouble(tv2.getText().toString());
                }
                if(!TextUtils.isEmpty(tv3.getText().toString()))
                {

                    level= Double.parseDouble(tv3.getText().toString());
                }
                //
                appState.setX(x);
                appState.setY(y);
                appState.setLevel(level);
                //添加新的地震位置
                //显示当前位置的天气情况
                curAtomresult="";
                get_GeoInfo(mContext,x,y);
                get_Atomsphere(x,y);
                SetMapCenterByPoint(x,y);
                //注册事件 发送到pgfragment
                //

            }
        });
        Button btnCacel=(Button)cx.findViewById(R.id.btn_dial_cancel);
        btnCacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();


    }

    /**
     * 初始化地图组件
     * @param v 当前fragmentView
     */
    private void initMapParam(View v)
    {
        mapView =(MapView) v.findViewById(R.id.main_mapview);
        mMapUtil=new MapUtil(mapView,null);
        //设置启用内置的缩放控件
        mapView.setBuiltInZoomControls(false);
        //得到mMapView的控制权,可以用它控制和驱动平移和缩放
        MapController mMapController = mapView.getController();
        mMapCtl=mMapController;

        // 设置地图服务端数据源网址，
        mapView.setCustomTileService(define_service_url);

        // 图层管理类
        mapLayer_manage = new TMapLayerManager(mapView);

        //设置地图zoom级别
        mMapController.setZoom(10);
        mapView.setSatellite(false);
        //地名的隐藏功能只有在卫星地图模式下才起作用：
        mapView.setPlaceName(true);  //隐藏地名
        //mapView.setMapType(MapView.TMapType.MAP_TYPE_VEC);
        //天地图logo的显示位置可以通过接口在地图的4个角进行调整，方便开发者设计UI布局：
        mapView.setLogoPos(MapView.LOGO_RIGHT_TOP); //logo显示在右上角
        mapView.isWebMercatorCRS();//说是可以清晰地图 我试试吧 @jiezhao

    }

    /**
     * 初始化地图底图切换按钮
     *
     */
    private void initMapTypeChange(View v)
    {
        tb_map=(Switch) v.findViewById(R.id.switch_type);
        tb_map.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()){
                    case R.id.switch_type:
                        if(!isChecked){
                            tb_map.setText("卫星");
                            mapView.setMapType(MapView.TMapType.MAP_TYPE_VEC);
                        }else {
                            tb_map.setText("地图");
                            mapView.setMapType(MapView.TMapType.MAP_TYPE_IMG);
                        }
                        break;

                    default:
                        break;
                }
            }
        });

    }

    /**
     * 根据经纬度参数 添加坐标位置 并定位到中心点
     * @param x 纬度
     * @param y 经度
     */
    private void  SetMapCenterByPoint(double x,double y)
    {
        Drawable drawable= ResourcesCompat.getDrawable(getResources(),R.drawable.ic_my_location_red_700_24dp,null);
        mMapUtil.locateByPosition(x,y,drawable);
        GeoPoint point = new GeoPoint((int) (x * 1E6), (int) (y * 1E6));
        //设置地图中心点
        mMapCtl.setCenter(point);
    }
    //获取Geocoding的方法
    /**
     * 通过坐标点geocoding地理名称 显示天气信息
     * 返回函数为onGeoDecodeResult中的结果
     */
    private void get_GeoInfo(Context p_context,double p_x,double p_y)
    {
        //
        TGeoDecode decode = new TGeoDecode(this);
        GeoPoint  geoPoint= new GeoPoint((int)(p_x*1000000), (int)(p_y*1000000));
        decode.search(geoPoint);
    }

    //天气信息获取解析处理字符串过程及方法
    /**
     * 通过坐标获取天气实时信息
     * 请求数据的结果在Handler函数中
     */
    private void get_Atomsphere(double x,double y)
    {
        String res_="";
        Atomsphere_ZXTQ atom=new Atomsphere_ZXTQ();

        requestAtomsphereUrl= atom.generateGetDiaryWeatherURL( "","zh-Hans",
                "c",
                "1",
                "1",
                x,
                y
        );
        Thread t=new Thread()
        {
            public void run()
            {
                //获取天气信息
               requestGet(requestAtomsphereUrl);

            }
        };
        t.start();


    }
    /**
     * 解析返回的天气信息json 内容
     */
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
    /**
     * 接收天气信息message并解析成对应的字符串显示
     * 显示结果显示在UI上
     */
    Handler request_handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            // 处理消息时需要知道是成功的消息还是失败的消息
            switch (msg.what) {
                case 1:
                    String tempstr  =msg.obj.toString();
                    curAtomresult=   resolveJson(tempstr);
                    //Toast.makeText(mContext,curAtomresult, Toast.LENGTH_LONG).show();
                     expTv.setText(curAtomresult+expTv.getText());
                    //atomMsg.setText(curAtomresult);
                    //layout_bottom.setVisibility(View.VISIBLE);
                    break;
                case 0:
                    Toast.makeText(mContext, "天气获取失败",Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }

        }
    };
    /**
     * HttpClient 请求 当前天气 api资源数据
     * 返回结果数据
     */
    public void requestGet(String requestUrl) {
        HttpURLConnection urlConn=null;
        try {

            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            if (urlConn.getResponseCode() == 200) {
                // 获取服务器相应头中的流，流中的数据就是客户端请求的数据
                InputStream is = urlConn.getInputStream();
                Message msg = new Message();
                // 消息对象可以携带数据
                msg.obj = streamToString(is);
                msg.what = 1;
                request_handler.sendMessage(msg);
            } else {
                // Toast.makeText(Download_picActivity.this, "请求失败",
                // 0).show();
                Message msg = request_handler.obtainMessage();
                msg.what = 0;
                request_handler.sendMessage(msg);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            urlConn.disconnect();
        }
    }
    /**
     * 解析文本内容
     */
    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {

            return null;
        }
    }

    //短信获取解析显示字符串等方法
    /**
     * 处理短信内容类和方法
     */
    public class SmsObserver extends ContentObserver {

        public SmsObserver(Context context,android.os.Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {

            //每当有新短信到来时，使用我们获取短消息的方法
            getSmsFromPhone(curNumber);
            updatePoint();
            super.onChange(selfChange);
        }
    }
    public android.os.Handler   smsHandler = new android.os.Handler() {
        //这里可以进行回调的操作
        //TODO


    };
    /**
     * 获得当前手机的短信内容 手机号码可以通过xml资源配置
     * @param phoneNumber
     */
    public void getSmsFromPhone(String phoneNumber) {
        ContentResolver cr =mContext.getContentResolver();
        String[] projection = new String[] { "body" };//"_id", "address", "person",, "date", "type
        //号码可以用固定的 地震专门的短信地址
        String where = " address = '"+phoneNumber+"' ";
        //        String where = " address = '8613132525116' "+"AND date >  "
//                + (System.currentTimeMillis() - 10 * 60 * 1000);
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur)
            return;
        if (cur.moveToNext()) {
            String body = cur.getString(cur.getColumnIndex("body"));
            //解析当前短信内容 包括 时间 地点 震源深度或公里数 名称
            sms_jzyj=ResolveSms(body);

        }
    }
    /**
     * 解析当前短信内容
     */
    public SMS_JZYJ ResolveSms(String Sms_str)
    {
        SMS_JZYJ sms_jzyj=new SMS_JZYJ();
        if(TextUtils.isEmpty(Sms_str)){

            Sms_str= msm_;
        }
        //测试目前直接赋值
        Sms_str= msm_;
        if(TextUtils.isEmpty(Sms_str))
        {
            return null;
        }
        //
        int sms1=Sms_str.indexOf("：")+1;
        int sms2=Sms_str.indexOf("分");
        //时间
        String _date=Sms_str.substring(sms1,sms2);
        //
        int sms3=Sms_str.indexOf("(")+1;
        //
        int sms4=Sms_str.indexOf(")");
        //地点
        String _place=Sms_str.substring(sms2+1,sms3-1);
        //坐标
        String _zb=Sms_str.substring(sms3,sms4);
        String[] _xy=_zb.split(",");
        String latstr=_xy[0].replace("北纬","");
        String lonstr=_xy[1].replace("东经","");


        //震源深度
        int sms5=Sms_str.indexOf("深度");
        int sms6=Sms_str.indexOf("公里")+2;
        String _deep=Sms_str.substring(sms5,sms6);

        //震级
        int sms7=Sms_str.indexOf("级地震");
        int sms8=Sms_str.indexOf("发生");
        String _level=Sms_str.substring(sms8+2,sms7);
        //


        sms_jzyj.set_BW(latstr);
        sms_jzyj.set_DJ(lonstr);
        sms_jzyj.set_Date(_date);
        sms_jzyj.set_Place(_place);
        sms_jzyj.set_Deep(_deep);
        sms_jzyj.set_Level(_level);
        return  sms_jzyj;

    }

    /**
     * 搜索POI关键字查询
     * 返回在GetResult方法中
     * 目前采用了当前范围搜索POI的方式
     * 按类型传入参数 0 普通搜索 1 当前范围搜索 2 周边搜索
     */
    private void searchKey(String key, int type) {
        mKeyword = key;
        if (mPoisOverlay != null) {
          mapView.removeOverlay(mPoisOverlay);
            mPoisOverlay = null;
        }

        switch (type) {
//            case 0: // 普通
//                mPoiSearch = new PoiSearch(mContext, new OnGetPoiResultListener() {
//                    @Override
//                    public void OnGetPoiResult(ArrayList<PoiInfo> arrayList, ArrayList<PoiSearch.ProvinceInfo> arrayList1, String s, int i) {
//                        GetResult(arrayList, arrayList1,  s, i);
//                    }
//                }, mapView);
//                mPoiSearch.search(key, null, null);
//                break;
           case 1: // 视图范围内
                GeoPoint p1 = mapView.getProjection().fromPixels(0, 0);
                GeoPoint p2 = mapView.getProjection().fromPixels(mapView.getWidth(),
                        mapView.getHeight());
                mPoiSearch = new PoiSearch(mContext, new OnGetPoiResultListener() {
                    @Override
                    public void OnGetPoiResult(ArrayList<PoiInfo> arrayList, ArrayList<PoiSearch.ProvinceInfo> arrayList1, String s, int i) {
                        GetResult(arrayList, arrayList1,  s, i);
                    }
                },mapView);
                mPoiSearch.search(key, p1, p2);
                break;
//            case 2: // 周边
//                mPoiSearch = new PoiSearch(this, this, mapView);
//                GeoPoint center = mapView.getMapCenter();
//                mPoiSearch.search(key, center, 20000);
//                break;
//            case 3: // 行政区
//                mPoiSearch = new PoiSearch(this, this, mapView);
//                mPoiSearch.search("156110000", key);
//                break;
        }

    }


    public void GetResult(ArrayList<PoiInfo> poiInfo, ArrayList<PoiSearch.ProvinceInfo> cityInfo,
                               String keyword, int error) {
        mPoiList = poiInfo;
        mCityList = cityInfo;

        int poiSize = poiInfo != null ? poiInfo.size() : 0;
        int citySize = cityInfo != null ? cityInfo.size() : 0;

        if (error != TErrorCode.OK) {
            //mTextTips.setText("搜索关键字失败 key = " + mKeyword + ", type = " + mSearchType
               //     + ", error = " + error);
            return;
        }
        if (poiSize == 0) {
            //mTextTips.setText("没有找到结果 key = " + mKeyword + ", type = " + mSearchType);
            Toast.makeText(mContext,"没有找到对应的结果",Toast.LENGTH_LONG).show();
            return;
        }


        // 添加覆盖物
        Drawable marker = getResources().getDrawable(R.drawable.ic_place_deep_orange_600_24dp);
        mPoisOverlay = new PoisOverlay(marker);
        mPoisOverlay.setData(poiInfo);
        mPoisOverlay.setDrawFocusedItem(true);
        mPoisOverlay.showFocusAlways(true);
        mPoisOverlay.populate();
        mapView.addOverlay(mPoisOverlay);

        GeoPoint point = poiInfo.get(0).mPoint;

        mapView.getController().animateTo(point);
    }

    /**
     * 显示图层列表
     */
    void showMapLayerList() {
        int mapType = mapView.getMapType();
        String title = mapLayer_manage.getMapName(mapType);
        final String[] allLayers = mapLayer_manage.getLayers(mapType);
        final String[] showLayers = mapLayer_manage.getLayersShow();
        final int size = allLayers.length;

        // 开关状态
        final boolean[] checkedItems = new boolean[size];
        List<String> list = Arrays.asList(showLayers);
        for (int i = 0; i < size; ++i) {
            if (list.contains(allLayers[i]))
                checkedItems[i] = true;
            else
                checkedItems[i] = false;
        }

        DialogInterface.OnMultiChoiceClickListener choiceListener = new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int whichButton,
                                boolean isChecked) {
                // 设置开关状态
                checkedItems[whichButton] = isChecked;
            }
        };

        DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // 设置可见图层
                List<String> list = Arrays.asList(showLayers);
                ArrayList<String> showList = new ArrayList<String>(list);
                for (int i = size - 1; i >= 0; --i) {
                    String item = allLayers[i];
                    if (checkedItems[i] && !showList.contains(item))
                        showList.add(item);
                    else if (!checkedItems[i] && showList.contains(item))
                        showList.remove(item);
                }
                String[] showLayers = showList.toArray(new String[showList
                        .size()]);
                mapLayer_manage.setLayersShow(showLayers);
                searchView.clearFocus();
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                searchView.clearFocus();
            }
        };

        // 创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMultiChoiceItems(allLayers, checkedItems, choiceListener);
        builder.setPositiveButton("确定", okListener);
        builder.setNegativeButton("取消", cancelListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 更新提示信息
     */
    void updateTips() {
        String[] allLayers = mapLayer_manage.getLayers(mapView.getMapType());
        String tips = "包含图层：";
        for (String layer : allLayers)
            tips += layer + " ";

        String[] showLayers = mapLayer_manage.getLayersShow();
        tips += "\n可见图层：";
        for (String layer : showLayers)
            tips += layer + " ";

       //Toast.makeText(mContext,tips,Toast.LENGTH_LONG);
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }


}
