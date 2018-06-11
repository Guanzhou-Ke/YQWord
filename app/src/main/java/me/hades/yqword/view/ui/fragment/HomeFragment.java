package me.hades.yqword.view.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.hades.yqword.App;
import me.hades.yqword.R;
import me.hades.yqword.model.DaoSession;
import me.hades.yqword.model.Queries;
import me.hades.yqword.model.WordDao;
import me.hades.yqword.utils.CommonValues;
import me.hades.yqword.utils.Config;
import me.hades.yqword.utils.ToastUtil;
import me.hades.yqword.view.ui.activity.LearnWordActivity;
import me.hades.yqword.view.ui.activity.WordListActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.remainTimeTxt)
    TextView remainTimeTxt;


    @BindView(R.id.startLearnBtn)
    Button startLearnBtn;

    @BindView(R.id.progressRatePi)
    PieChart progressRatePi;

    DaoSession daoSession;
    WordDao wordDao;
    Context context;
    Timer timer = new Timer();
    @BindView(R.id.countDownView)
    CardView countDownView;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // 初始化
        daoSession = ((App) getActivity().getApplication()).getDaoSession();
        wordDao = daoSession.getWordDao();
        context = App.globalContext;

        //绑定注解
        ButterKnife.bind(this, view);
        // 开始学习
        startLearnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LearnWordActivity.class);
                intent.putExtra(CommonValues.MODE, CommonValues.LEARN_MODE);
                startActivity(intent);
            }
        });
        // 添加计时任务
        timer.schedule(new ShowRemainTime(), 0, 1000);
        // 绘制饼图
        drawPi();
        countDownView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String[] encourageSentence = CommonValues.ENCOURAGE_SENTENCE;
                int choosePosition = new Random().nextInt(encourageSentence.length);
                String showSentence = encourageSentence[choosePosition];
                ToastUtil.showShort(context, showSentence);
                return false;
            }
        });
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        mListener.onFragmentInteraction(1);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        /**
         * 通信接口
         * @param action
         */
        void onFragmentInteraction(int action);
    }

    /**
     * 绘画饼图
     */
    public void drawPi() {
        Queries queries = Queries.getInstance(daoSession);
        boolean isCoreMode = Config.coreModeIsOn();
        boolean isEasyMode = Config.easyModeIsOn();
        Integer neverShowCount = queries.getList(CommonValues.NEVER_SHOW, isCoreMode, isEasyMode).size();
        Integer knownWordCount = queries.getList(CommonValues.KNOWED, isCoreMode, isEasyMode).size();
        Integer notLearnYetCount = queries.getList(CommonValues.NOT_LEARNED, isCoreMode, isEasyMode).size();

        // todayShouldLearnTxt.setText(String.valueOf(Config.getPlanShouldLearn()));

        progressRatePi.setCenterText(Config.getPieWord());

        List<PieEntry> entries = new ArrayList<>();
        final Integer[] counts = {neverShowCount, knownWordCount};
        if (notLearnYetCount > 500) {
            for (int i = 0; i < counts.length; i++) {
                if (counts[i] < 500) {
                    counts[i] += 500;
                }
            }
        }
        entries.add(new PieEntry(counts[0], CommonValues.NEVER_SHOW + neverShowCount));
        entries.add(new PieEntry(counts[1], CommonValues.KNOWED + knownWordCount));
        entries.add(new PieEntry(notLearnYetCount, CommonValues.NOT_LEARNED + notLearnYetCount));

        PieDataSet set = new PieDataSet(entries, "总进度");
        set.setDrawValues(false);
        set.setValueTextSize(18);


        // 已掌握区域颜色
        int green = ContextCompat.getColor(context, R.color.green);
        // 已记
        int grey6 = ContextCompat.getColor(context, R.color.blue);
        // 未学习
        int grey9 = ContextCompat.getColor(context, R.color.yellow);
        set.setColors(green, grey6, grey9);
        PieData data = new PieData(set);
        progressRatePi.setCenterTextSize(40);
        progressRatePi.getDescription().setEnabled(false);
        progressRatePi.getLegend().setEnabled(false);

        progressRatePi.setData(data);
        progressRatePi.invalidate();
        progressRatePi.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                e.getData();
                String label = ((PieEntry) e).getLabel();
                Intent intent = new Intent(context, WordListActivity.class);
                intent.putExtra(CommonValues.WORD_LIST_LBL, label);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    public void refresh() {
        drawPi();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (CommonValues.DATA_CHANGED) {
                drawPi();
                CommonValues.DATA_CHANGED = false;
            }
        }
    }

    /**
     * 设置顶部倒计时
     */
    private void setRemainTime() {
        Date now = new Date();
        long diff = Config.getExamTime().getTime() - now.getTime();
        if (diff < 0) {
            Config.addExamDate();
            return;
        }


        Long diffSeconds = diff / 1000 % 60;
        Long diffMinutes = diff / (60 * 1000) % 60;
        Long diffHours = diff / (60 * 60 * 1000) % 24;
        Long diffDays = diff / (24 * 60 * 60 * 1000);


        String remainTime = diffDays + "天" + String.format(Locale.getDefault(), "%02d", diffHours) + "时"
                + String.format(Locale.getDefault(), "%02d", diffMinutes) + "分" + String.format(Locale.getDefault(), "%02d", diffSeconds) + "秒";
        remainTimeTxt.setText(remainTime);
    }

    /**
     * 倒计时后台线程
     */
    class ShowRemainTime extends TimerTask {
        public void run() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRemainTime();
                    }
                });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
