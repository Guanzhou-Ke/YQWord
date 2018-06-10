package me.hades.yqword.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by hades on 2018/6/9.
 * 数据库初始化帮助类
 * @author hades
 * @version 1.0
 * @see android.database.sqlite.SQLiteOpenHelper
 */

public class WordDatabase extends SQLiteAssetHelper{

    private static final int DATABASE_VERSION = 4;
    Context context;

    public WordDatabase(Context context) {
        super(context, CommonValues.DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        // 单词库的版本是2
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE word ADD COLUMN LAST_LEARN_TIME INTEGER DEFAULT 0");
        }
    }
}
