package me.hades.yqword.view.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import butterknife.BindView;
import butterknife.ButterKnife;
import me.hades.yqword.R;
import me.hades.yqword.utils.ADFilterTool;
import me.hades.yqword.utils.Config;
import retrofit2.http.GET;

public class WebViewActivity extends BaseActivity {

    private static final String TAG = WebViewActivity.class.getSimpleName();


    public static final String URL_KEY = "url";

    public static final String TITLE_KEY = "title";


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.wv_content)
    WebView mWebContent;

    @BindView(R.id.index_progressBar)
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        initDataAndView();
    }

    private void initWeb() {
        mWebContent.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //声明WebSettings子类
        WebSettings webSettings = mWebContent.getSettings();

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setJavaScriptEnabled(true);


        mWebContent.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mProgressBar.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            /**
             * 拦截器，对广告继续拦截
             * @param view
             * @param request
             * @return
             */
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString().toLowerCase();
                Log.e(TAG, url);
                if (!ADFilterTool.hasAd(context, url)) {
                    return super.shouldInterceptRequest(view, request);//正常加载
                }else{
                    return new WebResourceResponse(null,null,null);//含有广告资源屏蔽请求
                }
            }
        });
        //显示进度
        mWebContent.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.d(TAG, newProgress+"");
                mProgressBar.setProgress(newProgress);
                if(newProgress >= 100){
                    mProgressBar.setVisibility(View.GONE);
                }

            }

        });

    }



    public void initDataAndView() {
        initWeb();
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(URL_KEY)) {
            String url = bundle.getString(URL_KEY);
            mWebContent.loadUrl(url);
        }
        if (bundle.containsKey(TITLE_KEY)) {
            String title = bundle.getString(TITLE_KEY);
            toolbar.setTitle(title);

        }
        setSupportActionBar(toolbar);
        //设置返回键
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }



    /**
     * 通过URL来启动
     * @param context
     * @param url
     */
    public static void startActivity(Context context, String url, String title) {
        Intent intent = new Intent();
        intent.setClass(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(URL_KEY,url);
        bundle.putString(TITLE_KEY, title);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 点击系统返回按钮
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
