package me.hades.yqword.view.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.hades.yqword.App;
import me.hades.yqword.R;
import me.hades.yqword.model.Message;
import me.hades.yqword.model.User;
import me.hades.yqword.preference.ApiPreference;
import me.hades.yqword.utils.CommonValues;
import me.hades.yqword.utils.SPUtil;
import me.hades.yqword.utils.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailsActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = UserDetailsActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.head_icon_iv)
    ImageView head_icon_iv;

    @BindView(R.id.nick_name_tv)
    TextView nick_name_tv;

    @BindView(R.id.username_tv)
    TextView username_tv;

    @BindView(R.id.hard_word_book_cv)
    CardView hard_word_book_cv;

    @BindView(R.id.logout_cv)
    CardView logout_cv;

    private ApiPreference apiPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);

        apiPreference = App.apiPreference;

        logout_cv.setOnClickListener(this);

        toolbar.setTitle("个人资料");
        setSupportActionBar(toolbar);
        //设置返回键
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        showLoadingDialog("加载个人资料...");

        loadInfo();


    }

    private void loadInfo() {
        String username = (String) SPUtil.get(this, CommonValues.USERNAME_KEY, "");
        String token = (String) SPUtil.get(this, CommonValues.TOKEN_KEY, "");
        apiPreference.getUserInfo(username, token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                dismissLoadingDialog();
                User user = response.body();
                Picasso.get()
                        .load(user.getHeadicon())
                        .resize(300,300)
                        .centerCrop()
                        .into(head_icon_iv);
                username_tv.setText(user.getUsername());
                nick_name_tv.setText(user.getNickname());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                dismissLoadingDialog();
                ToastUtil.showShort(UserDetailsActivity.this, "网络异常");
                Log.e(TAG, t.toString());
            }
        });
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
            case R.id.logout_cv:
                logout();
                break;
        }
    }

    private void logout() {
        String username = (String) SPUtil.get(this, CommonValues.USERNAME_KEY, "");
        String token = (String) SPUtil.get(this, CommonValues.TOKEN_KEY, "");
        apiPreference.logout(username, token).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (200 == response.body().code) {
                    ToastUtil.showShort(UserDetailsActivity.this, "退出登录");
                    setResult(-1);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                ToastUtil.showShort(UserDetailsActivity.this, "网络异常");
                Log.e(TAG, t.toString());
            }
        });
    }
}
