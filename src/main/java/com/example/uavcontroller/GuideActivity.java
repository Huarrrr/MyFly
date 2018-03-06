package com.example.uavcontroller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/12.
 */

public class GuideActivity extends AppCompatActivity {
    private ViewPager vp;
    private LinearLayout ll;//小圆点容器
    private ImageView imageView;//小圆点图片
    private ImageView[] imageViews; //小圆点图片数组

    private Button button;//最后一页的按钮

    private List<View> viewList;//引导页图片
    private int[] imageIdArray;//引导页图片资源数组
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.guide_layout);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        button = (Button)findViewById(R.id.guide_button);
        button.setOnClickListener(blistener);
        //加载ViewPager
        initViewPager();
        //加载底部圆点
        initPoint();

    }

    private void initPoint() {
        ll = (LinearLayout)findViewById(R.id.guide_round);
        //根据ViewPager的item数量实例化数组
        imageViews = new ImageView[viewList.size()];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30,30);
        //循环新建底部圆点ImageView，将生成的ImageView保存到数组中
        for(int i=0;i<imageViews.length;i++){
            imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            if(i == 0){
                imageView.setBackgroundResource(R.drawable.round02);
            }else {
                imageView.setBackgroundResource(R.drawable.round01);
            }
            imageViews[i] = imageView;
            ll.addView(imageViews[i]);
        }
    }

    private void initViewPager() {
        vp = (ViewPager)findViewById(R.id.view_pager);
        viewList = new ArrayList<>();
        //实例化图片资源
        imageIdArray = new int[]{R.drawable.guide,R.drawable.guide,R.drawable.guide};
        //获取一个Layout参数，设置为全屏
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //循环创建View并加入到集合中
        for(int i=0;i<imageIdArray.length;i++){
            //new ImageView并设置全屏和图片资源
            ImageView imageview = new ImageView(this);
            imageview.setLayoutParams(params);
            imageview.setBackgroundResource(imageIdArray[i]);
            //将ImageView加入到集合中
            viewList.add(imageview);
        }
        MyPagerApadter apadter = new MyPagerApadter(viewList);
        vp.setAdapter(apadter);

        vp.addOnPageChangeListener(listener);

    }
    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for(int i=0;i<imageIdArray.length;i++){
                if(i == position){
                    imageViews[position].setBackgroundResource(R.drawable.round02);
                }else{
                    imageViews[i].setBackgroundResource(R.drawable.round01);
                }
            }
            if(position == imageIdArray.length-1){

                button.setVisibility(View.VISIBLE);
            }else {
                button.setVisibility(View.GONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private View.OnClickListener blistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isFrist();
            Intent intent = new Intent(GuideActivity.this,Controller.class);
            startActivity(intent);
            finish();
        }
    };
    public void isFrist(){
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();//获取SharedPreferences.Editor对象
        editor.putBoolean("isFrist",false);//添加一个布尔型的数据
        editor.commit();//提交数据
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(listener!=null){
            vp.removeOnPageChangeListener(listener);
        }
    }
}
