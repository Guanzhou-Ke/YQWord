package me.hades.yqword.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.ycl.tabview.library.TabView;
import com.ycl.tabview.library.TabViewChild;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.hades.yqword.App;
import me.hades.yqword.R;
import me.hades.yqword.utils.CommonValues;
import me.hades.yqword.view.ui.fragment.NewsFragment;
import me.hades.yqword.view.ui.fragment.HomeFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener,
        NewsFragment.OnFragmentInteractionListener{


    @BindView(R.id.tabView)
    TabView tabView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    HomeFragment homeFragment;
    NewsFragment newsFragment;



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
        TabViewChild tab1 = new TabViewChild(R.mipmap.ic_launcher, R.mipmap.ic_launcher, "记单词", homeFragment);
        TabViewChild tab2 = new TabViewChild(R.mipmap.ic_launcher, R.mipmap.ic_launcher, "考研动态", newsFragment);
        tabViewChildList.add(tab1);
        tabViewChildList.add(tab2);

        tabView.setTabViewChild(tabViewChildList, getSupportFragmentManager());


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
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


}
