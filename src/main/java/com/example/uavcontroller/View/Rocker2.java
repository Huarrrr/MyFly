package com.example.uavcontroller.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/4/12.
 */

public class Rocker2 extends View {
    private Paint paint;
    private float touch_X;
    private float touch_Y;
    private float RX;
    private float RY;
    private Orientation orientation;
    private float R;
    private boolean isOPE = false;
    private String state = "0";

    public Rocker2(Context context) {
        super(context);
    }

    public Rocker2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    public Rocker2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        touch_X = getWidth()/2;
        touch_Y = getHeight()/2;
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        RX =  width/2;//圆心XY坐标
        RY = width/2;
        R = width/2 - (width/2)*0.3f;//大圆半径
        //画大圆
        paint.setColor(0x666666);
        paint.setAlpha(100);
        canvas.drawCircle(RX,RY,R,paint);

        paint.setColor(0xF3B30A01);
        paint.setAlpha(150);
        paint.setStrokeWidth((width/2)*0.3f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(RX,(RY-R)+(width/4)*0.3f,RX,(RY+R)-(width/4)*0.3f,paint);
        paint.reset();
        float r =(width/2)*0.3f-10;
        paint.setColor(0x978e8e);
        paint.setAlpha(255);
        if(isInCircle(RX,RY,touch_X,touch_Y) ) {
            //画小圆
            canvas.drawCircle(RX, touch_Y, r, paint);
        }else {
            double rad = getRad(RX,RY,touch_X,touch_Y);
            getXY(RX,RY,rad);
            canvas.drawCircle(RX, touch_Y, r, paint);
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touch_X = event.getX();
                touch_Y = event.getY();
                if(orientation != null && isOPE){
                    send(touch_X,touch_Y);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                touch_X = event.getX();
                touch_Y = event.getY();
                if(orientation != null && isOPE){
                    send(touch_X,touch_Y);
                }
                break;
            case MotionEvent.ACTION_UP:
                touch_X = getWidth()/2;
                touch_Y = getWidth()/2;
                state = "0";
                if(orientation != null && isOPE){
                    orientation.cancelled();
                }
                break;
        }
        invalidate();
        return true;
    }
    //判断是否在圆内
    private boolean isInCircle(float x1 ,float y1,float x2 ,float y2){

        return  Math.sqrt(Math.pow(x1 - x2,2)+Math.pow(y1 - y2,2)) <= R;
    }
    //两点的弧度
    private double getRad(float px1, float py1, float px2, float py2){
        float x = px2 -px1;//得到两点X的距离
        float y =  py1 -py2; //得到两点Y的距离
        //算出斜边长
        float xie = (float) Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
        //得到这个角度的余弦值（通过三角函数中的定理 ：邻边/斜边=角度余弦值）
        float cosAngle = x/xie;
        //通过反余弦定理获取到其角度的弧度
        float rad = (float) Math.acos(cosAngle);
        //注意：当触屏的位置Y坐标<摇杆的Y坐标我们要取反值-0~-180
        if (py2 < py1) {
            rad = -rad;
        }
        return rad;
    }
    //得到触摸点与圆心所成线段与圆的交点坐标
    private void getXY(float centerX, float centerY,double rad){
        touch_X = (float) (R*Math.cos(rad))+centerX;
        touch_Y =  (float) (R*Math.sin(rad)) + centerY;
    }
    //判断触点方向
    private String orientation(float px2 ,float py2){
        float y =  RY - py2; //得到两点Y的距离

        if(y>0){
            return "top";


        }else if(y<0){
           return "bottom";


        }
        return "0";

    }
    private void send(float px2 ,float py2){
        String s = orientation(px2,py2);
        if(!state.equals(s)){

                switch (s) {
                    case "top":
                        orientation.top();
                        break;
                    case "bottom":
                        orientation.bottom();
                        break;
                }


        }

        state = s;
    }



    public void addOrientationListener(Orientation orientation){
        this.orientation = orientation;
    }

    public interface Orientation{
        void top();
        void bottom();
        void cancelled();


    }
    public void setEnabled(boolean isOPE){
        this.isOPE = isOPE;
    }
   /* //延迟执行
    private boolean delayTo(){
        currentTime =System.currentTimeMillis();
        if(currentTime - startTime > 2000){
            startTime = currentTime;
            return true;
        }
        return false;

    }*/
}
