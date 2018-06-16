package me.hades.yqword.view.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;
import me.hades.yqword.App;
import me.hades.yqword.R;
import me.hades.yqword.model.NewsModel;
import me.hades.yqword.preference.ApiPreference;
import me.hades.yqword.utils.ToastUtil;
import me.hades.yqword.view.ui.activity.MainActivity;
import me.hades.yqword.view.ui.activity.WebViewActivity;
import me.hades.yqword.view.ui.adapter.NewsAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NewsFragment extends Fragment implements BGAOnRVItemClickListener,
        BGARefreshLayout.BGARefreshLayoutDelegate{

    private static final String TAG = NewsFragment.class.getSimpleName();

    @BindView(R.id.refresh_layout)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.data_rv)
    RecyclerView mDataRv;

    NewsAdapter mAdapter;

    int page = 0;

    private ArrayList<NewsModel> mDatas = null;


    MainActivity mainActivity;
    private OnFragmentInteractionListener mListener;

    // 获取api接口
    private ApiPreference apiPreference = App.apiPreference;

    // 检测当前刷新加载状态
    private boolean isRefresh = false, isLoadMore = false;

    private MaterialDialog mLoadingDialog;

    public NewsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }



    private void initView() {
        mRefreshLayout.setDelegate(this);

        mAdapter = new NewsAdapter(mDataRv);
        mAdapter.setOnRVItemClickListener(this);

        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(
                App.globalContext, true
        );

        stickinessRefreshViewHolder.setStickinessColor(R.color.colorPrimary);
        stickinessRefreshViewHolder.setRotateImage(R.mipmap.bga_refresh_stickiness);
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);
        mDataRv.setLayoutManager(new LinearLayoutManager(App.globalContext, LinearLayoutManager.VERTICAL, false));
        mDataRv.setAdapter(mAdapter);

        showLoadingDialog();
        isLoadMore = true;
        getDataFromServer();

    }

    private void getDataFromServer() {
        if (page == 0) {
            mDatas = new ArrayList<>();
        }
        apiPreference.listNews(page).enqueue(new Callback<List<NewsModel>>() {
            @Override
            public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                Log.i(TAG, response.body().toString());
                mDatas.addAll(response.body());
                if (isRefresh) {
                    mRefreshLayout.endRefreshing();
                    isRefresh = false;
                } else if (isLoadMore) {
                    dismissLoadingDialog();
                    mRefreshLayout.endLoadingMore();
                    isLoadMore = false;
                }
                mAdapter.setData(mDatas);
                page++;
            }

            @Override
            public void onFailure(Call<List<NewsModel>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mainActivity = (MainActivity) context;

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
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        NewsModel newsModel = mAdapter.getData().get(position);
        WebViewActivity.startActivity(getContext(), newsModel.getLink(), newsModel.getTitle());
//        ToastUtil.showShort(getContext(), "点击了: " + mAdapter.getData().get(position).getTitle());
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        isRefresh = true;
        getDataFromServer();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        showLoadingDialog();
        isLoadMore = true;
        getDataFromServer();
        return true;
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

        void onFragmentInteraction(int action);
    }


    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            // 这里注意不能绑定全局
            mLoadingDialog = new MaterialDialog.Builder(getContext())
                    .title("加载中")
                    .widgetColorRes(R.color.colorPrimary)
                    .progress(true, 0)
                    .iconRes(R.mipmap.ic_launcher)
                    .cancelable(false)
                    .build();
        }
        mLoadingDialog.setContent("好消息正在火速赶来...");
        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

}
