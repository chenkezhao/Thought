package com.ckz.thought.app;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.ckz.thought.R;

/**
 * Created by kaiser on 2015/10/27.
 * 菜单三
 * 提升你脑子的记忆力
 */
public class ReactionActivity extends AppCompatActivity{

    private ImageView im;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//通过Action Bar图标进行导航


        ImageView imgShow = (ImageView) findViewById(R.id.ivxx);

        //属性动画
        /*//同步动画设计
        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("translationX", 0, 360F);
        PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("translationY", 0, 360F);
        PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("rotation", 0, 360F);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(imgShow, p1, p2, p3);
        objectAnimator.setDuration(3000);//持续时间
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);//次数
        objectAnimator.start();*/

        //通过AnimatiorSet来设计同步执行的多个属性动画
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imgShow, "translationX", 0F, 360F);//X轴平移旋转
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(imgShow, "translationY", 0F, 360F);//Y轴平移旋转
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(imgShow, "rotation", 0F, 360F);//360度旋转
        AnimatorSet set = new AnimatorSet();
        //set.playSequentially(animator1, animator2, animator3);//分步执行
        //set.playTogether(animator1, animator2, animator3);//同步执行
        //属性动画的执行顺序控制
        // 先同步执行动画animator2和animator3,然后再执行animator1
        set.play(animator3).with(animator1);
        set.play(animator2).after(animator3);
        set.setDuration(5000);
        set.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reaction_activity, menu);
        SupportMenuItem searchItem = (SupportMenuItem)menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        // 配置SearchView的属性
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                Toast.makeText(this, "你点击了“保存”按键！", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_delete:
                Toast.makeText(this, "你点击了“删除”按键！", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_call:
                Toast.makeText(this, "你点击了“呼叫”按键！", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}
