package com.llm.activityresult.fragment;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.llm.activityresult.IStartActivityAction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ResultFragment 工厂类
 *
 * @author v_luoliming01
 */
public class ResultFragmentFactory {
    private static final String FRAGMENT_TAG = "result_fragment";

    /**
     * 创建 IStartActivityAction 实现fragment
     * 并加入Activity
     *
     * @param activity Host Activity
     * @return
     */
    public static IStartActivityAction create(Activity activity) {
        IStartActivityAction fragment = null;
        if (activity instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            ResultSupportFragment supportFragment =
                    (ResultSupportFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
            if (supportFragment == null) {
                supportFragment = ResultSupportFragment.newInstance();
                fragmentManager.beginTransaction()
                        .add(supportFragment, FRAGMENT_TAG)
                        .commitNowAllowingStateLoss();
            }
            fragment = supportFragment;
        } else {
            android.app.FragmentManager fragmentManager = activity.getFragmentManager();
            ResultFragment resultFragment =
                    (ResultFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
            if (resultFragment == null) {
                resultFragment = ResultFragment.newInstance();
                fragmentManager.beginTransaction()
                        .add(resultFragment, FRAGMENT_TAG)
                        .commitAllowingStateLoss();
            }
            fragment = resultFragment;
        }
        return fragment;
    }

    /**
     * 创建 IStartActivityAction 代理类
     *
     * @param activity Host Activity
     * @return
     */
    public static IStartActivityAction createProxy(Activity activity) {
        final IStartActivityAction target = create(activity);
        return (IStartActivityAction) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return method.invoke(target, args);
                    }
                });
    }
}
