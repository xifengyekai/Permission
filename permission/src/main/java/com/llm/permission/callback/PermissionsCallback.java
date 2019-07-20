package com.llm.permission.callback;

import com.llm.permission.bean.Permission;

/**
 * 请求权限会回调
 *
 * @author v_luoliming01
 * @date 2019-07-03 10:24
 */
public interface PermissionsCallback {
    /**
     * 当所有权限被授予
     *
     * @param permissions
     */
    void onAllPermissionGranted(Permission[] permissions);

    /**
     * 当权限被拒绝
     *
     * @param permissions
     */
    void onPermissionDenied(Permission[] permissions);
}
