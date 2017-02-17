package com.ckz.thought.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ckz.thought.MyApplication;
import com.ckz.thought.R;
import com.ckz.thought.utils.PreferenceUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tyrantgit.explosionfield.ExplosionField;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * Created by kaiser on 2015/10/27.
 */
public class ReactionActivity extends BaseActivity {
	private ExplosionField		mExplosionField;
	private AutoRelativeLayout	rl_root;
	private LayoutInflater inflate;
	// 消息处理机制
	private final Handler		myHandler	= new Handler() {
												public void handleMessage(Message msg) {
													switch (msg.what) {
														case ExplosionField.LIST_ANIMATIONEND:
															//View root = findViewById(R.id.rl_root);
															//reset(rl_root);
															rl_root.removeAllViews();
															resetData();
															initNewSimpleDraweeViewInLayout();
															addListener(rl_root);
															mExplosionField.clear();
															break;
														default:
															break;
													}
												}
											};
	private Integer[] tDrawIds;
	private List<Integer> tops;
	private List<Integer> lefts;
	private List<Integer> drawIds;
	private int number=0;
	private int btnLenght = 270;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reaction);
		initView();
		setTitle("超级反应");
		mExplosionField = ExplosionField.attach2Window(this,myHandler);
		addListener(rl_root);
	}

	private void initView() {
		rl_root = (AutoRelativeLayout) findViewById(R.id.rl_root);
		inflate = getLayoutInflater();
		initData();
		initNewSimpleDraweeViewInLayout();
	}

	private void initData(){
		tDrawIds =new Integer[]{
				R.mipmap.btn_gray,
				R.mipmap.btn_red,
				R.mipmap.btn_green,
				R.mipmap.btn_khaki,
				R.mipmap.btn_violet,
				R.mipmap.btn_blue_green,
				R.mipmap.btn_white,
				R.mipmap.btn_yellow,
				R.mipmap.btn_watchet
		};
		number = PreferenceUtils.getInstance().getReactionLength();
		resetData();
	}
	private void resetData(){
		tops = generateRandomArray(number,btnLenght/2, MyApplication.getInstance().getScreenHeight()-btnLenght/2);
		lefts = generateRandomArray(number,btnLenght/2, MyApplication.getInstance().getScreenWidth()-btnLenght/2);
		drawIds = Arrays.asList(tDrawIds);
		Collections.shuffle(drawIds);
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

	private void initNewSimpleDraweeViewInLayout() {
		for(int i=0;i<number;i++){
			SimpleDraweeView mSimpleDraweeView = (SimpleDraweeView)inflate.inflate(R.layout.view_simpledraweeview,rl_root,false);
			mSimpleDraweeView.setImageURI("res:///"+drawIds.get(i));
			rl_root.addView(mSimpleDraweeView);
			RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams) mSimpleDraweeView.getLayoutParams();
			parms.leftMargin = lefts.get(i);
			parms.topMargin = tops.get(i);
			mSimpleDraweeView.setLayoutParams(parms);
		}
	}


	/**
	 * 随机产生指定的范围不重复的集合
	 * @param size 指定集合大小
	 * @param min 随机数最小
	 * @param max 随机数最大
     * @return
     */
	public List<Integer> generateRandomArray(int size,int min,int max){
		Set<Integer> set = new LinkedHashSet<Integer>(); //集合是没有重复的值,LinkedHashSet是有顺序不重复集合,HashSet则为无顺序不重复集合
		Integer num = size;
		Random ran = new Random();
		while(set.size() < num){
			Integer tmp = ran.nextInt(max-min)+min; //min到max之间随机选一个数
			set.add(tmp);//直接加入，当有重复值时，不会往里加入，直到set的长度为52才结束
		}
		return  Collections.list(Collections.enumeration(set));
	}
}
