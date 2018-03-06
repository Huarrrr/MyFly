package com.example.uavcontroller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uavcontroller.Fragment.PIDFragment;
import com.example.uavcontroller.Fragment.PostureFragment;

/**
 * Created by Administrator on 2017/4/15.
 */

public class SetActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView left_black;
    private TextView pid_set;
    private TextView zitai_set;
    private TextView line;
    private TextView line2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window =getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.red));
            //底部导航栏
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.set_layout);
        init();
    }

    private void init() {
        initView();
        initEvent();
    }

    private void initEvent() {
        left_black.setOnClickListener(this);
        pid_set.setOnClickListener(this);
        zitai_set.setOnClickListener(this);
    }

    private void initView() {
        left_black = (ImageView) findViewById(R.id.left_black);
        pid_set = (TextView)findViewById(R.id.pid_set);
        zitai_set = (TextView) findViewById(R.id.zitai_set);
        line = (TextView) findViewById(R.id.line);
        line2 = (TextView) findViewById(R.id.line2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_black:
                finish();
                break;
            case R.id.pid_set:
                line.setBackgroundColor(getResources().getColor(R.color.white));
                line2.setBackgroundColor(getResources().getColor(R.color.gray));
                PIDFragment pid = new PIDFragment();
                setFragment(pid);
                break;
            case R.id.zitai_set:
                line.setBackgroundColor(getResources().getColor(R.color.gray));
                line2.setBackgroundColor(getResources().getColor(R.color.white));
                PostureFragment posture = new PostureFragment();
                setFragment(posture);
                break;
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentManager manger = getFragmentManager();
        FragmentTransaction transaction = manger.beginTransaction();
        transaction.replace(R.id.set_framelayout,fragment);
        transaction.commit();
    }
}
