package com.llm.activityresult;

import android.app.Activity;
import android.content.Intent;

import com.llm.activityresult.fragment.ResultFragmentFactory;

/**
 * ActivityResult 辅助器
 * 先init，再startActivityForResult
 *
 * @author v_luoliming01
 * @date 2019-07-05 15:30
 */
public class ActivityResultHelper {

    private Activity mActivity;

    private ActivityResultHelper(Activity activity) {
        this.mActivity = activity;
    }

    public static ActivityResultHelper init(Activity activity) {
        return new ActivityResultHelper(activity);
    }

    public void startActivityForResult(Intent intent, ResultCallback resultCallback) {
        if (intent == null || resultCallback == null) {
            return;
        }
        ResultFragmentFactory.createProxy(mActivity)
                .startActivityForResult(intent, resultCallback);
    }

    public void startActivityForResult(Class<?> clazz, ResultCallback resultCallback) {
        if (clazz == null) {
            return;
        }
        startActivityForResult(new Intent(mActivity, clazz), resultCallback);
    }
}
