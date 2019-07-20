package com.llm.activityresult.util;

import android.app.Activity;
import android.os.Build;
import android.util.SparseArray;

import java.util.Random;

/**
 * 工具类
 *
 * @author v_luoliming01
 * @date 2019-07-05 14:42
 */
public final class Tools {
    private static final int MAX_COUNT = 10;

    /**
     * 随机生成请求码
     * 请求码在给定数组中有了就重新生成，且最多尝试生成10次
     *
     * @param sparseArray
     * @param <T>
     * @return
     */
    public static <T> int generateRequestCode(SparseArray<T> sparseArray) {
        Random random = new Random();
        int code = 0;
        int count = 0;
        do {
            code = random.nextInt(0x0000FFFF);
            count++;
        } while (sparseArray.indexOfKey(code) >= 0 && count < MAX_COUNT);
        return code;
    }

    /**
     * Activity 是否可以用
     *
     * @param activity
     * @return
     */
    public static boolean isActivityAvailable(Activity activity) {
        if (activity == null) {
            return false;
        }
        if (activity.isFinishing()) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            return false;
        }
        return true;
    }
}
