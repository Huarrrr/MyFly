package com.example.uavcontroller.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.uavcontroller.R;


/**
 * Created by Administrator on 2017/3/29.
 */

public class SwitchView extends View {
    private Bitmap switchBackgroundBitmap;
    private Bitmap slideBitmap;
    private boolean switchState = false;
    private Paint paint;
    private boolean isTouchMode =false;
    private float currentX;
    private OnSwitchStateListener mlistener;




    /**
     * 在布局文件中，添加该对象，并且设置了style属性，则调用该方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public SwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       /* eclipse 可以用以下方法 android studio 不能用
       String namespace ="http://schemas.android.com/apk/com.neusoft.myswitch";
        int switchbackgroundResourceValue = attrs.getAttributeResourceValue(namespace,"witch_background",-1);
        setSwitchBackgroundResource(switchbackgroundResourceValue);
        int slide_buttonResourceValue = attrs.getAttributeResourceValue(namespace, "slide_button", -1);
        setSlideButtonResource(slide_buttonResourceValue);
        switchState = attrs.getAttributeBooleanValue(namespace, "switch_state", false);*/


        /*TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.SwitchView);
        int switchbackgroundResourceValue = typedArray.getResourceId(R.styleable.SwitchView_switch_background,-1);

        setSwitchBackgroundResource(switchbackgroundResourceValue);
        int slide_buttonResourceValue =typedArray.getResourceId(R.styleable.SwitchView_slide_button,-1);
        switchState = typedArray.getBoolean(R.styleable.SwitchView_switch_state,false);
        setSwitchBackgroundResource(switchbackgroundResourceValue);
        setSlideButtonResource(slide_buttonResourceValue);
        typedArray.recycle();
        init();*/

    }



    /**
     * 在布局文件中，添加该对象，则调用该方法
     * @param context
     * @param attrs
     */
    public SwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchView);
        int switchbackgroundResourceValue = typedArray.getResourceId(R.styleable.SwitchView_switch_background,-1);

        setSwitchBackgroundResource(switchbackgroundResourceValue);
        int slide_buttonResourceValue =typedArray.getResourceId(R.styleable.SwitchView_slide_button,-1);
        switchState = typedArray.getBoolean(R.styleable.SwitchView_switch_state,false);
        setSwitchBackgroundResource(switchbackgroundResourceValue);
        setSlideButtonResource(slide_buttonResourceValue);
        typedArray.recycle();

        init();

    }



    /**
     * 在布局文件中，添加该对象，则调用该方法
     * @param
     */
    public SwitchView(Context context) {
        super(context);
     // init();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measurewidth(widthMeasureSpec),measureheight(heightMeasureSpec));
    }
    //自定义控件宽度
    private int measurewidth(int width){
        int mwidth = 0;
        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(width);
        int specSize = MeasureSpec.getSize(width);
        if(specMode == MeasureSpec.EXACTLY){
            mwidth = specSize;
        }else {
            int imagewidth = switchBackgroundBitmap.getWidth();
            if(specMode == MeasureSpec.AT_MOST){
                 mwidth = Math.min(imagewidth,specSize);
            }
        }
        return mwidth;
    }
    //自定义控件高度
    private int measureheight(int height){
        int mheight = 0;
        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(height);
        int specSize = MeasureSpec.getSize(height);
        if(specMode == MeasureSpec.EXACTLY){
            mheight = specSize;
        }else {
            int imageheight = switchBackgroundBitmap.getHeight();
            if(specMode == MeasureSpec.AT_MOST){
                mheight = Math.min(imageheight,specSize);
            }
        }
        return mheight;
    }
    //初始化对象
    private void  init(){
        paint = new Paint();//画笔
        paint.setAntiAlias(true);
            }
    //设置控件的大小
    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //setMeasuredDimension(switchBackgroundBitmap.getWidth(),switchBackgroundBitmap.getHeight());
       // setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }*/
    //把外部 传进来的图片，转换成一个图片相关的图片对象
    public void setSwitchBackgroundResource(int switchBackgroundResource){
          switchBackgroundBitmap = BitmapFactory.decodeResource(getResources(),switchBackgroundResource);
    }
    public void setSlideButtonResource(int slideButton){
        slideBitmap = BitmapFactory.decodeResource(getResources(),slideButton);
    }

    //开关状态
    public void setSwitchState(boolean switchState){
        this.switchState = switchState;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*Log.e("onDraw Canvas","height" +canvas.getHeight()+"\twidth"+canvas.getWidth());
        Log.e("onDraw view","height" +this.getHeight()+"\twidth"+this.getWidth());*/
         scaleBitmap(canvas);
        canvas.drawBitmap(switchBackgroundBitmap,0,0,paint);//画背景图片
        if(isTouchMode) {
            int newLeft = (int) currentX;
                if (newLeft < 0) {
                    newLeft = 0;
                } else if (newLeft > switchBackgroundBitmap.getWidth() - slideBitmap.getWidth()) {
                    newLeft = switchBackgroundBitmap.getWidth() - slideBitmap.getWidth();
                }
                canvas.drawBitmap(slideBitmap, newLeft, 0, paint);

        }else {
            if (switchState) {
                float left = switchBackgroundBitmap.getWidth() - slideBitmap.getWidth();
                canvas.drawBitmap(slideBitmap, left, 0, paint);
            } else {
                canvas.drawBitmap(slideBitmap, 0, 0, paint);
            }
        }

    }

    private void scaleBitmap(Canvas canvas) {
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) canvas.getWidth() / switchBackgroundBitmap.getWidth());
        float scaleHeight = ((float) canvas.getHeight()) / switchBackgroundBitmap.getHeight();
        matrix.postScale(scaleWidth,scaleHeight);
        switchBackgroundBitmap = Bitmap.createBitmap(switchBackgroundBitmap,0,0,switchBackgroundBitmap.getWidth(),switchBackgroundBitmap.getHeight(),matrix,false);
        slideBitmap = Bitmap.createBitmap(slideBitmap,0,0,slideBitmap.getWidth(),slideBitmap.getHeight(),matrix,false);
        /*if(!switchBackgroundBitmap.isRecycled()){
            switchBackgroundBitmap.recycle();
        }*/

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTouchMode = true;
                break;
            case MotionEvent.ACTION_MOVE:
                isTouchMode =true;
                currentX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                isTouchMode = false;
                float current = event.getX();
                boolean state = switchBackgroundBitmap.getWidth()/2.0f < current;
                if(state != switchState && mlistener != null){
                    mlistener.onSwitchStateUpdate(state);
                }
                switchState =state;
                break;
        }
        invalidate();
        return true;
    }
    public void setOnSwitchStateListener(OnSwitchStateListener listener){
        this.mlistener = listener;
    }

    //定义一个开关状态的监听接口，用来监听开关的状态
    public interface OnSwitchStateListener{
         void onSwitchStateUpdate(boolean switchState);
    }
}
