package me.hades.yqword.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hades on 2018/6/11.
 * 封装SoundPool的工具类。传入单词名字，context即可
 * @author hades
 * @version 1.0
 * @see android.media.SoundPool
 */

public class SoundPlayUtil{

    private static final String TAG = SoundPlayUtil.class.getSimpleName();

    private static final String SOUND_FOLDER = "word_sound";

    // 音频池容量
    private static final int MAX_SOUND = 100;

    // 当前容量
    private static int CURRENT_SOUND = 0;

    private static boolean inited = false;

    private static AssetManager mAssets;

    private static SoundPool mSoundPool;

    private static Map<String, Integer> soundMap;

    public static void play(Context context, String wordName) {
        if (!inited) {
            init(context);
            inited = true;
        }
        if (soundMap.containsKey(wordName)) {
            // 已经在缓冲池中
            int id = soundMap.get(wordName);
            mSoundPool.play(id, 1, 1, 1, 0, 1);
        } else {
            try {
                AssetFileDescriptor afd = getFileDescriptor(wordName);
                int id = mSoundPool.load(afd, 1);
                soundMap.put(wordName, id);
                CURRENT_SOUND++;
                Log.i(TAG, "sound at: " + afd.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static AssetFileDescriptor getFileDescriptor(String wordName) throws IOException {
        return mAssets.openFd(SOUND_FOLDER + "/" + wordName + ".mp3");
    }

    private static void init(Context context) {
        mAssets = context.getAssets();
        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(MAX_SOUND)
                .setAudioAttributes(attrBuilder.build())
                .build();
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, 1, 1, 1, 0, 1);
                if(CURRENT_SOUND+1 >= MAX_SOUND) release();
            }
        });
        soundMap = new HashMap<>();
    }

    private static void release() {
        mSoundPool.release();
        inited = false;
    }


}
