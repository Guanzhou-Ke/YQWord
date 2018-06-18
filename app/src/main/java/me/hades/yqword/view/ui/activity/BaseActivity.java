package me.hades.yqword.view.ui.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.afollestad.materialdialogs.MaterialDialog;

import me.hades.yqword.App;
import me.hades.yqword.R;
import me.hades.yqword.model.DaoSession;
import me.hades.yqword.model.WordDao;

/**
 * Created by hades on 2018/6/9.
 *
 * 基类设计
 * @author hades
 * @version 1.0
 * @see AppCompatActivity
 */

public class BaseActivity extends AppCompatActivity {

    DaoSession daoSession;
    WordDao wordDao;
    Context context;

    private MaterialDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        daoSession = ((App) getApplication()).getDaoSession();
        wordDao = daoSession.getWordDao();
        context = getApplicationContext();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public WordDao getWordDao() {
        return wordDao;
    }

    public void showLoadingDialog(String content) {
        if (mLoadingDialog == null) {
            // 这里注意不能绑定全局
            mLoadingDialog = new MaterialDialog.Builder(this)
                    .title("提示")
                    .widgetColorRes(R.color.colorPrimary)
                    .progress(true, 0)
                    .iconRes(R.mipmap.ic_launcher)
                    .cancelable(false)
                    .build();
        }
        mLoadingDialog.setContent(content);
        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
}
