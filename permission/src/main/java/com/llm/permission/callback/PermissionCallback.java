package com.llm.permission.callback;

import com.llm.permission.bean.Permission;

/**
 * 请求权限会回调
 *
 * @author v_luoliming01
 */
public interface PermissionCallback {
    /**
     * 当权限被授予
     *
     * @param permission 请求的权限
     */
    void onPermissionGranted(Permission permission);

    /**
     * 当权限被拒绝
     *
     * @param permission 请求的权限
     */
    void onPermissionDenied(Permission permission);
}
