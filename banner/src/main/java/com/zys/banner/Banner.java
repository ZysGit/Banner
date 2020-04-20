package com.zys.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;


public class Banner extends FrameLayout {
    private static final String TAG = Banner.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private BannerAdapter bannerAdapter;
    private LinearLayoutManager mLayoutManager;

    private AutoLoopTask mAutoLoopTask;

    private OnBannerListener mOnBannerListener;

    private long mDelayTime;

    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initTypeArray(context, attrs);
    }

    private void initTypeArray(Context context, AttributeSet attrs) {

    }

    private void init(Context context) {
        mAutoLoopTask = new AutoLoopTask(this);

        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mLayoutManager = new BannerLayoutManager(context);
        mLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    resetBorderItem();
                }
            }
        });

        PagerSnapHelper mPagerSnapHelper = new PagerSnapHelper();
        mPagerSnapHelper.attachToRecyclerView(mRecyclerView);
        addView(mRecyclerView);

        initBannerParams();
    }

    private void initBannerParams() {
        setDelayTime(5_000L);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_UP
                || action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_OUTSIDE) {
            autoResume();
        } else if (action == MotionEvent.ACTION_DOWN) {
            autoPause();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            autoResume();
        } else {
            autoPause();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        autoResume();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        autoPause();
    }

    public void setAdapter(BannerAdapter adapter) {
        bannerAdapter = adapter;
        mRecyclerView.setAdapter(bannerAdapter);
        bannerAdapter.notifyDataSetChanged();
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollToItem(2, false);
                if (mOnBannerListener != null) {
                    mOnBannerListener.onBannerChanged(0);
                }
            }
        }, 500);
    }

    public BannerAdapter getAdapter() {
        return bannerAdapter;
    }

    public void setOnBannerListener(OnBannerListener onBannerListener) {
        this.mOnBannerListener = onBannerListener;
        if (getAdapter() != null) {
            getAdapter().setOnBannerListener(this.mOnBannerListener);
        }
    }

    public void setDelayTime(long mDelayTime) {
        this.mDelayTime = mDelayTime;
    }

    private boolean started;

    public void start() {
        Log.d(TAG, "start");
        stop();
        postDelayed(mAutoLoopTask, mDelayTime);
        started = true;
        isPlaying = true;
    }

    private boolean isPlaying;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void stop() {
        Log.d(TAG, "stop");
        removeCallbacks(mAutoLoopTask);
        started = false;
        isPlaying = false;
    }

    private boolean isPaused;

    private void autoPause() {
        if (started && !isPaused) {
            Log.d(TAG, "auto pause");
            removeCallbacks(mAutoLoopTask);
            isPaused = true;
        }
    }

    private void autoResume() {
        if (started && isPaused) {
            Log.d(TAG, "auto resume");
            isPaused = false;
            postDelayed(mAutoLoopTask, mDelayTime);
        }
    }

    private int getCurrentItem() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            int offset = lastVisibleItemPosition - firstVisibleItemPosition;
            return firstVisibleItemPosition + (offset / 2);
        }
        return 0;
    }

    private int getItemCount() {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter != null) {
            return adapter.getItemCount();
        }
        return 0;
    }

    private void scrollToItem(int position, boolean smoothScroll) {
//        Log.d(TAG, "scrollToItem [" + position + "]");
        if (position < 0) {
            return;
        }
        if (smoothScroll) {
            mRecyclerView.smoothScrollToPosition(position);
        } else {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            if (layoutManager != null) {
                RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
                if (adapter != null) {
                    View childView = mRecyclerView.getChildAt(0);
                    int rvWidth = mRecyclerView.getWidth();
                    int offsetPx = 0;
                    if (childView != null) {
                        MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                        int childWidth = childView.getWidth();
                        offsetPx = (rvWidth - childWidth - layoutParams.leftMargin - layoutParams.rightMargin) / 2;
                    }
                    layoutManager.scrollToPositionWithOffset(position, offsetPx);
                }
            }
        }
    }

    private void resetBorderItem() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            int offset = lastVisibleItemPosition - firstVisibleItemPosition;
            int currentPagePosition = firstVisibleItemPosition + (offset / 2);

            RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
            if (adapter != null) {
                if (currentPagePosition == 1) {
                    scrollToItem(adapter.getItemCount() - 3, false);
                } else if (currentPagePosition == 0) {
                    scrollToItem(adapter.getItemCount() - 4, false);
                } else if (currentPagePosition == adapter.getItemCount() - 2) {
                    scrollToItem(2, false);
                } else if (currentPagePosition == adapter.getItemCount() - 1) {
                    scrollToItem(3, false);
                }
            }
        }
    }

    static class AutoLoopTask implements Runnable {

        private WeakReference<Banner> bannerWeakReference;

        AutoLoopTask(Banner banner) {
            this.bannerWeakReference = new WeakReference<>(banner);
        }

        @Override
        public void run() {
            Banner banner = this.bannerWeakReference.get();
            if (banner != null) {
                int currentItem = banner.getCurrentItem();
                int itemCount = banner.getItemCount();
                if (itemCount <= 4) {
                    return;
                }
                int next = currentItem % (itemCount - 1) + 1;
                banner.scrollToItem(next, true);
                if (banner.mOnBannerListener != null) {
                    int realItem = BannerUtils.getRealPosition(next, itemCount - 4);
                    banner.mOnBannerListener.onBannerChanged(realItem);
                }
                banner.postDelayed(this, banner.mDelayTime);
            }
        }
    }
}
