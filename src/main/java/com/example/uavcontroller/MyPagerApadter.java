package com.example.uavcontroller;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/3/12.
 */

public class MyPagerApadter extends PagerAdapter {
    private List<View> views;
    public MyPagerApadter(List<View> views){
        this.views = views;
    }
     //返回页面个数
    @Override
    public int getCount() {
        if(views != null){
            return views.size();
        }
        return 0;
    }
    //判断对象是否生成界面
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 初始化position位置的界面
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    //删除ViewPager的item
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
