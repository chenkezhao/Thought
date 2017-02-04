package com.ckz.thought.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.ckz.thought.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 程序的主页面
 * 菜单功能跳转页
 * 功能介绍：
 * 这是主菜单页面，没什么好说的
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.tv_copyrightInfo)
    private TextView tv_copyrightInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainHomeUp();
        initView();
    }

    private void initView(){
        tv_copyrightInfo.setText(Html.fromHtml("<div style=\"padding: 24px;color: #FFFFFF\">\n" +
                "Copyright © &nbsp;&nbsp;2017&nbsp; 陈科肇 ALL RIGHT RESERVED<br>\n" +
                "联系方式：<font class=\"email\">310771881@qq.com</font>\n" +
                "</div>"));
    }


    /**
     * 跳转到算法页面
     * @param v
     */
    @Event(R.id.btn_arithmetic)
    private void goToArithmeticClick(View view){
        Intent intent = new Intent(MainActivity.this,GoActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到超强记忆力页面
     * @param view
     */
    @Event(R.id.btn_memory)
    private void goToMemoryClick(View view){
        Intent intent = new Intent(MainActivity.this,MemoryActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到超级反应页面
     * @param view
     */
    @Event(R.id.btn_reaction)
    private void goToReactionClick(View view){
        Intent intent = new Intent(MainActivity.this,ReactionActivity.class);
        startActivity(intent);
    }
}








