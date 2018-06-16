package me.hades.yqword;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import me.hades.yqword.model.DaoMaster;
import me.hades.yqword.model.DaoSession;
import me.hades.yqword.preference.ApiPreference;
import me.hades.yqword.utils.CommonValues;
import me.hades.yqword.utils.Config;
import me.hades.yqword.utils.SPUtil;
import me.hades.yqword.utils.WordDatabase;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hades on 2018/6/2.
 */

public class App extends Application implements Application.ActivityLifecycleCallbacks{

    private static final String TAG = App.class.getSimpleName();

    public static Context globalContext = null;

    public static ApiPreference apiPreference;

    /**
     * DEBUG 开关
     */
    public static final Boolean DEBUG = true;

    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    DaoMaster daoMaster;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        if(globalContext == null) {
            globalContext = getApplicationContext();
        }
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:【友盟+】 AppKey
         * 参数3:【友盟+】 Channel
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret
         */
        UMConfigure.init(this, CommonValues.UMengAppkey,
                "Umeng",UMConfigure.DEVICE_TYPE_PHONE,null);

        /**
         * 对数据库进行初始化
         */
        boolean isFirstOpen = !SPUtil.contains(globalContext, CommonValues.FIRST_OPEN);
//        isFirstOpen=false; //for init db , delete this later
        if (isFirstOpen) {
            globalContext.deleteDatabase(CommonValues.DATABASE_NAME);
            new WordDatabase(globalContext).getWritableDatabase();
            helper = new DaoMaster.DevOpenHelper(this, CommonValues.DATABASE_NAME, null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();

            SPUtil.putAndApply(globalContext, CommonValues.FIRST_OPEN, "no");

        } else {

            db = new WordDatabase(globalContext).getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }

        // 初始化配置工具
        Config.setContext(globalContext);

        //初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CommonValues.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiPreference = retrofit.create(ApiPreference.class);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if(DEBUG){

        }
        MobclickAgent.onResume(this);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        MobclickAgent.onPause(this);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    /**
     * 返回数据库Session
     * @return
     */
    public DaoSession getDaoSession() {
        return daoSession;
    }
}
