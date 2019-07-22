package com.llm.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.llm.permission.bean.Permission;
import com.llm.permission.bean.SpecialPermission;
import com.llm.permission.log.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * permission 工具类
 *
 * @author v_luoliming01
 */
public class PermissionUtils {

    private static final String TAG = PermissionUtils.class.getSimpleName();

    private static Application sApplication = null;

    private PermissionUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void initApplication(Application application) {
        sApplication = application;
    }

    public static Context getContext() {
        return getApplicationContext();
    }

    public static Context getApplicationContext() {
        return getApplication();
    }

    /**
     * get application
     * 如果当前sApplication为空就反射获取
     *
     * @return
     */
    public static Application getApplication() {
        if (sApplication != null) {
            return sApplication;
        }
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method method = activityThreadClass.getMethod("currentActivityThread");
            Object activityThread = method.invoke(null, (Object[]) null);

            Field field = activityThreadClass.getField("mInitialApplication");
            field.setAccessible(true);
            sApplication = (Application) field.get(activityThread);

            if (sApplication == null) {
                Method method2 = activityThreadClass.getMethod("getApplication");
                sApplication = (Application) method2.invoke(activityThread, (Object[]) null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sApplication;
    }

    /**
     * 获取包名
     *
     * @return
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * 获取 ApplicationInfo
     *
     * @return
     */
    public static ApplicationInfo getApplicationInfo() {
        return getApplication().getApplicationInfo();
    }

    /**
     * 获取 PackageManager
     *
     * @return
     */
    public static PackageManager getPackageManager() {
        return getContext().getPackageManager();
    }

    /**
     * 获取 targetSdkVersion
     *
     * @return
     */
    public static int getTargetSdkVersion() {
        return getApplicationInfo().targetSdkVersion;
    }

    /**
     * 获取栈顶activity
     *
     * @return
     */
    public static Activity getTopActivity() {
        return PermissionActivityLifecycle.getTopActivity().get();
    }

    /**
     * 判断activity是否可用
     *
     * @param activity
     * @return
     */
    public static boolean isActivityAvailable(Activity activity) {
        if (activity == null) {
            Logger.d(TAG, "activity is null");
            return false;
        }
        if (activity.isFinishing()) {
            Logger.d(TAG, "activity is finishing");
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            Logger.d(TAG, "activity is destroyed");
            return false;
        }
        return true;
    }

    /**
     * 断言当前线程是主线程
     *
     * @return
     */
    public static boolean assertMainThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }

    /**
     * 是否是老权限系统
     * 判断是否需要进行动态权限申请
     *
     * @return
     */
    public static boolean isOldPermissionSystem() {
        int targetSdkVersion = getTargetSdkVersion();
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M && targetSdkVersion < Build.VERSION_CODES.M;
    }

    /**
     * 是否可以请求动态权限
     *
     * @return
     */
    public static boolean canRuntimePermission() {
        return !isOldPermissionSystem();
    }

    /**
     * Permission[] 转成 String[]
     *
     * @param permissions permission数组
     * @return 字符窜权限数组
     */
    public static String[] buildPermissions(@NonNull Permission[] permissions) {
        String[] array = new String[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            array[i] = permissions[i].getName();
        }
        return array;
    }

    /**
     * 生成请求code
     * <p>
     * 随机生成，不重复的request code，
     * 如果重复最多尝试 MAX_COUNT 次重新生成
     *
     * @return
     */
    public static <T> int generateRequestCode(SparseArray<T> sparseArray) {
        Random codeGenerator = new Random();
        int code = 0;
        int tryCount = 0;
        do {
            code = codeGenerator.nextInt(0x0000FFFF);
            tryCount++;
        } while (sparseArray.indexOfKey(code) >= 0 && tryCount < Constants.MAX__TRY_COUNT);
        return code;
    }

    /**
     * 筛选被拒绝的权限
     *
     * @param permissions
     * @return
     */
    public static Permission[] filterDeniedPermissions(Permission[] permissions) {
        List<Permission> deniedPermissions = new ArrayList<>(0);
        for (Permission permission : permissions) {
            if (!permission.isGranted()) {
                deniedPermissions.add(permission);
            }
        }
        Logger.d(TAG, "refusedPermissionList.size = " + deniedPermissions.size());
        return deniedPermissions.toArray(new Permission[0]);
    }

    public static Intent newSpecialPermissionIntentInstance(@SpecialPermission String permission) {
        switch (permission) {
            case SpecialPermission.NOTIFICATION:
                return newNotificationIntent();
            case SpecialPermission.SYSTEM_ALERT_WINDOW:
                return newSystemAlertIntent();
            case SpecialPermission.UNKNOWN_APP_SOURCE:
                return newUnknownAppSourceIntent();
            default:
                return newAppManageIntent();
        }
    }

    /**
     * new app 管理页面的 intent
     *
     * @return
     */
    public static Intent newAppManageIntent() {
        Intent intent = new Intent();
        try {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts(Constants.PACKAGE, getPackageName(), null));
        } catch (Exception e) {
            e.printStackTrace();
            intent.setAction(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS);
        }
        return intent;
    }

    /**
     * new 通知开关 intent
     *
     * @return
     */
    public static Intent newNotificationIntent() {
        Intent intent = new Intent();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, getApplicationInfo().uid);
                Logger.d(TAG, "newNotificationIntent on O");
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.putExtra(Constants.APP_PACKAGE, getPackageName());
                intent.putExtra(Constants.APP_UID, getApplicationInfo().uid);
                Logger.d(TAG, "newNotificationIntent on L-N");
            } else {
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts(Constants.PACKAGE, getPackageName(), null));
                Logger.d(TAG, "newNotificationIntent below L");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d(TAG, e.toString());
            intent = newAppManageIntent();
        }
        return intent;
    }

    /**
     * new 系统弹窗授权 intent
     *
     * @return
     */
    public static Intent newSystemAlertIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.fromParts(Constants.PACKAGE, getPackageName(), null));
        }
        return newAppManageIntent();
    }

    /**
     * new 未知来源安装权限 intent
     *
     * @return
     */
    public static Intent newUnknownAppSourceIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        }
        return null;
    }

}
