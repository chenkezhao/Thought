package com.ckz.thought.app;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.ckz.thought.R;

import tyrantgit.explosionfield.ExplosionField;

/**
 *
 * Created by kaiser on 2015/10/27.
 */
public class ReactionActivity extends BaseActivity{
    private ExplosionField mExplosionField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction);
        setTitle("超级反应");
        mExplosionField = ExplosionField.attach2Window(this);
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
