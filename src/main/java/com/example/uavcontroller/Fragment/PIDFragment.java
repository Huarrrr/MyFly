package com.example.uavcontroller.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.uavcontroller.Adapter.MyBaseExpandableListAdapter;
import com.example.uavcontroller.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class PIDFragment extends Fragment {

    private View view;
    private ExpandableListView elv;
    private List<String> group;
    private List<String> item;
    private List<List<String>> itemArray;
    private MyBaseExpandableListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pid_fragment,container,false);
        init();
        return view;

    }

    private void init() {

        initView();
        initData();
    }

    private void initData() {
        group = new ArrayList<>();
        item = new ArrayList<>();
        itemArray = new ArrayList<>();

        group.add("内环");
        group.add("外环");

        item.add("横滚");
        item.add("俯仰");
        item.add("航向");

        itemArray.add(item);
        itemArray.add(item);

        adapter = new MyBaseExpandableListAdapter(group,itemArray,getActivity());
        elv.setAdapter(adapter);
        elv.expandGroup(0);
    }

    private void initView() {
        elv = (ExpandableListView) view.findViewById(R.id.pid_elv);

    }
}
