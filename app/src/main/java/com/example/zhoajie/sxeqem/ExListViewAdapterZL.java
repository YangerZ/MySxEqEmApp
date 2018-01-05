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
 * Created by Administrator on 2017/9/14.
 * 此处是展开ExpandListView的Adapter类
 * 用于初始数据内容和list页面内容
 */

public class ExListViewAdapterZL extends BaseExpandableListAdapter {
    private List<XZQ_NODE> groupArray;
    private List<List<XZQ_NODE>> childArray;

    private Map<String, List<String>> childMap;
    private Context mContext;

    public ExListViewAdapterZL(Context context, List<XZQ_NODE> groupArray, List<List<XZQ_NODE>> childArray){
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
        GroupHolder holder = null;
        if(view == null){
            holder = new GroupHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.listviewgroup_zl, null);
            holder.groupName = (TextView)view.findViewById(R.id.tv_listgp_zl);
            holder.arrow = (ImageView)view.findViewById(R.id.imgview_listgp_zl);
            view.setTag(holder);
        }else{
            holder = (GroupHolder)view.getTag();
        }


        //判断是否已经打开列表
        if(isExpanded){
            holder.arrow.setBackgroundResource(R.drawable.ic_expand_less_black_24dp);
        }else{
            holder.arrow.setBackgroundResource(R.drawable.ic_expand_more_black_24dp);
        }
        XZQ_NODE xzq_node=groupArray.get(groupPosition);

        holder.groupName.setText(xzq_node.name);
        holder.grpNodeId=xzq_node.c_node;
        holder.dirUrl=xzq_node.content;
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        ChildHolder holder = null;
        if(view == null){
            holder = new ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.listviewitem_zl, null);
            holder.childName = (TextView)view.findViewById(R.id.tv_listitem_zl);
            holder.sound = (ImageView)view.findViewById(R.id.imgview_listitem_zl);
            holder.divider = (ImageView)view.findViewById(R.id.iv_divider_listitem_zl);
            view.setTag(holder);
        }else{
            holder = (ChildHolder)view.getTag();
        }

        if(childPosition == 0){
            //holder.divider.setVisibility(View.GONE);
        }

        holder.sound.setBackgroundResource(R.drawable.ic_arrow_forward_black_24dp);
        XZQ_NODE xzq_node=(XZQ_NODE)childArray.get(groupPosition).get(childPosition);
        holder.dirUrl=xzq_node.content;
        holder.childName.setText(xzq_node.name);
        holder.NodeId=  xzq_node.c_node ;
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
