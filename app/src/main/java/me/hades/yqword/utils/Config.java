package me.hades.yqword.utils;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.hades.yqword.R;

/**
 * Created by hades on 2018/6/9.
 */

public class Config {

    private static Context context;

    /**
     * @return 今日剩余需学单词数目
     */
    public static int getTodayShouldLearn() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String haveLearnSp = simpleDateFormat.format(new Date()) + "learn";
        int haveLearn = (int) SPUtil.get(context, haveLearnSp, 0);
        SPUtil.get(context, haveLearnSp, 0);
        int todayRemain = getPlanShouldLearn() - haveLearn;
        if (todayRemain < 0) {
            todayRemain = 0;
        }
        return todayRemain;
    }

    /**
     * @return 计划今日应学单词总数
     */
    public static int getPlanShouldLearn() {
        String planLearn = (String) SPUtil.get(context, CommonValues.PLAN_LEARN, "99");
        if (planLearn == null || planLearn.isEmpty()) {
            return 10;
        }
        return Integer.valueOf(planLearn);
    }

    public static void setPlanShouldLearn(String planShouldLearn) {
        SPUtil.putAndApply(context, CommonValues.PLAN_LEARN, planShouldLearn);
    }

    public static Date getExamTime() {
        String examTimeStr = (String) SPUtil.get(context, CommonValues.EXAM_DATE, context.getString(R.string.defaultTestDate));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date examDate = simpleDateFormat.parse(examTimeStr);
            return examDate;
        } catch (Exception e) {
            Log.e("should never go there", e.toString());
            return null;
        }
    }


    public static String getPieWord() {
        String pieWord = (String) SPUtil.get(context, CommonValues.PIE_WORD, "进度");
        return pieWord;
    }

    /**
     * call when init application
     *
     * @param initContext
     */
    public static void setContext(Context initContext) {
        context = initContext.getApplicationContext();
    }

    public static boolean shouldSearchTipShow() {
        return (boolean) SPUtil.get(context, CommonValues.searchTipShow, true);
    }

    public static void setSearchTipShow(boolean searchTipShow) {
        SPUtil.putAndApply(context, CommonValues.searchTipShow, searchTipShow);
    }


    public static boolean shouldShowGuide() {
        return (boolean) SPUtil.get(context, CommonValues.shouldShowGuide, true);
    }

    public static void dontShowGuide() {
        SPUtil.putAndApply(context, CommonValues.shouldShowGuide, false);
    }

    public static boolean getAutoDisplay() {
        return (boolean) SPUtil.get(context, CommonValues.AUTO_DISPLAY, false);
    }

    public static void setAutoDisplay(boolean autoDisplay) {
        SPUtil.putAndApply(context, CommonValues.AUTO_DISPLAY, autoDisplay);
    }

    public static String getGUID() {
        return (String) SPUtil.get(context, CommonValues.GUID, CommonValues.DEFAULT_GUID);
    }

    public static void setGuid(String guid) {
        SPUtil.putAndApply(context, CommonValues.GUID, guid);
    }

    public static boolean coreModeIsOn() {
        return (boolean) SPUtil.get(context, context.getString(R.string.core_mode), false);
    }

    public static boolean easyModeIsOn() {
        return (boolean) SPUtil.get(context, context.getString(R.string.hide_easy), false);
    }

    public static boolean getShowChinese() {
        return (boolean) SPUtil.get(context, CommonValues.SHOW_CHINESE_LIST, true);
    }

    public static void setShowChinese(boolean showChinese) {
        SPUtil.putAndApply(context, CommonValues.SHOW_CHINESE_LIST, showChinese);
    }

    /**
     *
     * @param examDate calendar
     */
    public static void setExamTime(Calendar examDate) {
        int year=examDate.get(Calendar.YEAR);
        int month=examDate.get(Calendar.MONTH);
        month++;//应该是12月,但是calendar month最大是11.
        int day=examDate.get(Calendar.DAY_OF_MONTH);
        String formatExamDate=year+"-"+month+"-"+day;
        SPUtil.putAndApply(context,CommonValues.EXAM_DATE,formatExamDate);
    }

    //自动调整开考时间
    public static void addExamDate() {
        setExamDate(Calendar.getInstance().get(Calendar.YEAR));
    }
    public static void setExamDate(int year){

        Calendar examDate= Calendar.getInstance();
        int month=examDate.getActualMaximum(Calendar.MONTH);
        int day=29; //考试时间是周六,必然是12月2*的一天.
        examDate.set(year,month,day);
        while (true) {
            int dayOfWeek = examDate.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek==Calendar.SATURDAY) {
                break;
            }else {
                day--;
                examDate.set(Calendar.DAY_OF_MONTH,day);
            }
        }
        //当前是12月并且已经过去今年的考研时间
        if (Calendar.getInstance().after(examDate)) {
            year++;
            setExamDate(year);
        } else {
            Config.setExamTime(examDate);
        }

    }
}

