package com.ckz.thought.app;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ckz.thought.R;
import com.ckz.thought.utils.MessageUtils;

/**
 *
 * Created by Administrator on 2017/2/7.
 */

public class SettingActivity extends PreferenceActivity {
	private EditTextPreference etp_timeout;
	private EditTextPreference etp_complexity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
		Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
		root.addView(bar, 0); // insert at top
		bar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		setTitle("服务器设置");
		addPreferencesFromResource(R.xml.setting_preference);


		// 1 获取超时时间Preference
		etp_timeout = (EditTextPreference) findPreference("et_arithmetic_timeout");
		etp_timeout.setSummary(etp_timeout.getText());
		// 2 设置超时时间Preference变更监听器
		etp_timeout.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
											  Object newValue) {

				// 根据新的 Preference 值设置Summary
				if("0".equals(newValue+"") || "".equals(newValue+"")){
					Toast.makeText(SettingActivity.this,"不能设置为0或空",Toast.LENGTH_SHORT).show();
					return false;
				}else {
					etp_timeout.setSummary(newValue+"");
				}
				return true;
			}
		});
		//算术复杂性
		etp_complexity = (EditTextPreference) findPreference("et_arithmetic_complexity");
		etp_complexity.setSummary(etp_complexity.getText());
		etp_complexity.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
											  Object newValue) {
				if("0".equals(newValue+"") || "".equals(newValue+"")){
					Toast.makeText(SettingActivity.this,"不能设置为0或空",Toast.LENGTH_SHORT).show();
					return false;
				}else {
					etp_complexity.setSummary(newValue+"");
				}
				return true;
			}
		});
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
