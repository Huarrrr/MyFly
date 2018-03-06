package com.example.uavcontroller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uavcontroller.R;

import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {
    private List<String> group;
    private List<List<String>> itemArray;
    private Context mContext;


    public MyBaseExpandableListAdapter(List<String> group, List<List<String>> itemArray, Context mContext){
        this.group = group;
        this.itemArray = itemArray;
        this.mContext = mContext;

    }
    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return itemArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return itemArray.get(groupPosition).get(childPosition);
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderGroup viewHolderGroup = new ViewHolderGroup();

            String groupstr = group.get(groupPosition);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_exlist_group, null);
                viewHolderGroup.tv_group_name = (TextView) convertView.findViewById(R.id.grou_name);
                viewHolderGroup.iv_grop = (ImageView) convertView.findViewById(R.id.grou_pic);
                convertView.setTag(viewHolderGroup);
            } else {
                viewHolderGroup = (ViewHolderGroup) convertView.getTag();
            }
            viewHolderGroup.tv_group_name.setText(groupstr);
            if (isExpanded) {
                viewHolderGroup.iv_grop.setBackgroundResource(R.drawable.ic_arrow_drop_below);
            } else {
                viewHolderGroup.iv_grop.setBackgroundResource(R.drawable.ic_arrow_drop_left);
            }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String itemstr = itemArray.get(groupPosition).get(childPosition);
        ViewHolderItem viewHolderItem = new ViewHolderItem();
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_exlist_item,null);
            viewHolderItem.tv_item_name = (TextView) convertView.findViewById(R.id.id_item);
            convertView.setTag(viewHolderItem);
        }else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }
        viewHolderItem.tv_item_name.setText(itemstr);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    private static class ViewHolderGroup{
        private TextView tv_group_name;
        private ImageView iv_grop;
    }

    private static class ViewHolderItem{
        private TextView tv_item_name;
    }

}
