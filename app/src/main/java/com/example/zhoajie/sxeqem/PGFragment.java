package com.example.zhoajie.sxeqem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhoajie.sxeqem.global.BufferPointTask;
import com.example.zhoajie.sxeqem.global.CenterDistanceTask;
import com.example.zhoajie.sxeqem.global.EllipseCal;
import com.example.zhoajie.sxeqem.global.Feature;
import com.example.zhoajie.sxeqem.global.Field;
import com.example.zhoajie.sxeqem.global.GpResult;
import com.example.zhoajie.sxeqem.global.JsonParser;
import com.example.zhoajie.sxeqem.global.MessageEvent;
import com.example.zhoajie.sxeqem.global.MetaIS;
import com.example.zhoajie.sxeqem.global.MetaParserImp;
import com.example.zhoajie.sxeqem.global.MyApp;
import com.example.zhoajie.sxeqem.global.PopDensityTask;
import com.example.zhoajie.sxeqem.global.RestAsyncTask;
import com.example.zhoajie.sxeqem.global.WebRequestClient;
import com.example.zhoajie.sxeqem.global.YXFWResultItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

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



    //CardView上的详细按钮
    private ImageView imgV1;
    private ImageView imgV2;
    private ImageView imgV3;
    private ImageView imgV4;

    private  View pgview;
    private ProgressBar pbBar;
    //返回按钮
    private  ImageButton imgbackbtn;

    //Layout用于切换cardview 和详细结果内容
    private LinearLayout gplayout;
    private LinearLayout detaillayout;
    private TextView yxfw_textview;
    private TextView title_textview;

    //存放影响范围上的数据结果 adapter class
    private YXFWResultItem  yxfwresult;
    private GpResult zzdis;
    private GpResult historydz;
    //数据库返回的地震案例结果
    private List<EarthArchiveNode> archiveEarthquakes;
    private StringBuilder yxfwStr=new StringBuilder();
    private ListView listView=null;
    //Dictionary for label field en-zh
    private HashMap labelsmap=new HashMap();

    public PGFragment() {
        // Required empty public constructor
    }
    //
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

        yxfwresult=new YXFWResultItem();
        labelsmap.put("name","名称");
        labelsmap.put("DISTANCE","距离");
        labelsmap.put("location","位置");
        labelsmap.put("date_","日期");
        labelsmap.put("magnitude","级数");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        pgview=inflater.inflate(R.layout.fragment_pg, container, false);
        gplayout=(LinearLayout) pgview.findViewById(R.id.pglayout);
       // gplayout.setVisibility(View.GONE);
        detaillayout=(LinearLayout) pgview.findViewById(R.id.detaillayout);
        detaillayout.setVisibility(View.GONE);
        pbBar=(ProgressBar)pgview.findViewById(R.id.probar);

        yxfw_textview=(TextView)pgview.findViewById(R.id.yxfw_tv);
        title_textview=(TextView)pgview.findViewById(R.id.curLabel);
        imgV1=(ImageView)pgview.findViewById(R.id.img1);
        imgV1.setOnClickListener(listener);

        imgV2=(ImageView)pgview.findViewById(R.id.img2);
        imgV2.setOnClickListener(listener);

        imgV3=(ImageView)pgview.findViewById(R.id.img3);
        imgV3.setOnClickListener(listener);

        imgV4=(ImageView)pgview.findViewById(R.id.img4);
        imgV4.setOnClickListener(listener);

        imgbackbtn=(ImageButton)pgview.findViewById(R.id.imgback);
        imgbackbtn.setOnClickListener(listener);

        listView=(ListView)pgview.findViewById(R.id.GPList);
//        EventBus.getDefault().register(this);
        //都得从椭圆开始计算
        CalculateEllipse();
        return pgview;
    }

    //Event Bus接受消息回调



    //将数据加载到UI上显示  Listview开始adpter模式
    /**
     * 多个不同的listview adpter的数据 字段不同需要处理不同的内容
     */
    public View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            if(v.getId()==R.id.imgback)
            {
                detaillayout.setVisibility(View.GONE);
                gplayout.setVisibility(View.VISIBLE);

            }
            else{
                detaillayout.setVisibility(View.VISIBLE);
                gplayout.setVisibility(View.GONE);
                yxfw_textview.setVisibility(View.GONE);
                //初始化数据结果到textview  和listview上吧
                //跳转到detail
                if(v.getId()==R.id.img1)
                {
                    yxfw_textview.setVisibility(View.VISIBLE);

                    title_textview.setText("影响范围");
                    if(yxfwresult==null){

                        return;
                    }
                    //每次清空面板值
                    yxfwStr.delete(0,yxfwStr.toString().length());
                    yxfwStr.append("  极震区范围及影响"+"\n");
                    yxfwStr.append("    极震区烈度："+yxfwresult.tag_ld+"\n");
                    yxfwStr.append("    长轴："+yxfwresult.tag_a+"\n");
                    yxfwStr.append("    短轴："+yxfwresult.tag_b+"\n");
                    yxfwStr.append("    极震区面积："+yxfwresult.tag_area+"\n");
                    yxfwStr.append("    受灾总面积 ："+yxfwresult.tag_disaster+"\n");
                    List<Feature> fs=yxfwresult.bufferXian.GetFeatureItems();


                    yxfwStr.append(" \n 极震区包含的县 \n ");
                    if(fs==null||fs.isEmpty()){

                        yxfwStr.append(" 未查询到相关信息 \n");
                    }
                    else
                    {
                        for(Feature fe:fs)
                        {
                            HashMap attributesItems=fe.GetAttributeItems();
                            String name=attributesItems.get("NAME2004").toString();
                            yxfwStr.append("    名称："+name+"\n");
                        }

                    }


                    yxfwStr.append("\n 极震区包含的乡镇 \n");
                    List<Feature> fs1= yxfwresult.bufferZhen.GetFeatureItems();
                    if(fs1==null||fs1.isEmpty()){

                        yxfwStr.append(" 未查询到相关信息 \n");
                    }
                    else{
                        for(Feature fe:fs1)
                        {
                            HashMap attributesItems=fe.GetAttributeItems();
                            String name=attributesItems.get("name").toString();
                            String code=attributesItems.get("postcode").toString();
                            yxfwStr.append("    乡镇："+name+" 邮编："+code+"\n");
                        }

                    }



                    yxfwStr.append("\n 震中地区的人口密度 \n");
                    List<Feature> fs2=yxfwresult.popdensity.GetFeatureItems();
                    if(fs2==null||fs2.isEmpty()){

                        yxfwStr.append(" 未查询到相关信息 \n");
                    }
                    else{
                        for(Feature fe:fs2)
                        {
                            HashMap attributesItems=fe.GetAttributeItems();
                            String name=attributesItems.get("name").toString();
                            String code=attributesItems.get("postcode").toString();
                            String total=attributesItems.get("total").toString();
                            String family=attributesItems.get("family").toString();
                            String over65=attributesItems.get("over65").toString();
                            String under14=attributesItems.get("under14").toString();
                            String resident=attributesItems.get("resident").toString();
                            yxfwStr.append("    名称："+name+"\n");
                            yxfwStr.append("    邮编："+code+"\n");
                            yxfwStr.append("    总人口："+total+"\n");
                            yxfwStr.append("    家庭："+family+"\n");
                            yxfwStr.append("    14岁以下："+under14+"\n");
                            yxfwStr.append("    65岁以上："+over65+"\n");
                            yxfwStr.append("    常驻人口："+resident+"\n");


                        }

                    }
                    yxfw_textview.setText(yxfwStr.toString());

                }
                if(v.getId()==R.id.img2)
                {
                    title_textview.setText("震中距");
                    if(zzdis==null){
                        return;
                    }
                    List<Feature> zzf=  zzdis.GetFeatureItems();
                    String[] fields=new String[]{"name","DISTANCE"};
                    List<Map<String, Object>> list=getListData(fields,zzf);
                    listView.setAdapter(new PG_Adapter(mContext, list));

                }
                if(v.getId()==R.id.img3)
                {
                    if(historydz==null){
                        return;
                    }
                    title_textview.setText("历史地震");
                    List<Feature> historyf=historydz.GetFeatureItems();
                    String[] fields=new String[]{"location","date_","magnitude","DISTANCE"};
                    List<Map<String, Object>> list=getListData(fields,historyf);
                    listView.setAdapter(new PG_Adapter(mContext, list));

                }
                if(v.getId()==R.id.img4)
                {
                    title_textview.setText("地震案例");
                    if(archiveEarthquakes==null||archiveEarthquakes.isEmpty()){

                        return;
                    }
                    String[] fields=new String[]{"",""};
                    List<Map<String, Object>> list=getListData(fields,archiveEarthquakes);
                    listView.setAdapter(new PG_Adapter(mContext, list));
                }
            }


        }
    };

    /**
     * 结果集合的处理
     * 从GP 的 result features or EarthArchiveNode 集合 到adpter的处理
     * @param labels 这是要显示的字段集合
     * @param totallist gp获取的结果和数据库查询的结果
     * @return
     */
    public List<Map<String, Object>> getListData(String[] labels,List totallist){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        if(totallist==null||totallist.isEmpty()){

            return  list;
        }
        for (Object ob:totallist ){

            Map<String, Object> map=new HashMap<String, Object>();

            if(ob instanceof Feature){
                Feature curFea=((Feature) ob);
              HashMap attributeItems  =curFea.GetAttributeItems();
              String title=attributeItems.get(labels[0]).toString();
              map.put("title",title);
                String content=  ConvertFeatureToString(curFea,labels);
                map.put("content",content);

            }
            if(ob instanceof EarthArchiveNode){
                EarthArchiveNode earthArchiveNode=((EarthArchiveNode) ob);
                String name=earthArchiveNode.position.toString();
                map.put("title",name);
                String content=  ConvertEarthNodeToString(earthArchiveNode);
                map.put("content",content);

            }

            list.add(map);
        }


        return list;
    }

    /**
     * 转换feature类中属性内容 到UI 上要显示的字符串
     * @param feature
     * @param labels
     * @return
     */
    public String ConvertFeatureToString(Feature feature,String[] labels){
        //
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("");
        HashMap attributeItems  =feature.GetAttributeItems();
        for (int i=0;i < labels.length;i++){

            String label=labels[i].toString();
            String zh_label=labelsmap.get(label).toString();
            String item= attributeItems.get(label).toString();
            stringBuilder.append( " "+zh_label+" : "+item+"\n");
        }

        return  stringBuilder.toString();
    }

    /**
     * 转换node地震案例类到UI上要显示的字符串
     * @param earthArchiveNode
     * @return
     */
    public String ConvertEarthNodeToString(EarthArchiveNode earthArchiveNode){
        StringBuilder stringBuilder=new StringBuilder();
        String time="   时间：" +earthArchiveNode.GetTime()+"\n";
        String level="   震级："+ String.valueOf(earthArchiveNode.level)+"\n";
        String rysw="   人员伤亡（人）： \n"+"      死亡"+earthArchiveNode.death1+"  重伤"+earthArchiveNode.death2+"  轻伤"+earthArchiveNode.death3+"\n";
        String fwph="   房屋破坏（平方米）："+"\n"
                +"      毁坏 "+earthArchiveNode.destroy1+"\n"
                +"      严重 "+earthArchiveNode.destroy2+"\n"
                +"      中等 "+earthArchiveNode.destroy3+"\n"
                +"      轻微 "+earthArchiveNode.destroy4+"\n" ;
        String zjjjss="   直接经济损失(万元): "+earthArchiveNode.lost+" \n";
        stringBuilder.append(time);
        stringBuilder.append(level);
        stringBuilder.append(rysw);
        stringBuilder.append(fwph);
        stringBuilder.append(zjjjss);
        return stringBuilder.toString();
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
        pbBar.setVisibility(View.VISIBLE);
        //接受全局变量的参数 可能是短信或者手动添加的坐标点
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
            String a =EllipseCal.doubleToString(ells[0].a); //String.valueOf(ells[0].a);
            String b =EllipseCal.doubleToString(ells[0].b);  //String.valueOf(ells[0].b);
            //集镇区面积
            String area = String.valueOf(ells[0].getArea());
            //灾区面积
            String areaZQ = String.valueOf(ells[1].getArea());
            //添加极震区坐标点Gp rest get http
            yxfwresult.tag_ld=ld;
            yxfwresult.tag_a=a;
            yxfwresult.tag_b=b;
            yxfwresult.tag_area=area;
            yxfwresult.tag_disaster=areaZQ;
            //sqllite读取条件A<M<B中的地震案例
            GetArchiveByLevel(level);
            //走一堆GP肯定花时间
            //考虑加个loader
            ProductCenterPoint(x,y,String.valueOf(level),ells[0].a,ells[0].b);



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
        WebRequestClient.get(purl, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    if(responseString.isEmpty())
                    {
                        return;
                    }


                }catch (Exception je){
                    String error=je.toString();
                    pbBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "调用地理服务失败",
                            Toast.LENGTH_SHORT).show();
                }
                finally {
                    //开始下几个调用
                    //确认同步已经完成
                    //后边几个可以同时分析gp
                    GetMakeEllipseBuffer();
                }
            }

            @Override
            public void onStart() {
                // called before request is started
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
        WebRequestClient.get(purl, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    if(responseString.isEmpty()){

                        return;
                    }
                    HashMap rhm=    JsonParser.resolveJson(responseString);
                    GpResult gp=(GpResult) rhm.get("xianJizhenqu");
                    GpResult gp1=(GpResult)rhm.get("xiangzhenJizhenqu");
                    //tv1.setText(tweetText);

                    yxfwresult.bufferXian=gp;
                    yxfwresult.bufferZhen=  gp1;
                    //初始到adpter类然后接入UI


                }catch (Exception ex){
                    String error=ex.toString();
                    pbBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "调用地理服务失败",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                finally {
                    GetPopDensity();
                }

            }

            @Override
            public void onStart() {
                // called before request is started
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
        WebRequestClient.get(urlStr, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString)
            {
                try {
                    if(responseString.isEmpty()){

                        return;
                    }
                    HashMap rHashmap= JsonParser.resolveJson(responseString);
                    GpResult gp=(GpResult) rHashmap.get("polulatinZhenzhong");
                    yxfwresult.popdensity=gp;

                }
                catch (Exception ex){

                    String error=ex.toString();
                    pbBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "调用地理服务失败",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                finally {

                    GetGPzzDistance();
                }

            }

            @Override
            public void onStart() {
                // called before request is started
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
        WebRequestClient.get(urlStr, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    if(responseString.isEmpty()){
                        return;
                    }

                    HashMap rHashmap= JsonParser.resolveJson(responseString);
                    zzdis=(GpResult)rHashmap.get("xianzhenzhongju");
                    historydz=(GpResult)rHashmap.get("lishidizhen");

                }catch (Exception ex){

                    String error=ex.toString();
                    pbBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "调用地理服务失败",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                finally {
                    //
                    pbBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "查询完成",
                            Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onStart() {
                // called before request is started
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
        archiveEarthquakes=    arvchEarth.getNearArchivelBylevel(level);


    }


}
