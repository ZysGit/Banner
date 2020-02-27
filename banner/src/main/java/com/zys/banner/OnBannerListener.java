package com.zys.banner;

public interface OnBannerListener<T> {
    void onBannerClicked(T data, int position);

    void onBannerChanged(int position);
}
