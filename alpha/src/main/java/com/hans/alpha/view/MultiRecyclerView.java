package com.hans.alpha.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hans.alpha.R;
import com.hans.alpha.android.Log;

/**
 * Created by Changel on 2016/3/13.
 * <p>支持下拉刷新自动加载更多的RecyclerView
 * 具体使用布局{@link MultiRecyclerViewLayout}</p>
 * <p/>
 * Changed by Hans on 2016/7/29  修复添加头布局position不准确的错误(Noted by YZW)
 */
public class MultiRecyclerView extends RecyclerView {

    private final static String TAG = "MotherShip.BaseRecyclerView";

    protected boolean mIsFooterEnable = false;

    public void setAutoLoadMoreEnable(boolean autoLoadMore) {
        mIsFooterEnable = autoLoadMore;
    }

    private boolean mIsHeaderEnable = false;

    public void setHeaderEnable(boolean mIsHeaderEnable) {
        this.mIsHeaderEnable = mIsHeaderEnable;
    }

    private boolean mIsLoadingMore = false;

    public void setLoadingMore(boolean loadingMore) {
        this.mIsLoadingMore = loadingMore;
    }

    private boolean mIsRefreshing = true;

    public void setIsRefreshing(boolean isRefreshing) {
        this.mIsRefreshing = isRefreshing;
    }

    public MultiRecyclerView(Context context) {
        super(context);
        initialize();
    }

    public MultiRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public MultiRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public interface MutilRecycleViewLoadMoreListener {
        void onLoadMore();

    }

    private MutilRecycleViewLoadMoreListener DEFAULT_LISTENER = new MutilRecycleViewLoadMoreListener() {
        @Override
        public void onLoadMore() {
            Log.w(TAG, "onLoadMore in default");
        }
    };

    private MutilRecycleViewLoadMoreListener mListener = DEFAULT_LISTENER;

    public void setListener(MutilRecycleViewLoadMoreListener mListener) {
        this.mListener = mListener;
    }

    private int mLoadMorePosition = -1;

    private void initialize() {

        super.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mIsFooterEnable && !mIsLoadingMore && !mIsRefreshing && dy > 0) {
                    int lastVisiblePosition = getLastVisiblePosition();
                    if (lastVisiblePosition + 1 == mAutoLoadAdapter.getItemCount()) {
                        setLoadingMore(true);
                        mLoadMorePosition = lastVisiblePosition;
                        mListener.onLoadMore();
                    }
                } else if (mIsFooterEnable && !mIsLoadingMore && mIsRefreshing && dy > 0) {
                    // 保证在刷新的同时滑到底部请求加载更多，会在完成刷新后执行。
                    int lastVisiblePosition = getLastVisiblePosition();
                    if (lastVisiblePosition + 1 == mAutoLoadAdapter.getItemCount()) {
                        setLoadingMore(true);
                    }
                }
            }
        });
    }

    @Override
    public void setLayoutManager(final LayoutManager layout) {
        if (layout == null) {
            Log.d(TAG, "layout is null");
            return;
        }
        if (layout instanceof GridLayoutManager) {
            ((GridLayoutManager) layout).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getAdapter().getItemCount() - 1 == position
                            && mIsFooterEnable && getAdapter().getItemViewType(position) == TYPE_FOOTER) {
                        return ((GridLayoutManager) layout).getSpanCount();

                    } else {
                        return 1;
                    }
                }
            });
        }
        if (getLayoutManager() == null) {
            super.setLayoutManager(layout);
        } else {
            switchLayoutManager(layout);
        }
    }


    private AutoLoadAdapter mAutoLoadAdapter = null;

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            if (null != mAutoLoadAdapter) {
                mAutoLoadAdapter.onFinish();
            }
            mAutoLoadAdapter = new AutoLoadAdapter(adapter);
            if (getAdapter() == null) {
                super.setAdapter(mAutoLoadAdapter);
            } else {

                super.swapAdapter(mAutoLoadAdapter, true);
            }
        } else {
            Log.e(TAG, "setAdapter err:%s", "adapter is null");
            throw new NullPointerException("adapter is null");
        }
    }

    public final static int TYPE_HEADER = 0X1111;
    public final static int TYPE_FOOTER = 0X2222;

    public class AutoLoadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private RecyclerView.Adapter mInternalAdapter;

        public AutoLoadAdapter() {

        }

        public AutoLoadAdapter(RecyclerView.Adapter adapter) {
            mInternalAdapter = adapter;
        }

        public void onFinish() {
            this.mInternalAdapter = null;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == TYPE_HEADER) {
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        mHeaderResId, parent, false));
            } else if (viewType == TYPE_FOOTER) {
                return new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        mFooterResId, parent, false));
            } else {
                return mInternalAdapter.onCreateViewHolder(parent, viewType);
            }

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if (type != TYPE_FOOTER && type != TYPE_HEADER) {
                if (mIsHeaderEnable)//头部占用了1个位置,所以回传过去的position是要-1的 Noted By YZW
                    mInternalAdapter.onBindViewHolder(holder, position - 1);
                else
                    mInternalAdapter.onBindViewHolder(holder, position);
            }
        }

        @Override
        public int getItemCount() {
            return mInternalAdapter.getItemCount() + (mIsHeaderEnable ? 1 : 0) + (mIsFooterEnable ? 1 : 0);
        }

        @Override
        public int getItemViewType(int position) {
            int headerPosition = 0;
            int footerPosition = getItemCount() - 1;

            if (headerPosition == position && mIsHeaderEnable && mHeaderResId > 0) {
                return TYPE_HEADER;
            }
            if (footerPosition == position && mIsFooterEnable && mFooterResId > 0) {
                return TYPE_FOOTER;
            }

            return mInternalAdapter.getItemViewType(position);
        }

        public class FooterViewHolder extends RecyclerView.ViewHolder {
            public FooterViewHolder(View footerView) {
                super(footerView);
            }
        }

        public class HeaderViewHolder extends RecyclerView.ViewHolder {
            public HeaderViewHolder(View headerView) {
                super(headerView);
            }
        }

    }

    private int mHeaderResId;

    public void addHeaderView(int resId) {
        mHeaderResId = resId;
    }

    private int mFooterResId = R.layout.list_foot_loading;

    public void addFooterView(int resId) {
        if (resId != 0) {
            mFooterResId = resId;
        }
    }

    private int getLastVisiblePosition() {
        int position = 0;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else if (null != getLayoutManager()) {
            position = getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    private int getFirstVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMinPositions(lastPositions);
        } else {
            position = 0;
        }
        return position;
    }

    private int getMinPositions(int[] positions) {
        int size = positions.length;
        int minPosition = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            minPosition = Math.min(minPosition, positions[i]);
        }
        return minPosition;
    }

    public void switchLayoutManager(LayoutManager layoutManager) {
        int firstVisiblePosition = getFirstVisiblePosition();
//        getLayoutManager().removeAllViews();
        super.setLayoutManager(layoutManager);
        //super.swapAdapter(mAutoLoadAdapter, true);
        getLayoutManager().scrollToPosition(firstVisiblePosition);
    }

    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }

    public void onLoadMoreFinish(boolean hasMore) {
        setAutoLoadMoreEnable(hasMore);
        getAdapter().notifyItemRemoved(mLoadMorePosition);
        mIsLoadingMore = false;
    }

    public void isNeedLoadMoreSoon() {
        int lastVisiblePosition = getLastVisiblePosition();
        if (lastVisiblePosition == -1) {
            return;
        }
        if (mIsLoadingMore == true) {
            setLoadingMore(true);
            mLoadMorePosition = lastVisiblePosition;
            mListener.onLoadMore();
        }
    }

    public static class Builder {

        private Context mContext;
        private boolean isAutoLoadMore;
        private RecyclerView.Adapter adapter;
        private int resId = R.layout.list_foot_loading;
        private RecyclerView.LayoutManager layout;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder autoLoadMoreEnable(boolean isAutoLoadMore) {
            this.isAutoLoadMore = isAutoLoadMore;
            return this;
        }

        public Builder adapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
            return this;
        }

        public Builder footerView(int resId) {
            this.resId = resId;
            return this;
        }

        public Builder layoutManager(RecyclerView.LayoutManager layout) {
            this.layout = layout;
            return this;
        }

        public MultiRecyclerView build() {
            MultiRecyclerView pMultiRecyclerView = new MultiRecyclerView(mContext);
            pMultiRecyclerView.setAutoLoadMoreEnable(isAutoLoadMore);
            pMultiRecyclerView.setAdapter(adapter);
            pMultiRecyclerView.addFooterView(resId);
            pMultiRecyclerView.setLayoutManager(layout);
            return pMultiRecyclerView;
        }


    }


}