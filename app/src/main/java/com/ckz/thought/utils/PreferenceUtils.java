package com.ckz.thought.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ckz.thought.MyApplication;

/**
 *
 * Created by Administrator on 2017/2/9.
 */

public class PreferenceUtils {
    private static volatile PreferenceUtils instance;
    private SharedPreferences prefs = MyApplication.getInstance().getSharedPreferences(
            MyApplication.getInstance().getPackageName() + "_preferences",
            Context.MODE_PRIVATE);
    private PreferenceUtils(){
    }

    public static PreferenceUtils getInstance(){
        if(instance==null){
            synchronized (PreferenceUtils.class){
                if(instance==null){
                    instance = new PreferenceUtils();
                }
            }
        }
        return instance;
    }


    public boolean isPlayMusic(){
        boolean isPlay = true;
        if (prefs != null) {
            isPlay = prefs.getBoolean("cb_arithmetic_music", false);
        }
        return isPlay;
    }

    public int getArithmeticTimeout(){
        int timeout =5;
        if(prefs!=null){
            timeout = Integer.parseInt(prefs.getString("et_arithmetic_timeout","5"));
        }
        return timeout;
    }
    public int getArithmeticComplexity(){
        int number =1;
        if(prefs!=null){
            number = Integer.parseInt(prefs.getString("et_arithmetic_complexity","1"));
        }
        return number;
    }

    public boolean isArithmeticSerialTimeouts(){
        boolean b = true;
        if (prefs != null) {
            b = prefs.getBoolean("cb_arithmetic_serial_timeouts", false);
        }
        return b;
    }
}
