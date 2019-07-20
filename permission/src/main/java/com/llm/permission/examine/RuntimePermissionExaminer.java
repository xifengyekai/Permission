package com.llm.permission.examine;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.llm.permission.PermissionUtils;
import com.llm.permission.log.Logger;


/**
 * 动态权限检查
 *
 * @author v_luoliming01
 * @date 2019-07-03 11:58
 */
public class RuntimePermissionExaminer implements IPermissionExaminer {
    @Override
    public boolean checkPermission(@NonNull String permissionName) {
        Logger.d(RuntimePermissionExaminer.class.getSimpleName(), permissionName);
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                PermissionUtils.getTopActivity(), permissionName);
    }
}
