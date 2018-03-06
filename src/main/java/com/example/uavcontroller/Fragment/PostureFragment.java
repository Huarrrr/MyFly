package com.example.uavcontroller.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.uavcontroller.Adapter.MyBaseAdapter;
import com.example.uavcontroller.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/16.
 */

public class PostureFragment extends Fragment {

    private View view;
    private ListView posture;
    private List<String> item;
    private MyBaseAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.posture_fragment,container,false);
        init();
        return view;
    }

    private void init() {
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        item = new ArrayList<>();

        item.add("加速度");
        item.add("陀螺仪");

        adapter = new MyBaseAdapter(item,getActivity());
        posture.setAdapter(adapter);
    }

    private void initView() {
        posture = (ListView) view.findViewById(R.id.posture_set);
    }

    private void initEvent() {
    }

}
