package me.hades.yqword.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import me.hades.yqword.R;

/**
 * Created by hades on 2018/6/13.
 */

public class ADFilterTool {
    public static boolean hasAd(Context context, String url) {
        Resources res = context.getResources();
        String[] adUrls = res.getStringArray(R.array.adBlockUrl);
        for (String adUrl : adUrls) {
            if (url.contains(adUrl)) {
                Log.e("ADFilterTool", "发现广告");
                return true;
            }
        }
        return false;
    }
}
