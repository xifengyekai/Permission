package com.llm.permission.callback;

import com.llm.permission.bean.Permission;

/**
 * 请求权限会回调
 *
 * @author v_luoliming01
 */
public interface PermissionsCallback {
    /**
     * 当所有权限被授予
     *
     * @param permissions 请求的权限
     */
    void onAllPermissionGranted(Permission[] permissions);

    /**
     * 当权限被拒绝
     *
     * @param permissions 请求的权限
     */
    void onPermissionDenied(Permission[] permissions);
}
