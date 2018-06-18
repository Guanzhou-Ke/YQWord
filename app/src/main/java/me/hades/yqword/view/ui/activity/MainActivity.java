package me.hades.yqword.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;
import com.ycl.tabview.library.TabView;
import com.ycl.tabview.library.TabViewChild;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.hades.yqword.App;
import me.hades.yqword.R;
import me.hades.yqword.model.User;
import me.hades.yqword.utils.CommonValues;
import me.hades.yqword.utils.SPUtil;
import me.hades.yqword.view.ui.fragment.NewsFragment;
import me.hades.yqword.view.ui.fragment.HomeFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener,
        NewsFragment.OnFragmentInteractionListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tabView)
    TabView tabView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    ImageView head_icon_iv;

    TextView nick_name_tv;

    TextView username_tv;

    private static final int LOGIN_REQUEST_CODE = 0x00001;

    private static final int USER_DETAILS_REQUEST_CODE = 0x00002;

    private boolean isLogin = false;

    HomeFragment homeFragment;
    NewsFragment newsFragment;

    private Menu menu;

    private MaterialDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        homeFragment = new HomeFragment();
        newsFragment = new NewsFragment();

        // 初始化底部导航栏
        List<TabViewChild> tabViewChildList=new ArrayList<>();
        TabViewChild tab1 = new TabViewChild(R.drawable.ic_main_learn2, R.drawable.ic_main_learn1, "记单词", homeFragment);
        TabViewChild tab2 = new TabViewChild(R.drawable.ic_main_new2, R.drawable.ic_main_new1, "考研动态", newsFragment);
        tabViewChildList.add(tab1);
        tabViewChildList.add(tab2);

        tabView.setTabViewChild(tabViewChildList, getSupportFragmentManager());


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();

        View headerView = navigationView.getHeaderView(0);
        head_icon_iv = ButterKnife.findById(headerView, R.id.head_icon_iv);
        nick_name_tv = ButterKnife.findById(headerView, R.id.nick_name_tv);
        username_tv = ButterKnife.findById(headerView, R.id.username_tv);

        if (SPUtil.get(this, CommonValues.TOKEN_KEY, "").equals("")) {
            // 没登陆
            menu.getItem(0).setTitle("登陆");
            setHeaderViewInvisible();
            isLogin = false;
        } else {
            menu.getItem(0).setTitle("个人资料");
            checkUserInfo();
            isLogin = true;
        }
    }

    private void setHeaderViewInvisible() {
        head_icon_iv.setVisibility(View.INVISIBLE);
        nick_name_tv.setVisibility(View.INVISIBLE);
        username_tv.setVisibility(View.INVISIBLE);
    }

    private void setHeaderViewVisible() {
        head_icon_iv.setVisibility(View.VISIBLE);
        nick_name_tv.setVisibility(View.VISIBLE);
        username_tv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.searchWordView) {
            startActivity(new Intent(this, SearchWordActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user_info) {
            if (!isLogin) {
                startActivityForResult(new Intent(this, LoginActivity.class), LOGIN_REQUEST_CODE);
            } else {
                startActivityForResult(new Intent(this, UserDetailsActivity.class), USER_DETAILS_REQUEST_CODE);
            }
        } else if (id == R.id.nav_hard_word_note) {

        } else if (id == R.id.nav_rank_list) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {

        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * Fragment通讯回调接口
     * @param action
     */
    @Override
    public void onFragmentInteraction(int action) {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CommonValues.DATA_CHANGED) {
            homeFragment.refresh();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (LOGIN_REQUEST_CODE == requestCode) {
            isLogin = (1 == resultCode);
            checkUserInfo();
        } else if (USER_DETAILS_REQUEST_CODE == requestCode){
            if (-1 == resultCode) {
                // 退出登录
                isLogin = false;
                SPUtil.remove(this, CommonValues.TOKEN_KEY);
                SPUtil.remove(this, CommonValues.USERNAME_KEY);
                menu.getItem(0).setTitle("登录");
                setHeaderViewInvisible();
            }
        }
    }

    /**
     * 检查用户个人信息
     */
    private void checkUserInfo() {

        String username = (String) SPUtil.get(this, CommonValues.USERNAME_KEY, "");
        String token = (String) SPUtil.get(this, CommonValues.TOKEN_KEY, "");
        App.apiPreference.getUserInfo(username, token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                Picasso.get()
                        .load(user.getHeadicon())
                        .resize(50, 50)
                        .centerCrop()
                        .into(head_icon_iv);
                username_tv.setText(user.getUsername());
                nick_name_tv.setText(user.getNickname());
                menu.getItem(0).setTitle("个人资料");
                setHeaderViewVisible();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
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
                    .build();
        }
        dialog.setContent(content);
        dialog.show();
    }
}
