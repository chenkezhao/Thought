package com.ckz.thought.utils;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by win7 on 2015/11/9.
 * 定时器工具类
 */
public class TimerUtils {
    private Timer timer;

    //消息处理机制
    private Handler myHandler;

    //消息机制的序列号设置
    private int sequence;


    private TimerUtils(){
        super();
    }

    /**
     * 构造函数初始化 Handler 消息处理机制
     * @param handler 消息处理机制对象
     */
    public TimerUtils(Handler handler){
        myHandler = handler;
    }


    /**
     * 提供访问接口，获取 Timer对象
     * @return
     */
    public Timer getTimer(){
        return timer;
    }

    /**
     * 提醒任务
     */
    private class RemindTask extends TimerTask {
        @Override
        public void run() {
            Message message = new Message();
            message.what = sequence;
            myHandler.sendMessage(message);
        }
    }

    /**
     * 设置计时，秒为单位
     * @param seconds1 第一次多久执行
     * @param seconds2 之后每一次多久执行
     * @param number 消息机制触发号
     */
    public void setTimeout(int seconds1,int seconds2,int number) {
        sequence = number;//消息机制序列号的设置
        timer = new Timer();
        //第一次多久触发，之后每次多久触发
        timer.schedule(new RemindTask(), seconds1 * 1000,seconds2 * 1000);
    }
    /**
     * 设置计时，秒为单位
     * @param seconds 第一次多久执行
     * @param number 消息机制触发号
     */
    public void setTimeout(int seconds,int number) {
        sequence = number;//消息机制序列号的设置
        timer = new Timer();
        //第一次多久触发，之后每次多久触发
        timer.schedule(new RemindTask(), seconds * 1000);
    }

    /**
     * 取消计时器任务
     */
    public void clearTimeout() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }
}
