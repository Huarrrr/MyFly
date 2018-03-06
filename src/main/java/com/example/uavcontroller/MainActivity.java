package com.example.uavcontroller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler = new Handler();
    private boolean isFrist = true;//判断是否是第一次使用，默认是
    private SharedPreferences pref = null ;//创建SharedPreferences存储对象
    private TextView text_time;
    private Button stop_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        text_time = (TextView)findViewById(R.id.text_time);
        stop_button = (Button) findViewById(R.id.textView);
        pref = getSharedPreferences("data",MODE_PRIVATE);//实例化SharedPreferences存储对象
        isFrist = pref.getBoolean("isFrist",true);
        //延迟执行任务
        stop_button.setOnClickListener(this);
        MyCountDownTimer timer = new MyCountDownTimer(5000,1000,text_time);
        timer.start();
        mHandler.postDelayed(runnable,5000);



    }
    Runnable runnable =  new Runnable() {
        @Override
        public void run() {
            if(isFrist){
                Intent intent =  new Intent(MainActivity.this,GuideActivity.class);
                startActivity(intent);
                finish();

            }else {
                Intent intent = new Intent(MainActivity.this,Controller.class);
                startActivity(intent);
                finish();
            }

        }
    };


    @Override
    public void onClick(View v) {
        if(isFrist){
            Intent intent =  new Intent(MainActivity.this,GuideActivity.class);
            startActivity(intent);
            mHandler.removeCallbacks(runnable);
            finish();
        }else {
            Intent intent = new Intent(MainActivity.this,Controller.class);
            startActivity(intent);
            mHandler.removeCallbacks(runnable);
            finish();
        }
    }
}
