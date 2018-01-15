package com.example.zhoajie.sxeqem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhoajie.sxeqem.global.MessageEvent;
import com.example.zhoajie.sxeqem.global.MyApp;
import com.example.zhoajie.sxeqem.MapFragment.OnFragmentInteractionListener;
import com.idescout.sql.SqlScoutServer;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.logging.Handler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity   implements
        OnFragmentInteractionListener,TabListFragment.OnFragmentInteractionListener,ZLFragment.OnFragmentInteractionListener,ALFragment.OnFragmentInteractionListener,PGFragment.OnFragmentInteractionListener  {
    public MyApp appState;
    public DBManager dbHelper;
    public static Fragment[] mFragments;
    private FragmentManager manager;
    private  FragmentTransaction transaction;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_locate:
                    SetSelection(0);
                    return true;
                case R.id.navigation_data:
                    SetSelection(1);
                    return true;
                case R.id.navigation_estimate:
                   SetSelection(2);
                    return true;
                case R.id.navigation_dynamic:
                    SetSelection(1);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使用的全局变量
        appState = ((MyApp)getApplicationContext());
        //appState.setLevel("1");
        setContentView(R.layout.activity_main);
        //外部数据库写入内容
        dbHelper = new DBManager(this);
        dbHelper.openDatabase();
        dbHelper.closeDatabase();
        //

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        manager=getSupportFragmentManager();
        mFragments=new Fragment[3];

         SetSelection(0);
    }


    /*
    *隐藏所有的fragment
     */
    private void hideFragment(FragmentTransaction transaction) {

        for (int i=0;i<mFragments.length;i++)
         {
             Fragment curf=mFragments[i];
                 if(curf==null)
                 continue;
                 transaction.hide(curf);

         }
    }
    /*
    *按菜单索引设置当前显示fragment 0 1 2 3 的顺序依次是地图  列表  详细内容
     */
    private void SetSelection(int index){
        //重置button按钮样式  此处不需要

        transaction=manager.beginTransaction();
        //隐藏其他的fragment
        //

       hideFragment(transaction);
        //用Fragment动态代替布局文件中内容
        switch (index)
        {
            case 0:
                if (mFragments[0] == null)
                {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mFragments[0]  = new MapFragment();
                    transaction.add(R.id.con_frame, mFragments[0]);
                }
                else
                {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mFragments[0]);
                }
                transaction.commit();
                break;
            case 1:
                if (mFragments[1] == null)
                {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mFragments[1]  = new TabListFragment();
                    transaction.add(R.id.con_frame, mFragments[1]);
                }
                else
                {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mFragments[1]);
                }
                transaction.commit();
                break;
            case 2:
			if (mFragments[2] == null)
                {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mFragments[2]  = new PGFragment();


                    transaction.add(R.id.con_frame, mFragments[2]);
                    transaction.commit();
                }
                else
                {

                    PGFragment fragment2 = (PGFragment)mFragments[2];

                    //EventBus.getDefault().post(new MessageEvent());
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mFragments[2]);
                    transaction.commit();
                    fragment2.setTabUIVisible();
                    fragment2.CalculateEllipse();
                }


                break;
            case 3:
                break;
            default:
                break;

        }


    }


    @Override
    public void onFragmentInteraction(Uri uri) {
            String s=uri.toString();
    }




}
