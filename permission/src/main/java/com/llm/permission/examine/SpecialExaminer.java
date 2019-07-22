package com.llm.permission.examine;

import android.Manifest;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;

import com.llm.permission.PermissionUtils;
import com.llm.permission.bean.SpecialPermission;


/**
 * 特殊权限检查员
 *
 * @author v_luoliming01
 */
public class SpecialExaminer implements IPermissionExaminer {
    @Override
    public boolean checkPermission(@NonNull @SpecialPermission String permissionName) {
        switch (permissionName) {
            case SpecialPermission.NOTIFICATION:
                return checkNotification();
            case SpecialPermission.SYSTEM_ALERT_WINDOW:
                return checkSystemAlert();
            case SpecialPermission.UNKNOWN_APP_SOURCE:
                return checkUnknownAppSource();
            case SpecialPermission.WRITE_SETTINGS:
                return checkWriteSettins();
            default:
                return true;
        }
    }

    /**
     * 检查通知权限
     *
     * @return
     */
    private boolean checkNotification() {
        return NotificationManagerCompat.from(PermissionUtils.getContext()).areNotificationsEnabled();
    }

    /**
     * 检查系统弹窗权限
     *
     * @return
     */
    private boolean checkSystemAlert() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(PermissionUtils.getContext());
        }
        return new AppOpsPermissionExaminer().checkPermission(Manifest.permission.SYSTEM_ALERT_WINDOW);
    }

    /**
     * 检查未知来源安装权限
     *
     * @return
     */
    private boolean checkUnknownAppSource() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return PermissionUtils.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    /**
     * 检查修改设置权限
     *
     * @return
     */
    private boolean checkWriteSettins() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.System.canWrite(PermissionUtils.getContext());
        }
        return true;
    }

}
