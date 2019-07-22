package com.llm.permission;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.llm.permission.log.Logger;

import java.lang.ref.WeakReference;

/**
 * write the class description
 *
 * @author v_luoliming01
 */
public class PermissionActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = PermissionActivityLifecycle.class.getSimpleName();
    /**
     * 栈顶activity
     * 1.在 onActivityResumed 中赋值即可表示activity在栈顶
     * 2.在 onActivityCreated 中赋值，方便在activity onCreate()中请求权限
     */
    private static WeakReference<Activity> mTopActivity = null;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mTopActivity = new WeakReference<>(activity);
        Logger.d(TAG, "onActivityCreated:activity is " + activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        mTopActivity = new WeakReference<>(activity);
        Logger.d(TAG, "onActivityResumed:activity is " + activity);

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public static WeakReference<Activity> getTopActivity() {
        return mTopActivity;
    }
}
