package com.example.zhoajie.sxeqem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ZLFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ZLFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZLFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //expandlistview
    private ExpandableListView exLv;
    private Context mContext;
    //用于Expandistview数据绑定类
    private ExListViewAdapterZL exLtAdapter;
    //
    private Map<String, List<String>> dataset = new HashMap<>();
    private List<List<XZQ_NODE>> grplist=new ArrayList<>();
    private List<XZQ_NODE> grp=new ArrayList<>();


    //结合节点类型数据进行初始化ListView
    private List<XZQ_NODE> xzqnodes=null;

    // WebPageView 控件显示网页内容的  popuupwindow
    private AlertDialog webviewdialog;
    private AlertDialog.Builder webDialogBuilder;
    private WebView ctl_webview;
    private ProgressDialog progressDialog = null;
    private TextView tv_title_webview;
    private ImageButton imgbtn_closewebview;

    WebViewClient wvClient;
    public ZLFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZLFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ZLFragment newInstance(String param1, String param2) {
        ZLFragment fragment = new ZLFragment();
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
        initialData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_zl, container, false);
        webDialogBuilder=new AlertDialog.Builder(getActivity());
        //初始化弹出web页面内容
        initWebViewDialog();
        //初始化expandablelistview内容
        exLv=(ExpandableListView)v.findViewById(R.id.expendlist_zl);
        exLv.setGroupIndicator(null);

        exLtAdapter =new ExListViewAdapterZL(mContext,grp,grplist);
        exLv.setAdapter(exLtAdapter);

        //设置childitem click 事件
        exLv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //需要传入对应的html参数
                 XZQ_NODE xzq_node=   (XZQ_NODE) exLtAdapter.getChild(groupPosition,childPosition);
                //数据库中content字段对应html名称
                String p_url=xzq_node.content;
                String title=xzq_node.name;
                showItemInWebView(p_url,title);
                return true;
            }
        });
        return v;
    }

    //初始数据内容 从sqllite
    private void initialData() {
        //数据库内容获取
        //获取第一层数据
        //获取第二层数据
        CityRepo cityRepo=new CityRepo(mContext);
        xzqnodes=  cityRepo.getNodesBylevel(1);
        //
        int grpcount=xzqnodes.size();
        if(grpcount==0)
        {
            return;

        }
        XZQ_NODE xzq_node=null;
        for (int i=0;i<grpcount;i++)
        {
            xzq_node=xzqnodes.get(i);
            //
            int pid=xzq_node.c_node;
            //获取节点数据
            List<XZQ_NODE>  childList_xzq=cityRepo.getNodesByParentId(pid);
            //添加到对应组中
            grplist.add(childList_xzq);
            grp.add(xzq_node);
        }


    }

    //初始化弹出框内容 webview
    /**
     * 初始化弹出对话框
     * 点击添加按钮事件和取消事件
     */
    private void initWebViewDialog()
    {

        LayoutInflater layoutInflater =LayoutInflater.from(getActivity());
        View weblayout= layoutInflater.inflate(R.layout.webview_details, null);

        webDialogBuilder.setView(weblayout);
        webviewdialog = webDialogBuilder.create();

        ctl_webview=(WebView)weblayout.findViewById(R.id.webpageview);
        //progressBar=(ProgressBar)weblayout.findViewById(R.id.progressBar);
        tv_title_webview=(TextView)weblayout.findViewById(R.id.tv_title_webview);
        imgbtn_closewebview=(ImageButton)weblayout.findViewById(R.id.imgbtn_webview);

        imgbtn_closewebview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webviewdialog.cancel();
            }
        });
        WebSettings wSet = ctl_webview.getSettings();
        wSet.setJavaScriptEnabled(true);
        wSet.setDomStorageEnabled(true);
        // 设置支持javascript
        wSet.setJavaScriptEnabled(true);
        // 启动缓存
        wSet.setAppCacheEnabled(true);
        // 设置缓存模式
        wSet.setCacheMode(WebSettings.LOAD_DEFAULT);


    }

    //关闭dialog的方法
    private void closeDialog(AlertDialog dialog) {
        try {
            java.lang.reflect.Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showItemInWebView(String htmlStr,String titleStr)
    {//效果糖 弹出网页的效果
        htmlStr="file:///android_asset/ZL/"+htmlStr+".htm";

        ctl_webview.loadUrl(htmlStr);
        tv_title_webview.setText(titleStr);
        ctl_webview.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                //view.loadUrl(url);
                return true;
            }
        });
        webviewdialog.show();
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

}
