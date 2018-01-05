package com.example.zhoajie.sxeqem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/20.
 */

public class ExListViewAdapterAL extends BaseExpandableListAdapter {
    private List<DEMO_NODE> groupArray;
    private List<List<DEMO_NODE>> childArray;

    private Map<String, List<String>> childMap;
    private Context mContext;

    public ExListViewAdapterAL(Context context, List<DEMO_NODE> groupArray, List<List<DEMO_NODE>> childArray){
        mContext = context;
        this.groupArray = groupArray;
        this.childArray = childArray;

    }

    @Override
    public int getGroupCount() {
        return groupArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // return   childMap.get(childArray.get(groupPosition)).size();
        return childArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupArray.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return childArray.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        ExListViewAdapterAL.GroupHolder holder = null;
        if(view == null){
            holder = new ExListViewAdapterAL.GroupHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.listviewgroup_zl, null);
            holder.groupName = (TextView)view.findViewById(R.id.tv_listgp_zl);
            holder.arrow = (ImageView)view.findViewById(R.id.imgview_listgp_zl);

            view.setTag(holder);
        }else{
            holder = (ExListViewAdapterAL.GroupHolder)view.getTag();
        }


        //判断是否已经打开列表
        if(isExpanded){
            holder.arrow.setBackgroundResource(R.drawable.ic_expand_less_black_24dp);
        }else{
            holder.arrow.setBackgroundResource(R.drawable.ic_expand_more_black_24dp);
        }
        DEMO_NODE demo_node=groupArray.get(groupPosition);
        holder.dirUrl=demo_node.content;
        holder.groupName.setText(demo_node.name);
        holder.grpNodeId=demo_node.c_node;

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        ExListViewAdapterAL.ChildHolder holder = null;
        if(view == null){
            holder = new ExListViewAdapterAL.ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.listviewitem_zl, null);
            holder.childName = (TextView)view.findViewById(R.id.tv_listitem_zl);
            holder.sound = (ImageView)view.findViewById(R.id.imgview_listitem_zl);
            holder.divider = (ImageView)view.findViewById(R.id.iv_divider_listitem_zl);
            view.setTag(holder);
        }else{
            holder = (ExListViewAdapterAL.ChildHolder)view.getTag();
        }

        if(childPosition == 0){
            //holder.divider.setVisibility(View.GONE);
        }

        holder.sound.setBackgroundResource(R.drawable.ic_arrow_forward_black_24dp);
         DEMO_NODE demo_node=(DEMO_NODE)childArray.get(groupPosition).get(childPosition);
        holder.dirUrl=demo_node.content;
        holder.childName.setText(demo_node.name);
        holder.NodeId=  demo_node.c_node ;
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder{
        public TextView groupName;
        public ImageView arrow;
        public int grpNodeId;
        public String dirUrl;
    }


    class ChildHolder{
        public TextView childName;
        public ImageView sound;
        public ImageView divider;
        public int NodeId;
        public String dirUrl;
    }
}
