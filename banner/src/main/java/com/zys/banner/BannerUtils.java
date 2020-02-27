package com.zys.banner;

public final class BannerUtils {
    public static int getRealPosition(int position, int realCount) {
        if (position == 0) {
            return realCount - 2;
        } else if (position == 1) {
            return realCount - 1;
        } else if (position == realCount + 2) {
            return 0;
        } else if (position == realCount + 3) {
            return 1;
        } else {
            return position - 2;
        }
    }
}
