package com.llm.permission.callback;

import com.llm.permission.bean.Permission;

/**
 * 请求权限会回调
 *
 * @author v_luoliming01
 * @date 2019-07-03 10:24
 */
public interface PermissionCallback {
    /**
     * 当权限被授予
     *
     * @param permission
     */
    void onPermissionGranted(Permission permission);

    /**
     * 当权限被拒绝
     *
     * @param permission
     */
    void onPermissionDenied(Permission permission);
}
