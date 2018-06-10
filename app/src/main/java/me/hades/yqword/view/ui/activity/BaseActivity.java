package me.hades.yqword.view.ui.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import me.hades.yqword.App;
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
}
