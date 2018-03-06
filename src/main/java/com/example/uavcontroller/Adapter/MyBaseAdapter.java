package com.example.uavcontroller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uavcontroller.R;

import java.util.List;

/**
 * Created by Administrator on 2017/4/16.
 */

public class MyBaseAdapter extends BaseAdapter {
    private List<String> item;
    private Context mContext;

    public MyBaseAdapter(List<String> item, Context context){
        this.item = item;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String posture = item.get(position);
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.posture_list_item,null);
            viewHolder.posture_item = (TextView) convertView.findViewById(R.id.posture_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.posture_item.setText(posture);
        return convertView;
    }
    private static class ViewHolder{
        private TextView posture_item;
    }
}
