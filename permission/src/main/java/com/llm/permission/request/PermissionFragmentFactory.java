package com.llm.permission.request;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


import com.llm.permission.PermissionUtils;
import com.llm.permission.log.Logger;
import com.llm.permission.request.fragment.PermissionFragment;
import com.llm.permission.request.fragment.PermissionSupportFragment;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * fragment 工厂类
 * 生产 implement IRequestPermissionAction 的 fragment
 *
 * @author v_luoliming01
 * @date 2019-06-27 16:42
 */
public class PermissionFragmentFactory {
    private static final String TAG = PermissionFragmentFactory.class.getSimpleName();
    /**
     * fragment tag
     */
    private static final String FRAGMENT_TAG = "permission_fragment";

    /**
     * 创建 IRequestPermissionAction 具体实现fragment
     *
     * @param activity
     * @return
     */
    public static IRequestPermissionAction create(Activity activity) {
        Logger.d(TAG, "find fragment in FragmentActivity");
        IRequestPermissionAction fragment = null;
        if (activity instanceof FragmentActivity) {
            // 使用support包或者androidx，二者不能兼容
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            PermissionSupportFragment supportFragment =
                    (PermissionSupportFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
            if (supportFragment == null) {
                supportFragment = PermissionSupportFragment.newInstance();
                fragmentManager.beginTransaction()
                        .add(supportFragment, FRAGMENT_TAG)
                        .commitNowAllowingStateLoss();
            }
            fragment = supportFragment;
            Logger.d(TAG, "used by android.support/androidx package ");
        } else {
            // 使用app包
            android.app.FragmentManager fragmentManager = activity.getFragmentManager();
            PermissionFragment permissionFragment =
                    (PermissionFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
            if (permissionFragment == null) {
                permissionFragment = PermissionFragment.newInstance();
                fragmentManager.beginTransaction()
                        .add(permissionFragment, FRAGMENT_TAG)
                        .commitAllowingStateLoss();
            }
            fragment = permissionFragment;
            Logger.d(TAG, "used by android.app package ");
        }
        return fragment;
    }

    /**
     * 创建代理 IRequestPermissionAction 对象
     *
     * @return
     */
    public static IRequestPermissionAction createFragmentProxy() {
        final IRequestPermissionAction target = create(PermissionUtils.getTopActivity());
        return (IRequestPermissionAction) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Logger.d(TAG, "proxy invoke start");
                        Object object = method.invoke(target, args);
                        Logger.d(TAG, "proxy invoke end");
                        return object;
                    }
                });
    }
}
