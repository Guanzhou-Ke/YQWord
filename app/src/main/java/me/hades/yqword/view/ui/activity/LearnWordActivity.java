package me.hades.yqword.view.ui.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.hades.yqword.R;
import me.hades.yqword.model.LearnWordList;
import me.hades.yqword.model.Word;
import me.hades.yqword.model.WordList;
import me.hades.yqword.utils.ChineseCheck;
import me.hades.yqword.utils.CommonValues;
import me.hades.yqword.utils.Config;
import me.hades.yqword.utils.OnSwipeTouchListener;
import me.hades.yqword.utils.SPUtil;
import me.hades.yqword.utils.ToastUtil;
import me.hades.yqword.view.ui.dialog.NeverShowWordDialog;

public class LearnWordActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.englishTxt)
    TextView englishTxt;
    @BindView(R.id.chineseTxt)
    TextView chineseTxt;

    @BindView(R.id.lastLearnTime)
    TextView lastLearnTime;
    @BindView(R.id.knownTimeTxt)
    TextView knownTimeTxt;
    @BindView(R.id.phoneticTxt)
    TextView phoneticTxt;
    WordList words;
    boolean autoDisplay = false;
    @BindView(R.id.wordDetailBtn)
    TextView wordDetailBtn;
    @BindView(R.id.learnProgressBar)
    ProgressBar learnProgressBar;
    boolean tipsShouldShow;
    boolean netErrorShouldShow = true;
    @BindView(R.id.voiceBtn)
    Button displayVoiceBtn;
    @BindView(R.id.countLayout)
    LinearLayout countLayout;
    @BindView(R.id.card_view)
    CardView cardView;
    @BindView(R.id.unknownBtn)
    Button unknownBtn;
    @BindView(R.id.knowBtn)
    Button knowBtn;
    @BindView(R.id.cancelGraspBtn)
    TextView cancelGraspBtn;
    @BindView(R.id.coreImg)
    ImageView coreImg;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tipsShouldShow = !(boolean) SPUtil.get(context, CommonValues.TIPS_OF_NEVER_SHOW, false);
        setContentView(R.layout.activity_learn_word);
        ButterKnife.bind(this);
        // 初始化
        init();
    }

    private void init() {
        mode = getIntent().getStringExtra(CommonValues.MODE);
        if (mode.equals(CommonValues.LEARN_MODE)) {
            // 获取学习的单词列表
            words = new LearnWordList(this);
        }
        toolbar.setTitle(words.getListName());
        setSupportActionBar(toolbar);
        //设置返回键
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        /**
         * 已经没有需要学习的单词了
         */
        if (words.isEmpty()) {
            ToastUtil.showShort(context, words.getEmptyMessage());
            finish();
            return;
        }
        setLogic();

    }

    /**
     * 设置界面上各个部件的逻辑.
     */
    public void setLogic() {
        autoDisplay = Config.getAutoDisplay();

        setCurrentWord(words.getCurrent());
        learnProgressBar.setProgress(words.getPercent());
        wordDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, YouDaoWordActivity.class);
                intent.putExtra(CommonValues.ENGLISH, words.getCurrent().getEnglish());
                startActivity(intent);
            }
        });
        displayVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netErrorShouldShow = true;
                displayPronunciation();
            }
        });
        unknownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unknownBtn.getText().equals("加入生词本")) {
                    next();
                } else {
                    words.currentUnknown();
                    unknownBtn.setText("加入生词本");
                    knowBtn.setText("已记忆");
                    chineseTxt.setVisibility(View.VISIBLE);
                }
            }
        });
        knowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (knowBtn.getText().equals("下一个")) {
                    words.currentKnown();
                    next();
                } else {
                    knowBtn.setText("下一个");
                    unknownBtn.setText("不认识");
                    chineseTxt.setVisibility(View.VISIBLE);
                }
            }
        });
        if (autoDisplay) {
            displayPronunciation();
        }

        cardView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeLeft() {
                previous();
            }

        });

        englishTxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Word currentWord = words.getCurrent();
                if (currentWord.getCollect() != null) {
                    ToastUtil.showShort(context, "已经收藏过了");
                } else {
                    currentWord.setCollect(1);
                    wordDao.update(currentWord);
                    ToastUtil.showShort(context, "成功收藏单词" + currentWord.getEnglish());
                }
                return false;
            }
        });

    }

    public void currentNeverShow() {
        SPUtil.putAndApply(context, CommonValues.TIPS_OF_NEVER_SHOW, true);
        tipsShouldShow = false;
        words.currentNeverShow();
        next();
    }

    /**
     * 下一个单词
     */
    public void next() {

        Word currentWord = words.next();
        if (currentWord == null) {
            String message = words.getFinishMessage();
            ToastUtil.showShort(context, message);
            finish();
            return;
        }
        dealWordChange(currentWord);
    }

    /**
     * 前一个单词
     */
    public void previous() {
        Word currentWord = words.previous();
        if (currentWord == null) {
            ToastUtil.showShort(context, "当前为第一个单词");
            return;
        }
        dealWordChange(currentWord);
    }

    /**
     * 单词改变驱动
     * @param currentWord
     */
    public void dealWordChange(Word currentWord) {
        setCurrentWord(currentWord);
        learnProgressBar.setProgress(words.getPercent());
        if (autoDisplay) {

            displayPronunciation();
        }
        if (currentWord.isNeverShow()) {
            cancelGraspBtn.setVisibility(View.VISIBLE);
        } else {
            cancelGraspBtn.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 播放音频
     */
    private void displayPronunciation() {
        //TODO:先留着
    }

    /**
     * 设置当前单词
     * @param currentWord
     */
    public void setCurrentWord(final Word currentWord) {
        englishTxt.setText(currentWord.getEnglish());
        String knowTime = currentWord.getKnowTime() == null ? "0" : currentWord.getKnowTime().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String lastLearnTimeStr;
        if (currentWord.getLastLearnTime() == null || currentWord.getLastLearnTime().getTime() == 0) {
            lastLearnTimeStr = "不曾学过";
        } else {
            lastLearnTimeStr = simpleDateFormat.format(currentWord.getLastLearnTime());
        }
        if (currentWord.getHot() != null) {
            coreImg.setVisibility(View.VISIBLE);
        } else {
            coreImg.setVisibility(View.GONE);
        }
        coreImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(context, "五角星说明这是个核心单词");
            }
        });

        knownTimeTxt.setText(knowTime);
        lastLearnTime.setText(lastLearnTimeStr);
        phoneticTxt.setText("/" + currentWord.getPhonetic() + "/");
        String chinese = currentWord.getChinese();
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
        chineseTxt.setVisibility(View.INVISIBLE);

        knowBtn.setText("已记忆");
        unknownBtn.setText("不认识");
        cancelGraspBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentWord.setNeverShow(null);
                wordDao.update(currentWord);
                ToastUtil.showShort(context, "已经取消了对该单词的掌握");
                cancelGraspBtn.setVisibility(View.INVISIBLE);
            }
        });
        if (currentWord.isNeverShow()) {
            cancelGraspBtn.setVisibility(View.VISIBLE);
        } else {
            cancelGraspBtn.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_learn_word, menu);
        MenuItem autoDisplayChk = menu.findItem(R.id.autoDisplayChk);
        autoDisplayChk.setChecked(Config.getAutoDisplay());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.neverShow:
                if (tipsShouldShow) {
                    new NeverShowWordDialog().show(getFragmentManager(), "neverShowTag");
                } else {
                    currentNeverShow();
                }
                return true;
            case R.id.autoDisplayChk:
                item.setChecked(!item.isChecked());
                autoDisplay = item.isChecked();
                Config.setAutoDisplay(autoDisplay);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
