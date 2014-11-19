package com.dawn.spring.utils;

/**
 * Created by dawn-pc on 2014/11/15.
 */
public class CacheUtils {

    /** 检查SD卡是否存在 */
    public static boolean checkSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }
}
