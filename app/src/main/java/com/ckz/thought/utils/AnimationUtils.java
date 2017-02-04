package com.ckz.thought.utils;

import android.content.Context;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by kaiser on 2015/11/13.
 * 陈科肇
 * 动画工具类
 */
public class AnimationUtils {

    private TranslateAnimation translateAnimation;//移动

    private Context context;

    private AnimationUtils(Context context) {
        this.context = context;
    }


    /**
     * 移动
     * @param fromXDelta
     * @param toXDelta
     * @param fromYDelta
     * @param toYDelta
     * @return
     */
    public TranslateAnimation setTranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
        TranslateAnimation ta = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        ta.setDuration(3000);//持续的时间
        ta.setRepeatCount(TranslateAnimation.INFINITE);//无限循环
        ta.setInterpolator(context, android.R.anim.cycle_interpolator);
        return ta;
    }


}
