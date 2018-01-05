package com.example.zhoajie.sxeqem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabListFragment extends  android.support.v4.app.Fragment implements ZLFragment.OnFragmentInteractionListener,ALFragment.OnFragmentInteractionListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TabLayout ctl_tabLayout;
    private ViewPager ctl_ViewPager;


    private OnFragmentInteractionListener mListener;

    public TabListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabListFragment newInstance(String param1, String param2) {
        TabListFragment fragment = new TabListFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_tab_list, container, false);

        //tab页面及viewpager
        ctl_tabLayout=(TabLayout) v.findViewById(R.id.tabLayout);
        ctl_ViewPager=(ViewPager)v.findViewById(R.id.tab_ViewPager);

        //fragmentadpater初始化
        //构造适配器
        List<android.support.v4.app.Fragment> fragments=new ArrayList<android.support.v4.app.Fragment>();
        fragments.add(new ZLFragment());
        fragments.add(new ALFragment());
        FragAdapter adapter = new FragAdapter(getChildFragmentManager(), fragments);
        ctl_ViewPager.setAdapter(adapter);

        ctl_tabLayout.setupWithViewPager(ctl_ViewPager);
        //此处坑深 fuck populateItem啥啥的方法 会执行removealltabitem
        //调用顺序必须是先setupWithViewPager
        //然后再 加tabiitem
        ctl_tabLayout.getTabAt(0).setText(R.string.tab1_title);
        ctl_tabLayout.getTabAt(1).setText(R.string.tab2_title);

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

    @Override
    public void onFragmentInteraction(Uri uri) {

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
