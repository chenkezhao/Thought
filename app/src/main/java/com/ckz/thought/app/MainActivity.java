package com.ckz.thought.app;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.ckz.thought.R;
import com.ckz.thought.utils.MusicUtils;
import com.ckz.thought.utils.BitmapUtils;

/**
 * 程序的主页面
 * 菜单功能跳转页
 * 功能介绍：
 * 这是主菜单页面，没什么好说的
 */
public class MainActivity extends AppCompatActivity {

    private static Intent backMusic = null;

    //位图处理工具
    private BitmapUtils bitmapUtils;

    private ImageView btn_menu1;
    private ImageView btn_menu2;
    private ImageView btn_menu3;
    private ImageView app_main_mute;
    //音效播放服务
    private MusicUtils musicUtils;

    //统一管理bitmap资源
    private Resources res;
    private Bitmap[] bitmaps;
    private Bitmap[] bitmaps_;

    private final String TAG = MainActivity.class.getSimpleName();
    /**
     * 自定义单击事件监听类
     * 内部类
     */
    private class MainClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.app_main_mute://声音
                    if(musicUtils.getMediaPlayer()!=null){
                        if (musicUtils.getMediaPlayer().isPlaying()){
                            musicUtils.doStop();
                            app_main_mute.setImageBitmap(bitmaps_[3]);
                        }
                    }else{
                        musicUtils.doStart(MainActivity.this, R.raw.back_music, true);
                        app_main_mute.setImageBitmap(bitmaps[3]);
                    }
                    break;
            }
        }
    }

    /**
     * 触摸事件
     */
    private class MyTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {


            if(event.getAction()==MotionEvent.ACTION_DOWN){//按下
                switch (v.getId()){
                    case R.id.app_main_menu_1:
                        btn_menu1.setImageBitmap(bitmaps_[0]);
                        break;
                    case R.id.app_main_menu_2:
                        btn_menu2.setImageBitmap(bitmaps_[1]);
                        break;
                    case R.id.app_main_menu_3:
                        btn_menu3.setImageBitmap(bitmaps_[2]);
                        break;
                }
            }else if(event.getAction()== MotionEvent.ACTION_UP){//弹起
                switch (v.getId()){
                    case R.id.app_main_menu_1:
                        btn_menu1.setImageBitmap(bitmaps[0]);
                        Intent goIntent = new Intent(MainActivity.this,GoActivity.class);
                        startActivity(goIntent);
                        break;
                    case R.id.app_main_menu_2:
                        btn_menu2.setImageBitmap(bitmaps[1]);
                        Intent memoryIntent = new Intent(MainActivity.this,MemoryActivity.class);
                        startActivity(memoryIntent);
                        break;
                    case R.id.app_main_menu_3:
                        btn_menu3.setImageBitmap(bitmaps[2]);
                        Intent reactionIntent = new Intent(MainActivity.this,ReactionActivity.class);
                        startActivity(reactionIntent);
                        break;
                }
            }

            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res = getResources();
        //初始化音效工具类
        musicUtils = new MusicUtils();
        //初始化位图处理工具类
        bitmapUtils = new BitmapUtils();
        //获取资源对象
        btn_menu1 = (ImageView) findViewById(R.id.app_main_menu_1);
        btn_menu2 = (ImageView) findViewById(R.id.app_main_menu_2);
        btn_menu3 = (ImageView) findViewById(R.id.app_main_menu_3);
        app_main_mute = (ImageView) findViewById(R.id.app_main_mute);
        //注册事件
        btn_menu1.setOnTouchListener(new MyTouchListener());
        btn_menu2.setOnTouchListener(new MyTouchListener());
        btn_menu3.setOnTouchListener(new MyTouchListener());
        //背景声音事件注册
        app_main_mute.setOnClickListener(new MainClickListener());
        //加载bitmap资源
        if(bitmaps==null){
            bitmaps = new Bitmap[]{
                    bitmapUtils.compressBitmapFromResource(res, R.drawable.main_menu1,btn_menu1),
                    bitmapUtils.compressBitmapFromResource(res, R.drawable.main_menu2, btn_menu2),
                    bitmapUtils.compressBitmapFromResource(res, R.drawable.main_menu3, btn_menu3),
                    bitmapUtils.compressBitmapFromResource(res, R.drawable.mute, app_main_mute),
            };
        }
        //加载bitmap资源
        if(bitmaps_==null){
            bitmaps_ = new Bitmap[]{
                    bitmapUtils.compressBitmapFromResource(res, R.drawable.main_menu1_1,btn_menu1),
                    bitmapUtils.compressBitmapFromResource(res, R.drawable.main_menu2_1, btn_menu2),
                    bitmapUtils.compressBitmapFromResource(res, R.drawable.main_menu3_1, btn_menu3),
                    bitmapUtils.compressBitmapFromResource(res, R.drawable.mute_1, app_main_mute),
            };
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        musicUtils.doStart(MainActivity.this, R.raw.back_music, true);
        //加载背景图片
        btn_menu1.setImageBitmap(bitmaps[0]);
        btn_menu2.setImageBitmap(bitmaps[1]);
        btn_menu3.setImageBitmap(bitmaps[2]);
        app_main_mute.setImageBitmap(bitmaps[3]);
    }


    @Override
    protected void onStop() {
        super.onStop();
        new Thread(){
            @Override
            public void run() {
                if(musicUtils.getMediaPlayer()!=null){
                    if(!musicUtils.getMediaPlayer().isLooping()){
                        while (musicUtils.getMediaPlayer().isPlaying()){
                        }
                        musicUtils.doStop();
                    }else{
                        musicUtils.doStop();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //回收bitmap占存
        bitmapUtils.recycleBitmaps(bitmaps);
        bitmapUtils.recycleBitmaps(bitmaps_);
    }
}








