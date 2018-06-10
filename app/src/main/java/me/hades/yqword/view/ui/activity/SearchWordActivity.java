package me.hades.yqword.view.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.hades.yqword.App;
import me.hades.yqword.R;
import me.hades.yqword.model.DaoSession;
import me.hades.yqword.model.Word;
import me.hades.yqword.model.WordDao;
import me.hades.yqword.utils.CommonValues;
import me.hades.yqword.utils.Config;
import me.hades.yqword.utils.ToastUtil;
import me.hades.yqword.view.ui.adapter.WordListAdapter;
import me.hades.yqword.view.ui.dialog.WebSearchTipDialog;

public class SearchWordActivity extends BaseActivity implements SearchView.OnQueryTextListener{

    DaoSession daoSession;
    WordDao wordDao;
    android.support.v7.widget.SearchView wordSearchView;

    @BindView(R.id.candidateWordView)
    RecyclerView candidateWordView;

    WordListAdapter wordListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_word);
        ButterKnife.bind(this);

        daoSession = ((App) getApplication()).getDaoSession();
        wordDao = daoSession.getWordDao();

        wordListAdapter = new WordListAdapter(new ArrayList<Word>(), this);
        candidateWordView.setHasFixedSize(true);
        candidateWordView.setLayoutManager(new LinearLayoutManager(this));
        candidateWordView.setAdapter(wordListAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_word_menu, menu);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        final MenuItem searchItem = menu.findItem(R.id.searchWordView);
        wordSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        wordSearchView.setOnQueryTextListener(this);
        wordSearchView.setIconified(false);
        wordSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    finish();
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent = new Intent(context, YouDaoWordActivity.class);
        intent.putExtra(CommonValues.ENGLISH, query);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String toQuery = wordSearchView.getQuery().toString();
        if (toQuery.equals("")) {
            wordListAdapter.setWordList(new ArrayList<Word>());
            wordListAdapter.notifyDataSetChanged();
            return false;
        }
        if (!Pattern.compile("^[\\u4e00-\\u9fa5_a-zA-Z]+$").matcher(toQuery).matches()) {
            ToastUtil.showShort(context, "只能输入中英文");
            return false;
        }
        if (Pattern.compile("^[\\u4e00-\\u9fa5]+$").matcher(toQuery).matches()) {

            return false;
        }
        if (Pattern.compile("^[a-zA-Z]+$").matcher(toQuery).matches()) {
            String whereCondition = " where english like '" + toQuery + "%' COLLATE NOCASE limit 10";
            List<Word> words = wordDao.queryRaw(whereCondition);
            wordListAdapter.setWordList(words);

            wordListAdapter.notifyDataSetChanged();
            if (words.size() == 0) {
                if (Config.shouldSearchTipShow()) {
                    new WebSearchTipDialog().show(getFragmentManager(), "webSearchTip");
                    return false;
                }
            }
            return false;
        }
        return false;
    }
}
