package com.example.uavcontroller.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.uavcontroller.R;

/**
 * Created by Administrator on 2017/4/13.
 */

public class Speedometer extends View {

    private Bitmap speedometerbackgroundBitmap;
    private Bitmap speedometerflagBitmap;
    private Paint paint;
    private Paint paint1;
    private Paint paint2;
    private float X;
    private float Y;
    private float r;
    private int increase;
    private int accelerator = 0;
    private boolean iscancelled;
    private onSpeedometerChanged speedometerChanged;

    public Speedometer(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Speedometer);
        int speedometer_background = typedArray.getResourceId(R.styleable.Speedometer_speedometer_background,0);
        int speedometer_flag = typedArray.getResourceId(R.styleable.Speedometer_speedometer_flag,0);
        setspeedometerflagResource(speedometer_flag);
        setspeedometerbackgroundResource(speedometer_background);
        init();
        initData();
    }

    private void initData() {
       r = speedometerflagBitmap.getWidth();//指针旋转半径
    }

    private void init() {
        paint = new Paint();
        paint1  = new Paint();
        paint2 = new Paint();//画指针
        paint2.setAntiAlias(true);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(speedometerbackgroundBitmap.getWidth(),speedometerbackgroundBitmap.getHeight());

    }

    @Override
    protected void onDraw(Canvas canvas) {


        canvas.drawBitmap(speedometerbackgroundBitmap,0,0,paint);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(0xffffff);
        paint.setAlpha(250);
        canvas.drawRect((speedometerbackgroundBitmap.getWidth()*3)/8,(speedometerbackgroundBitmap.getWidth()*5)/18,(5*speedometerbackgroundBitmap.getWidth())/8
        ,(speedometerbackgroundBitmap.getWidth()*7)/18,paint);
        paint.reset();
        paint.setColor(0x000000);
        paint.setAlpha(255);
        paint.setAntiAlias(true);
        paint.setTextSize(speedometerbackgroundBitmap.getWidth()/12);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(accelerator+"",speedometerbackgroundBitmap.getWidth()/2,speedometerbackgroundBitmap.getWidth()*9/24,paint);
        paint.reset();
        canvas.save();
        canvas.translate(speedometerbackgroundBitmap.getWidth()/2,speedometerbackgroundBitmap.getWidth()/2);
        canvas.rotate(180+(accelerator*0.18f));
        paint1.setColor(0x978e8e);
        paint1.setAlpha(200);
        canvas.drawCircle(0,0,speedometerflagBitmap.getHeight()/2,paint1);
        paint1.setColor(0xfb0606);
        paint1.setAlpha(200);
        canvas.drawCircle(0,0,speedometerflagBitmap.getHeight()/4,paint1);

        paint2.setColor(0xfb0606);
        paint2.setStrokeWidth(4);
        paint2.setAlpha(200);

        canvas.drawLine(0,0,speedometerflagBitmap.getWidth()
                ,0,paint2);

        canvas.restore();
        super.onDraw(canvas);
    }
    private void setspeedometerbackgroundResource(int speedometer_background){
        speedometerbackgroundBitmap = BitmapFactory.decodeResource(getResources(),speedometer_background);
    }
    private void setspeedometerflagResource(int speedometer_flag){
        speedometerflagBitmap = BitmapFactory.decodeResource(getResources(),speedometer_flag);
    }
    public void cancelled(boolean cancelled){
        this.iscancelled = cancelled;
    }
    public void setSpeedometer(int increase){
        this.increase = increase;

    }
    public void reset(){
        accelerator = 0;
        invalidate();
    }
    public void startThread(){
        new Thread(runnable).start();
    }
    private  Runnable runnable = new Runnable(){

        @Override
        public void run() {
           // synchronized (this) {
                while (true) {

                    switch (increase) {
                        case 1:
                            accelerator += 1;
                            if (accelerator >= 700) {//油门最大值
                                accelerator = 700;
                            }
                            break;
                        case 2:
                            accelerator -= 1;
                            if (accelerator <= 0) {//油门最小值
                                accelerator = 0;
                            }
                            break;
                        case 0:
                            break;
                    }

                    if(speedometerChanged != null) {
                        speedometerChanged.onChanged(accelerator);
                    }
                   // Log.e("accelerator", Thread.currentThread().getName() + "  " + accelerator);
                    postInvalidate();
                    if(iscancelled) break;
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
       // }
    };
    public void setOnSpeedometerChanged(onSpeedometerChanged speedometerChanged){
        this.speedometerChanged = speedometerChanged;
    }

    public interface onSpeedometerChanged{
        void onChanged(int accelerator);
    }





}
