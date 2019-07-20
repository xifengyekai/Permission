package com.llm.permission.examine;

import android.support.annotation.NonNull;

/**
 * 权限检查员
 *
 * @author v_luoliming01
 * @date 2019-07-03 11:55
 */
public interface IPermissionExaminer {
    /**
     * 检查权限
     *
     * @param permissionName 权限名称数组
     * @return 检查后的权限数组
     */
    boolean checkPermission(@NonNull String permissionName);
}
