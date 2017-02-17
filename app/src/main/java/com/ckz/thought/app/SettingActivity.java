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

/**
 *
 * Created by Administrator on 2017/2/7.
 */

public class SettingActivity extends PreferenceActivity {
	private EditTextPreference etp_timeout;
	private EditTextPreference etp_complexity;
	private EditTextPreference etp_mem_timeout;
	private EditTextPreference etp_mem_lenght;
	private EditTextPreference etp_mem_preview;
	private EditTextPreference etp_rea_lenght;

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


		etp_mem_timeout = (EditTextPreference) findPreference("et_memory_timeout");
		etp_mem_timeout.setSummary(etp_mem_timeout.getText());
		etp_mem_timeout.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
											  Object newValue) {

				if("0".equals(newValue+"") || "".equals(newValue+"")){
					Toast.makeText(SettingActivity.this,"不能设置为0或空",Toast.LENGTH_SHORT).show();
					return false;
				}else {
					etp_mem_timeout.setSummary(newValue+"");
				}
				return true;
			}
		});
		etp_mem_lenght = (EditTextPreference) findPreference("et_memory_length");
		etp_mem_lenght.setSummary(etp_mem_lenght.getText());
		etp_mem_lenght.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
											  Object newValue) {
				if("0".equals(newValue+"") || "".equals(newValue+"")){
					Toast.makeText(SettingActivity.this,"不能设置为0或空",Toast.LENGTH_SHORT).show();
					return false;
				}else {
					int len = Integer.parseInt(newValue+"");
					if(len>0 && len<=10){
						etp_mem_lenght.setSummary(newValue+"");
					}else{
						Toast.makeText(SettingActivity.this,"超出范围（值:1-10）",Toast.LENGTH_SHORT).show();
						return false;
					}
				}
				return true;
			}
		});
		etp_mem_preview = (EditTextPreference) findPreference("et_memory_preview");
		etp_mem_preview.setSummary(etp_mem_preview.getText());
		etp_mem_preview.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
											  Object newValue) {

				if("0".equals(newValue+"") || "".equals(newValue+"")){
					Toast.makeText(SettingActivity.this,"不能设置为0或空",Toast.LENGTH_SHORT).show();
					return false;
				}else {
					etp_mem_preview.setSummary(newValue+"");
				}
				return true;
			}
		});




		etp_rea_lenght = (EditTextPreference) findPreference("et_reaction_length");
		etp_rea_lenght.setSummary(etp_rea_lenght.getText());
		etp_rea_lenght.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
											  Object newValue) {
				if("0".equals(newValue+"") || "".equals(newValue+"")){
					Toast.makeText(SettingActivity.this,"不能设置为0或空",Toast.LENGTH_SHORT).show();
					return false;
				}else {
					int len = Integer.parseInt(newValue+"");
					if(len>0 && len<=9){
						etp_rea_lenght.setSummary(newValue+"");
					}else{
						Toast.makeText(SettingActivity.this,"超出范围（值:1-9）",Toast.LENGTH_SHORT).show();
						return false;
					}
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
