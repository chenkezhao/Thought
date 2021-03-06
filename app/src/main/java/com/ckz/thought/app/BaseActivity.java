package com.ckz.thought.app;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ckz.thought.MyApplication;
import com.ckz.thought.utils.MessageUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;


/**
 *
 * Created by Administrator on 2017/1/8.
 */

public class BaseActivity extends AppCompatActivity{

    final private int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 689;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mActionBar = getSupportActionBar();
        if(mActionBar!=null){
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(false);
        }
        //启动时检查权限
        if(android.os.Build.VERSION.SDK_INT>=23){
            //checkPermission();
        }
    }

    public void setTitle(String title){
        if(mActionBar!=null){
            mActionBar.setTitle(title);
        }
    }

    public void setSubtitle(String subTitle){
        if(mActionBar!=null){
            mActionBar.setSubtitle(subTitle);
        }
    }

    public void setMainHomeUp(){
        if(mActionBar!=null){
            mActionBar.setHomeButtonEnabled(false);
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setDisplayShowHomeEnabled(true);
        }
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


    @TargetApi(23)
    private void checkPermission(){
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                MessageUtils.getInstance().showAlertDialog(this, "系统提示", "如果不赋予程序任何权限，程序将结束运行！", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
                    }
                });
                return;
            }
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    //queryTradeHistory(mCode,mDate);
                    MessageUtils.getInstance().showSnackbar(MyApplication.getInstance().getRootView(BaseActivity.this),"受权成功，尽情享用！");
                } else {
                    // Permission Denied
                    MessageUtils.getInstance().closeProgressDialog();
                    MyApplication.getInstance().exit();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getName()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    private String getName(){
        return getClass().getSimpleName();
    }
}
