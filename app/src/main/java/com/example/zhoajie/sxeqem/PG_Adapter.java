package com.example.zhoajie.sxeqem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/10.
 */

public class PG_Adapter extends BaseAdapter {
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public PG_Adapter(Context context, List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }

    /**
     * 对应listview中的layout内容
     */
    public final class FeatureItem{

        public TextView title;

        public TextView content;
    }
    @Override
    public int getCount() {
        return data.size();
    }
    /**
     * 获得某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FeatureItem featureItem=null;
        if(convertView==null){
            featureItem=new FeatureItem();
            //获得组件，实例化组件
            convertView=layoutInflater.inflate(R.layout.listviewitem_pg, null);
            featureItem.title=(TextView)convertView.findViewById(R.id.pg_detail_title);
            featureItem.content=(TextView)convertView.findViewById(R.id.pg_detail_content);
            convertView.setTag(featureItem);
        }else{
            featureItem=(FeatureItem)convertView.getTag();
            featureItem.title=(TextView)convertView.findViewById(R.id.pg_detail_title);
            featureItem.content=(TextView)convertView.findViewById(R.id.pg_detail_content);

        }
        //绑定数据

        featureItem.title.setText((String)data.get(position).get("title"));
        featureItem.content.setText((String)data.get(position).get("content"));
        return convertView;
    }

}
