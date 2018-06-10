package me.hades.yqword.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hades on 2018/6/10.
 */

public class ChineseCheck {
    static String regEx = "[\u4e00-\u9fa5]";
    static Pattern pat = Pattern.compile(regEx);

    public static boolean containChinese(String str) {
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }
}
