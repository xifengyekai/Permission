package com.llm.permission.examine;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.support.annotation.NonNull;

import com.llm.permission.PermissionUtils;
import com.llm.permission.log.Logger;

import java.lang.reflect.Method;

/**
 * AppOpsManager 检查权限
 *
 * @author v_luoliming01
 */
public class AppOpsPermissionExaminer implements IPermissionExaminer {
    private static final String TAG = AppOpsPermissionExaminer.class.getSimpleName();

    private AppOpsManager appOpsManager;
    /**
     * 检查 operation 的方法
     */
    private Method checkOp;

    /**
     * 检查权限
     *
     * @param permission 权限
     * @return 是否授予权限
     */
    @Override
    public boolean checkPermission(@NonNull String permission) {
        switch (permission) {
            // dangerous permission
            // calendar group
            case Manifest.permission.READ_CALENDAR:
                return checkOp(8);
            case Manifest.permission.WRITE_CALENDAR:
                return checkOp(9);
            // call log group
            case Manifest.permission.READ_CALL_LOG:
                return checkOp(6);
            case Manifest.permission.WRITE_CALL_LOG:
                return checkOp(7);
            // camera group
            case Manifest.permission.CAMERA:
                return checkOp(26);
            // contact group
            case Manifest.permission.READ_CONTACTS:
                return checkOp(4);
            case Manifest.permission.WRITE_CONTACTS:
                return checkOp(5);
            // location group
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                return checkOp(0);
            case Manifest.permission.ACCESS_FINE_LOCATION:
                return checkOp(1);
            // microphone group
            case Manifest.permission.RECORD_AUDIO:
                return checkOp(27);
            // phone group
            case Manifest.permission.CALL_PHONE:
                return checkOp(13);
            case Manifest.permission.READ_PHONE_STATE:
                return checkOp(51);
            case Manifest.permission.READ_PHONE_NUMBERS:
                return checkOp(65);
            case Manifest.permission.ANSWER_PHONE_CALLS:
                return checkOp(69);
            case Manifest.permission.ADD_VOICEMAIL:
                return checkOp(52);
            case Manifest.permission.USE_SIP:
                return checkOp(53);
            // sensors group
            case Manifest.permission.BODY_SENSORS:
                return checkOp(56);
            // sms group
            case Manifest.permission.READ_SMS:
                return checkOp(14);
            case Manifest.permission.RECEIVE_SMS:
                return checkOp(16);
            case Manifest.permission.RECEIVE_MMS:
                return checkOp(18);
            case Manifest.permission.RECEIVE_WAP_PUSH:
                return checkOp(19);
            case Manifest.permission.SEND_SMS:
                return checkOp(20);
            // storage group
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return checkOp(59);
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return checkOp(60);
                // special permission
            case Manifest.permission.SYSTEM_ALERT_WINDOW:
                return checkOp(24);
            default:
                return true;
        }
    }

    /**
     * 反射调用 AppOpsManager.checkOp(int op, int uid, String packageName)
     *
     * @param op operation
     * @return
     */
    private boolean checkOp(int op) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Logger.d(TAG, "sdk api below 4.4");
            return true;
        }
        if (appOpsManager == null) {
            appOpsManager = (AppOpsManager) PermissionUtils.getContext()
                    .getSystemService(Context.APP_OPS_SERVICE);
        }
        try {
            if (checkOp == null) {
                checkOp = AppOpsManager.class.getMethod("checkOp", int.class, int.class, String.class);
            }
            return AppOpsManager.MODE_ALLOWED == (int) checkOp.invoke(appOpsManager,
                    op, Binder.getCallingUid(), PermissionUtils.getContext().getPackageName());
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
            e.printStackTrace();
        }
        return true;
    }
}
