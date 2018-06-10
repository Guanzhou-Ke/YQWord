package me.hades.yqword.view.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.hades.yqword.R;
import me.hades.yqword.utils.CommonValues;

/**
 * 调用WebView 利用有道接口 查询单词
 * @author hades
 */

public class YouDaoWordActivity extends AppCompatActivity {

    @BindView(R.id.youdaoWebView)
    WebView youdaoWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_dao_word);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        String english = intent.getStringExtra(CommonValues.ENGLISH);
        if (english == null || english.equals("")) {
            Log.e("error", "没有英文传入");
        }
        WebSettings webSettings = youdaoWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        getSupportActionBar().setTitle("有道单词详情");
        youdaoWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        youdaoWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                String javaScript = "javascript:(function() {__closeBanner();})()";
                youdaoWebView.loadUrl(javaScript);
            }
        });
        youdaoWebView.loadUrl(CommonValues.youdaoWordPageUrl + english);
    }
}
