package me.hades.yqword.view.ui.adapter;

import android.support.v7.widget.RecyclerView;

import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;
import me.hades.yqword.R;
import me.hades.yqword.model.NewsModel;

/**
 * Created by hades on 2018/6/11.
 */

public class NewsAdapter extends BGARecyclerViewAdapter<NewsModel> {

    public NewsAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_news);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, NewsModel model) {
        helper.setText(R.id.news_title, model.getTitle())
                .setText(R.id.news_brief, model.getBrief())
                .setText(R.id.news_source, model.getSource())
                .setText(R.id.news_keywords, model.getKeywords())
                .setText(R.id.news_date, model.getDetetime());
    }
}
