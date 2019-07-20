package com.llm.permission;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.llm.permission.annotation.PermissionType;
import com.llm.permission.bean.Permission;
import com.llm.permission.bean.SpecialPermission;
import com.llm.permission.callback.IRequestPermissionsListener;
import com.llm.permission.callback.PermissionCallback;
import com.llm.permission.callback.PermissionsCallback;
import com.llm.permission.callback.RequestStatusCallback;
import com.llm.permission.callback.SpecialPermissionCallback;
import com.llm.permission.examine.IPermissionExaminer;
import com.llm.permission.examine.PermissionExaminerFactory;
import com.llm.permission.log.Logger;
import com.llm.permission.request.PermissionRequester;

/**
 * 权限辅助器
 *
 * @author v_luoliming01
 * @date 2019-06-27 17:13
 */
public class PermissionHelper {
    private static final String TAG = PermissionHelper.class.getSimpleName();
    /**
     * 是否已经初始化了
     */
    private volatile boolean alreadyInit = false;

    private static class SingletonHolder {
        private static PermissionHelper INSTANCE = new PermissionHelper();
    }

    public static PermissionHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 初始化
     *
     * @param application
     * @return
     */
    public void init(Application application) {
        if (alreadyInit) {
            return;
        }
        PermissionUtils.initApplication(application);
        alreadyInit = true;
        application.registerActivityLifecycleCallbacks(new PermissionActivityLifecycle());
    }

    /**
     * 自动初始化
     *
     * @param context Application Context
     * @return
     */
    public void autoInit(Context context) {
        if (!(context instanceof Application)) {
            throw new IllegalArgumentException("context must be application context");
        }
        init((Application) context);
    }

    /**
     * 是否进行debug
     *
     * @param debug
     */
    public void setDebug(boolean debug) {
        Logger.setDebug(debug);
    }

    /**
     * 检查并且请求特殊权限
     *
     * @param permission         权限名称
     * @param permissionCallback 结果回调
     */
    public void checkAndRequestPermission(@SpecialPermission String permission,
                                          SpecialPermissionCallback permissionCallback) {
        if (permissionCallback == null) {
            Logger.d(TAG, "SpecialPermissionCallback must be not null");
            return;
        }
        boolean isGranted = checkSpecialPermission(permission);
        if (isGranted) {
            permissionCallback.onGranted(permission);
            return;
        }
        int currentOsVersion = Build.VERSION.SDK_INT;
        switch (permission) {
            case SpecialPermission.UNKNOWN_APP_SOURCE:
                if (currentOsVersion < Build.VERSION_CODES.O) {
                    permissionCallback.onDenied(permission);
                    return;
                }
                break;
            case SpecialPermission.NOTIFICATION:
                if (currentOsVersion < Build.VERSION_CODES.KITKAT) {
                    permissionCallback.onDenied(permission);
                    return;
                }
                break;
            case SpecialPermission.SYSTEM_ALERT_WINDOW:
                if (currentOsVersion < Build.VERSION_CODES.M) {
                    permissionCallback.onDenied(permission);
                    return;
                }
                break;
            default:
                break;
        }
        requestSpecialPermission(permission, permissionCallback);
    }

    /**
     * 检查特殊权限
     *
     * @param permission
     * @return
     */
    private boolean checkSpecialPermission(@SpecialPermission String permission) {
        return PermissionExaminerFactory.createExaminer(PermissionType.PERMISSION_SPECIAL)
                .checkPermission(permission);
    }

    /**
     * 请求特殊权限
     *
     * @param permission         权限名称
     * @param permissionCallback 结果回调
     */
    private void requestSpecialPermission(@SpecialPermission final String permission,
                                          final SpecialPermissionCallback permissionCallback) {
        checkRequestStatus(new RequestStatusCallback() {
            @Override
            public void onStatusOK() {
                new PermissionRequester().requestSpecialPermission(permission, permissionCallback);
            }

            @Override
            public void onError() {
                Logger.e(TAG, "request status is error");
            }
        });
    }

    /**
     * 检查并且请求单个权限
     *
     * @param permission         权限名称
     * @param permissionCallback 结果回调
     */
    public void checkAndRequestPermission(final String permission, final PermissionCallback permissionCallback) {
        if (permissionCallback == null) {
            Logger.d(TAG, "PermissionCallback must be not null");
            return;
        }
        if (TextUtils.isEmpty(permission)) {
            Logger.d(TAG, "permission must be not empty");
            return;
        }
        checkAndRequestPermission(new String[]{permission}, new PermissionsCallback() {
            @Override
            public void onAllPermissionGranted(Permission[] permissions) {
                permissionCallback.onPermissionGranted(permissions[0]);
            }

            @Override
            public void onPermissionDenied(Permission[] permissions) {
                permissionCallback.onPermissionDenied(permissions[0]);
            }
        });
    }

    /**
     * 检查并且请求多个权限
     * <p>
     * 1.先检查权限，有些权限可能已经授予了，以6.0为界进行检查
     * 2.过滤被拒绝的权限，如果被拒接的权限为空，表示所有权限都授予了，直接回调成功
     * 3.如果有部分权限被拒绝了，判断是否可以进行动态权限申请
     * 3.1 可以进行动态权限申请，进行动态权限申请
     * 3.2 不可以动态申请，直接回调权限失败
     *
     * @param permissions         权限名称数组
     * @param permissionsCallback 结果回调
     */
    public void checkAndRequestPermission(String[] permissions, final PermissionsCallback permissionsCallback) {
        if (permissionsCallback == null) {
            Logger.d(TAG, "PermissionsCallback must be not null");
            return;
        }
        if (permissions == null) {
            Logger.d(TAG, "permissions must be not null");
            return;
        }
        Permission[] checkedPermissions = checkPermission(
                PermissionExaminerFactory.createExaminer(PermissionType.PERMISSION_DANGEROUS), permissions);
        Permission[] refusedPermission = PermissionUtils.filterDeniedPermissions(checkedPermissions);
        if (refusedPermission.length == 0) {
            permissionsCallback.onAllPermissionGranted(checkedPermissions);
        } else {
            if (PermissionUtils.canRuntimePermission()) {
                requestRuntimePermission(checkedPermissions, permissionsCallback);
            } else {
                permissionsCallback.onPermissionDenied(checkedPermissions);
            }

        }
    }

    /**
     * 代理检查权限
     *
     * @param permissionExaminer 被代理的检查员
     * @param permissionNames    权限数组
     * @return
     */
    private Permission[] checkPermission(final IPermissionExaminer permissionExaminer, String[] permissionNames) {
        Permission[] permissions = new Permission[permissionNames.length];
        int isGranted;
        boolean showRationale = false;
        for (int i = 0; i < permissionNames.length; i++) {
            isGranted = permissionExaminer.checkPermission(permissionNames[i])
                    ? PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED;
            if (PermissionUtils.getTopActivity() != null) {
                showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                        PermissionUtils.getTopActivity(), permissionNames[i]);
            }
            permissions[i] = new Permission(permissionNames[i], isGranted, showRationale);
        }
        return permissions;
    }

    /**
     * 请求动态权限
     * <p>
     * 判断当前是否是主线程，是就直接申请，不是就切换到主线程申请
     *
     * @param permissions         Permission[]
     * @param permissionsCallback 结果回调
     */
    private void requestRuntimePermission(final Permission[] permissions, final PermissionsCallback permissionsCallback) {
        checkRequestStatus(new RequestStatusCallback() {
            @Override
            public void onStatusOK() {
                requestPermission(permissions, permissionsCallback);
            }

            @Override
            public void onError() {
                Logger.e(TAG, "request status is error");
            }
        });
    }

    /**
     * 检查请求前状态
     *
     * @param callback
     */
    private void checkRequestStatus(final RequestStatusCallback callback) {
        if (!PermissionUtils.isActivityAvailable(PermissionUtils.getTopActivity())) {
            Logger.e(TAG, "this top activity is not available");
            callback.onError();
            return;
        }
        if (PermissionUtils.assertMainThread()) {
            callback.onStatusOK();
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    callback.onStatusOK();
                }
            });
        }
    }

    /**
     * 申请权限
     *
     * @param permissions         Permission[]
     * @param permissionsCallback 结果回调
     */
    private void requestPermission(Permission[] permissions, final PermissionsCallback permissionsCallback) {
        new PermissionRequester()
                .requestPermissions(permissions, new IRequestPermissionsListener() {
                    @Override
                    public void onRequestPermissionResult(Permission[] permissions) {
                        Permission[] deniedPermissions = PermissionUtils.filterDeniedPermissions(permissions);
                        if (deniedPermissions.length == 0) {
                            permissionsCallback.onAllPermissionGranted(permissions);
                        } else {
                            permissionsCallback.onPermissionDenied(deniedPermissions);
                        }
                    }
                });
    }


}
