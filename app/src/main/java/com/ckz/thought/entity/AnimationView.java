package com.ckz.thought.entity;

import android.view.View;

/**
 * Created by kaiser on 2015/11/13.
 * 属性动画
 * 动画View
 * 该包装类用于包装view的x,y坐标
 * 你可以通过setxxx等方法去设置View的坐标
 */
public class AnimationView {
    private View view;
    private int width;
    private int height;

    public AnimationView(View view) {
        this.view = view;
    }

    public int getWidth(){
        return view.getLayoutParams().width;

    }


    public void setWidth(int width){
        this.width = width;
        view.getLayoutParams().width= width;
        view.requestLayout();//重新加载布局
    }

    public int getHeight(){
        return view.getLayoutParams().height;
    }

    public void setHeight(int height){
        this.height = height;
        view.getLayoutParams().width= height;
        view.requestLayout();
    }
}
