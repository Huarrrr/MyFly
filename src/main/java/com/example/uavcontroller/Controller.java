package com.example.uavcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.uavcontroller.View.Rocker;
import com.example.uavcontroller.View.Rocker2;
import com.example.uavcontroller.View.Speedometer;
import com.example.uavcontroller.View.SwitchView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2017/3/12.
 */

public class Controller extends AppCompatActivity{
    private Rocker rocker;
    private Rocker2 rocker2;
    private SwitchView switchView;
    private RelativeLayout rl;
    private boolean isFrist =true;
    private Speedometer speedometer;
    private ImageView set;
    private byte[] data;
    private boolean isOpen =true;
    private Socket socket;
    private Button power;
    private int isON = 0;//0表示油门关，1表示油门开
    private boolean isfrist  = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_layout);

        initView();
        initEvent();
        initData();
    }


    private void initView() {
        rocker = (Rocker) findViewById(R.id.id_rocker);
        switchView = (SwitchView) findViewById(R.id.switchview);
        rl = (RelativeLayout) findViewById(R.id.rl );
        rocker2 =(Rocker2) findViewById(R.id.id_rocker2);
        speedometer = (Speedometer) findViewById(R.id.id_speedometer);
        set = (ImageView) findViewById(R.id.set);
        power = (Button) findViewById(R.id.power);
    }

    private void initEvent() {
        rl.setVisibility(ImageView.VISIBLE);
        rl.setOnClickListener(null);

        //方向键监听器
        rocker.addOrientationListener(new Rocker.Orientation() {

            @Override
            public void top(int pitchValue,int course) {
                Log.e("top",pitchValue+"");
                data[7]=(byte)(1500 >> 8);
                data[8]=(byte)(1500 & 0xff);
                data[9]=(byte)(pitchValue >> 8);
                data[10]=(byte)(pitchValue & 0xff);
            }

            @Override
            public void bottom(int pitchValue,int course) {
                Log.e("bottom",pitchValue+"");
                data[7]=(byte)(1500 >> 8);
                data[8]=(byte)(1500 & 0xff);
                data[9]=(byte)(pitchValue >> 8);
                data[10]=(byte)(pitchValue & 0xff);

            }

            @Override
            public void left(int rollValue,int course) {
             //   data[5]=(byte)(course >> 8)  ;
             //   data[6]=(byte)(course & 0xff);
                data[7]=(byte)(rollValue >> 8);
                data[8]=(byte)(rollValue & 0xff);
                data[9]=(byte)( 1500>> 8);
                data[10]=(byte)( 1500& 0xff);
                Log.e("left",rollValue+"");
            }

            @Override
            public void right(int rollValue,int course) {
              //  data[5]=(byte)(course >> 8)  ;
              //  data[6]=(byte)(course & 0xff);
                data[7]=(byte)(rollValue >> 8);
                data[8]=(byte)(rollValue & 0xff);
                data[9]=(byte)( 1500>> 8);
                data[10]=(byte)( 1500& 0xff);
                Log.e("right",rollValue+"");
            }

            @Override
            public void letgo(int rollValue, int pithchValue,int course) {
                Log.e("letgo",rollValue +"    "+pithchValue);
                //data[5]=(byte)(course >> 8)  ;
                //data[6]=(byte)(course & 0xff);
                data[7]=(byte)(rollValue >> 8);
                data[8]=(byte)(rollValue & 0xff);
                data[9]=(byte)( pithchValue>> 8);
                data[10]=(byte)( pithchValue& 0xff);
            }

        });


        //控制器开关监听器
        switchView.setOnSwitchStateListener(new SwitchView.OnSwitchStateListener() {
            @Override
            public void onSwitchStateUpdate(boolean switchState) {
               if(switchState){
                   rl.setVisibility(View.GONE);

               }else{
                   rl.setVisibility(View.VISIBLE);
                   rl.setOnClickListener(null);
               }
            }
        });
        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (isON){
                    case 0:
                        power.setBackgroundResource(R.drawable.power);
                        isOpen(true);
                        rocker2.setEnabled(true);
                        if(isfrist) {
                            connect();//向无人机发送链接请求
                            isfrist = false;
                        }
                        droneUpOrDown();//开启油门
                        isON = 1;
                        break;
                    case 1:
                        power.setBackgroundResource(R.drawable.power2);
                        isOpen(false);//关闭发送油门值的线程
                        speedometer.reset();//将油门值零
                        rocker2.setEnabled(false);
                        isON = 0;
                        break;
                }
            }
        });
        //油门监听器
        rocker2.addOrientationListener(new Rocker2.Orientation() {
            @Override
            public void top() {
                isOpen = true;
                speedometer.cancelled(false);
                speedometer.setSpeedometer(1);
                if(isFrist){
                    speedometer.startThread();
                    isFrist = false;
                }
            }
            @Override
            public void bottom() {
                speedometer.cancelled(false);
                speedometer.setSpeedometer(2);
                if(isFrist){
                    speedometer.startThread();
                    isFrist = false;
                }
            }

            @Override
            public void cancelled() {
                speedometer.cancelled(true);
                isFrist =true;
            }
        });
        //监听油门变化
        speedometer.setOnSpeedometerChanged(new Speedometer.onSpeedometerChanged() {

            @Override
            public void onChanged(int accelerator) {
                //acce = accelerator;
                data[3]=(byte) (accelerator>>8);
                data[4]=(byte) (accelerator & 0xff);

            }
        });

        //跳转到设置PID 姿态页面
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Controller.this,SetActivity.class);
                startActivity(intent);
            }
        });

    }

    private void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.4.1",333);
                    socket.getOutputStream().write("GEC\r\n".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void droneUpOrDown(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    if(socket != null) {
                        OutputStream out = socket.getOutputStream();
                        while (isOpen) {
                            //  Log.e("油门值",acce+"");
                            out.write(data);
                            Thread.sleep(5);
                        }
                    }
               } catch (IOException e) {
                   e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    private void isOpen(boolean open){
        isOpen =open;
    }
   private void initData(){
       data = new byte[34];
       data[0]=(byte) 0xAA;
       data[1]=(byte) 0xC0;
       data[2]=(byte) 0x1C;
       data[3]=0x00;             //油门高
       data[4]=0x00;             //油门低
       data[5]=(byte)(1500 >> 8)  ;//航向高
       data[6]=(byte)(1500 & 0xff);//航向低
       data[7]=(byte)(1500 >> 8);//横滚高
       data[8]=(byte)(1500 & 0xff);//横滚低
       data[9]=(byte)(1500 >> 8);//俯仰高
       data[10]=(byte)(1500 & 0xff);//俯仰低
      /* data[5]=0x00;
       data[6]=0x00;
       data[7]=0x00;
       data[8]=0x00;
       data[9]=0x00;
       data[10]=0x00;*/
       data[11]=0x00;
       data[12]=0x00;
       data[13]=0x00;
       data[14]=0x00;
       data[15]=0x00;
       data[16]=0x00;
       data[17]=0x00;
       data[18]=0x00;
       data[19]=0x00;
       data[20]=0x00;
       data[21]=0x00;
       data[22]=0x00;
       data[23]=0x00;
       data[24]=0x00;
       data[25]=0x00;
       data[26]=0x00;
       data[27]=0x00;
       data[28]=0x00;
       data[29]=0x00;
       data[30]=0x00;
       data[31]=(byte)0x1C;
       data[32]=(byte)0x0D;
       data[33]=(byte)0x0A;


   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
