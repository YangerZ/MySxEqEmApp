package com.example.zhoajie.sxeqem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 * 此处的fragmentadapter是用于资料页面和案例页面切换
 * 忽然想到这个应该用到整个程序，不应该用开始的数组年轻啊
 *返回的内容提供给viewpager
 */

public class FragAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    public FragAdapter(FragmentManager fm,List<Fragment> fragments)
    {
        super(fm);
        mFragments=fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


}
