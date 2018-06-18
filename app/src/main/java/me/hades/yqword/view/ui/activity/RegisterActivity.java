package me.hades.yqword.view.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.hades.yqword.App;
import me.hades.yqword.R;
import me.hades.yqword.model.Message;
import me.hades.yqword.preference.ApiPreference;
import me.hades.yqword.utils.MD5Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.userEditText)
    EditText userET;

    @BindView(R.id.pwdEditText)
    EditText pwdET;

    @BindView(R.id.cf_pwdEditText)
    EditText cf_pwdET;

    @BindView(R.id.bnRegister)
    Button bnRegister;

    @BindView(R.id.bnCancel)
    Button bnCancel;

    private ApiPreference apiPreference;

    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        apiPreference = App.apiPreference;

        bnRegister.setOnClickListener(this);
        bnCancel.setOnClickListener(this);

        toolbar.setTitle("注册");
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
            case R.id.bnRegister:
                register();
                break;
            case R.id.bnCancel:
                finish();
                break;

        }
    }

    /**
     * 注册事件
     */
    private void register() {
        final String username = userET.getText().toString();
        final String password = pwdET.getText().toString();
        final String cfPassword = cf_pwdET.getText().toString();

        if (null == username || null == password || null == cfPassword
                || username.equals("") || password.equals("") || cfPassword.equals("")) {
            showDialog("用户名和密码不能为空");
            return ;
        }

        if (!password.equals(cfPassword)) {
            showDialog("密码不一致");
            return ;
        }

        if (password.trim().length() < 6) {
            showDialog("密码不能小于6位数");
            return ;
        }

        String md5_password = MD5Util.encoder(password);

        showLoadingDialog("请稍后...");
        apiPreference.addUser(username, md5_password).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                dismissLoadingDialog();
                Message msg = response.body();
                showDialog(msg.message);
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                dismissLoadingDialog();
                Log.e(TAG, t.toString());
                showDialog("网络异常");
            }
        });
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
