package com.hans.alpha.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.hans.alpha.android.Log;

/**
 * Created by Changel on 2016/3/13.
 * <p>注意这个
 * 参考{@link MultiRecyclerView}</p>
 */
public class MultiRecyclerViewLayout extends SwipeRefreshLayout {

    private final static String TAG = "MotherShip.MutilRecyclerView";
    MultiRecyclerView mBaseRecyclerView = null;

    public MultiRecyclerViewLayout(Context context) {
        super(context);
    }

    public MultiRecyclerViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initialize(final MultiRecyclerView baseRecyclerView) {
        initialize(baseRecyclerView, null);
    }

    public void initialize(final MultiRecyclerView baseRecyclerView, LayoutParams params) {
        if (null == baseRecyclerView) {
            Log.e(TAG, "baseRecyclerView is NULL");
            return;
        }
        this.mBaseRecyclerView = baseRecyclerView;
        setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        if (null != params) {
            baseRecyclerView.setLayoutParams(params);
        } else {
            LayoutParams rp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            baseRecyclerView.setLayoutParams(rp);
        }

        addView(baseRecyclerView);
        baseRecyclerView.setListener(new MultiRecyclerView.MutilRecycleViewLoadMoreListener() {
            @Override
            public void onLoadMore() {
                setRefreshing(false);
                mListener.onLoadMore();
            }
        });

        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                baseRecyclerView.setIsRefreshing(isRefreshing());
                mListener.onRefresh();
            }
        });

    }

    public void beginPreRefresh(){
        onRefreshAnim(true);
        mListener.preRefresh();
    }


    public interface MutilRecycleViewListener {

        void preRefresh();

        void onRefresh();

        void onLoadMore();
    }

    private static MutilRecycleViewListener DEFAULT_LISTENER = new MutilRecycleViewListener() {
        @Override
        public void preRefresh() {
            Log.w(TAG, "preRefresh in default");
        }

        @Override
        public void onRefresh() {
            Log.w(TAG, "onRefresh in default");
        }

        @Override
        public void onLoadMore() {
            Log.w(TAG, "onLoadMore in default");
        }
    };

    private MutilRecycleViewListener mListener = DEFAULT_LISTENER;

    public void setListener(MutilRecycleViewListener mListener) {
        this.mListener = mListener;
    }

    private boolean isPreRefresh = true;

    public void setPreRefresh(boolean isPreRefresh) {
        this.isPreRefresh = isPreRefresh;
    }

    public RecyclerView.Adapter getAdapter() {
        return mBaseRecyclerView.getAdapter();
    }

    /**
     * 开启/关闭刷新动画
     */
    public void onRefreshAnim(final boolean isOpen) {
        post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(isOpen);
            }
        });
    }

    public void onRefreshFinish(boolean hasMore) {
        onRefreshAnim(false);
        mBaseRecyclerView.setIsRefreshing(false);
        mBaseRecyclerView.setAutoLoadMoreEnable(hasMore);
        getAdapter().notifyDataSetChanged();
        mBaseRecyclerView.isNeedLoadMoreSoon();
    }

    public void onLoadMoreFinish(boolean hasMore) {
        mBaseRecyclerView.onLoadMoreFinish(hasMore);
    }

    public MultiRecyclerView getMultiRecyclerView() {
        return mBaseRecyclerView;
    }

    public void setLoadMoreEnable(boolean isEnable) {
        mBaseRecyclerView.setAutoLoadMoreEnable(isEnable);
    }
}
