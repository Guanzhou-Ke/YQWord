package me.hades.yqword.view.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.util.DialogUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.hades.yqword.App;
import me.hades.yqword.R;
import me.hades.yqword.model.Message;
import me.hades.yqword.preference.ApiPreference;
import me.hades.yqword.utils.CommonValues;
import me.hades.yqword.utils.MD5Util;
import me.hades.yqword.utils.SPUtil;
import me.hades.yqword.utils.ToastUtil;
import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HTTP;

/**
 * Created by dandelion1995 on 2018/6/17.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = LoginActivity.class.getSimpleName();


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.userEditText)
    EditText userET;

    @BindView(R.id.pwdEditText)
    EditText pwdET;

    @BindView(R.id.bnLogin)
    Button bnLogin;

    @BindView(R.id.bnCancel)
    Button bnCancel;

    @BindView(R.id.register_tv)
    TextView register_tv;

    private ApiPreference apiPreference;

    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        this.apiPreference = App.apiPreference;
        bnLogin.setOnClickListener(this);
        bnCancel.setOnClickListener(this);
        register_tv.setOnClickListener(this);

        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);
        //设置返回键
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 点击系统返回按钮
                setResult(0);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bnLogin:
                login();
                break;
            case R.id.bnCancel:
                setResult(0);
                finish();
                break;
            case R.id.register_tv:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private void login() {
        final String username = userET.getText().toString();
        String password = pwdET.getText().toString();

        if (null == username || null == password || username.equals("") || password.equals("")) {
            showDialog("用户名和密码不能为空");
            return ;
        }
        if (password.trim().length() < 6) {
            showDialog("密码不能小于6位数");
            return ;
        }
        showDialog("登录中...请稍后");
        // 加密
        password = MD5Util.encoder(password);
        apiPreference.login(username, password).enqueue(
                new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        Message msg = response.body();
                        Log.e(TAG, response.toString());
                        if (200 == msg.code) {
                            dismissLoadingDialog();
                            ToastUtil.showShort(LoginActivity.this, "登录成功");
                            SPUtil.putAndApply(LoginActivity.this, CommonValues.TOKEN_KEY, msg.message);
                            SPUtil.putAndApply(LoginActivity.this, CommonValues.USERNAME_KEY, username);
                            setResult(1);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Log.e(TAG, t.toString());
                        showDialog("网络异常");
                    }
                }
        );
    }


    private void showDialog(String content) {
        if (null == dialog) {
            dialog = new MaterialDialog.Builder(this)
                    .title("提示")
                    .widgetColorRes(R.color.colorPrimary)
                    .negativeText("OK")
                    .build();
        }
        dialog.setContent(content);
        dialog.show();
    }



}