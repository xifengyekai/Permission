package com.llm.permission.callback;

import com.llm.permission.bean.SpecialPermission;

/**
 * 特殊权限回调
 *
 * @author v_luoliming01
 * @date 2019-07-04 16:55
 */
public interface SpecialPermissionCallback {
    /**
     * 权限ok，可做后续的事情
     *
     * @param permission 权限实体类
     *                   {@link com.llm.permission.bean.SpecialPermission }
     */
    void onGranted(@SpecialPermission String permission);

    /**
     * 权限不ok，被拒绝或者未授予
     *
     * @param permission 权限实体类
     */
    void onDenied(@SpecialPermission String permission);
}
