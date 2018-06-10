package me.hades.yqword.view.ui.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.hades.yqword.R;
import me.hades.yqword.model.Word;
import me.hades.yqword.utils.CommonValues;
import me.hades.yqword.view.ui.activity.WordDetailActivity;
import me.hades.yqword.view.ui.activity.YouDaoWordActivity;


/**
 * Created by Hades on 2018/6/9.
 */

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {


    List<Word> wordList;
    Context context;
    boolean showChinese = true;

    public WordListAdapter(List<Word> wordList, Context context) {

        this.context = context;
        this.wordList = wordList;
    }

    public List<Word> getWordList() {
        return wordList;
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_word_brief, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Word word = wordList.get(position);
        holder.englishTxt.setText(word.getEnglish());
        if (showChinese) {
            holder.chineseTxt.setText(word.getChinese());
        } else {
            holder.chineseTxt.setText("");
        }
        View parentView = (View) (holder.chineseTxt.getParent());
        parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WordDetailActivity.class);
                intent.putExtra(CommonValues.ENGLISH, word.getEnglish());
                context.startActivity(intent);
            }
        });
        parentView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context, YouDaoWordActivity.class);
                intent.putExtra(CommonValues.ENGLISH, word.getEnglish());
                context.startActivity(intent);
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public void setShowChinese(boolean showChinese) {
        this.showChinese = showChinese;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.englishTxt)
        TextView englishTxt;
        @BindView(R.id.chineseTxt)
        TextView chineseTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
