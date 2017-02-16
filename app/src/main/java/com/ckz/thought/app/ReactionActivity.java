package com.ckz.thought.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ckz.thought.R;
import com.tyrantgit.explosionfield.ExplosionField;

/**
 *
 * Created by kaiser on 2015/10/27.
 */
public class ReactionActivity extends BaseActivity{
    private ExplosionField mExplosionField;

    //消息处理机制
    private final Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ExplosionField.LIST_ANIMATIONEND:
                    View root = findViewById(R.id.rl_root);
                    reset(root);
                    addListener(root);
                    mExplosionField.clear();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction);
        setTitle("超级反应");
        mExplosionField = ExplosionField.attach2Window(this,myHandler);
        addListener(findViewById(R.id.rl_root));
    }


    private void addListener(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                addListener(parent.getChildAt(i));
            }
        } else {
            root.setClickable(true);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExplosionField.explode(v);
                    v.setOnClickListener(null);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_setting) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reset(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                reset(parent.getChildAt(i));
            }
        } else {
            root.setScaleX(1);
            root.setScaleY(1);
            root.setAlpha(1);
        }
    }
}
