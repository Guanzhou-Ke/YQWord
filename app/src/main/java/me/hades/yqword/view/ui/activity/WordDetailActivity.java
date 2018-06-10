package me.hades.yqword.view.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.hades.yqword.R;
import me.hades.yqword.model.Word;
import me.hades.yqword.model.WordDao;
import me.hades.yqword.utils.ChineseCheck;
import me.hades.yqword.utils.CommonValues;
import me.hades.yqword.utils.ToastUtil;

/**
 * 单词详情
 * @author hades
 * @version 1.0
 */

public class WordDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.wordDetailBtn)
    TextView wordDetailBtn;
    @BindView(R.id.voiceBtn)
    Button voiceBtn;
    String english;
    @BindView(R.id.englishTxt)
    TextView englishTxt;
    @BindView(R.id.phoneticTxt)
    TextView phoneticTxt;
    @BindView(R.id.knownTimeTxt)
    TextView knownTimeTxt;
    @BindView(R.id.lastLearnTime)
    TextView lastLearnTime;
    @BindView(R.id.isNeverShowTxt)
    TextView isNeverShowTxt;
    @BindView(R.id.chineseTxt)
    TextView chineseTxt;
    @BindView(R.id.cancelGraspBtn)
    Button cancelGraspBtn;

    boolean isNeverShow;

    @BindView(R.id.coreImg)
    ImageView coreImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        init();
    }

    private void init() {
        Intent intent = getIntent();

        english = intent.getStringExtra(CommonValues.ENGLISH);
        if (english == null || english.equals("")) {
            Log.e("error", "没有单词传入");
        }

        final Word word = wordDao.queryBuilder().where(WordDao.Properties.English.eq(english)).build().list().get(0);
        setContentView(R.layout.activity_word_detail);
        ButterKnife.bind(this);


        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPronunciation();
            }
        });
        wordDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, YouDaoWordActivity.class);
                intent.putExtra(CommonValues.ENGLISH, english);
                startActivity(intent);
            }
        });
        if (word.getHot() != null) {
            coreImg.setVisibility(View.VISIBLE);
        } else {
            coreImg.setVisibility(View.GONE);
        }

        englishTxt.setText(word.getEnglish());
        phoneticTxt.setText(word.getPhoneticFormat());
        isNeverShowTxt.setText(word.isNeverShow() ? "是" : "否");
        String knowTime = word.getKnowTime() == null ? "0" : word.getKnowTime().toString();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String lastLearnTimeStr;
        if (word.getLastLearnTime() == null || word.getLastLearnTime().getTime() == 0) {
            lastLearnTimeStr = "不曾学过";
        } else {
            lastLearnTimeStr = simpleDateFormat.format(word.getLastLearnTime());
        }


        knownTimeTxt.setText(knowTime);

        lastLearnTime.setText(lastLearnTimeStr);

        String chinese = word.getChinese();
        String regex = "[a-z]+\\.";
        Matcher matcher = Pattern.compile(regex).matcher(chinese);
        boolean isFirst = true;
        while (matcher.find()) {
            String showed = matcher.group();
            if (!isFirst) {
                //防止 出现a./vt.这种情况发生
                int index = chinese.indexOf(showed);
                String prefix = chinese.substring(0, index);
                if (ChineseCheck.containChinese(prefix)) {
                    chinese = chinese.replaceFirst(showed, "\n" + showed);
                }
            }
            isFirst = false;
        }
        chineseTxt.setText(chinese);
        isNeverShow = word.isNeverShow();
        if (isNeverShow) {
            cancelGraspBtn.setText("未掌握");
            cancelGraspBtn.setBackgroundColor(this.getResources().getColor(R.color.yellow));
        } else {
            cancelGraspBtn.setText("已掌握");
            cancelGraspBtn.setBackgroundColor(this.getResources().getColor(R.color.blue));
        }

        cancelGraspBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNeverShow) {
                    cancelGraspBtn.setText("已掌握");
                    cancelGraspBtn.setBackgroundColor(WordDetailActivity.this.getResources().getColor(R.color.blue));
                    word.setNeverShow(null);
                    ToastUtil.showShort(context, "加油，皇天不负有心人！");
                    wordDao.update(word);
                } else {
                    word.setNeverShow(1);
                    ToastUtil.showShort(context, "记得准时复习哦！");
                    cancelGraspBtn.setText("未掌握");
                    cancelGraspBtn.setBackgroundColor(WordDetailActivity.this.getResources().getColor(R.color.yellow));
                    wordDao.update(word);
                }

                isNeverShow = !isNeverShow;
                isNeverShowTxt.setText(word.isNeverShow() ? "是" : "否");

            }
        });
        englishTxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Word currentWord = word;
                if (currentWord.getCollect() != null) {
                    word.setCollect(null);
                    wordDao.update(word);
                    ToastUtil.showShort(context, "取消收藏单词" + currentWord.getEnglish());
                } else {
                    currentWord.setCollect(1);
                    wordDao.update(currentWord);
                    ToastUtil.showShort(context, "成功收藏单词" + currentWord.getEnglish());
                }
                return false;
            }
        });
        toolbar.setTitle("单词详情");
        setSupportActionBar(toolbar);
        //设置返回键
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    /**
     * 播放单词音频
     */
    public void displayPronunciation() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
