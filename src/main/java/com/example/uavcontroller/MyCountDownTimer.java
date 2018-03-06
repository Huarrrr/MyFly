package com.example.uavcontroller;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/17.
 */

public class MyCountDownTimer extends CountDownTimer {

    private TextView time_Text;
    public MyCountDownTimer(long millisInFuture, long countDownInterval,TextView time_Text) {
        super(millisInFuture, countDownInterval);
        this.time_Text = time_Text;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        time_Text.setText("倒计时"+"("+" "+millisUntilFinished/1000+" "+")");


    }

    @Override
    public void onFinish() {
        time_Text.setVisibility(View.GONE);

    }
}
