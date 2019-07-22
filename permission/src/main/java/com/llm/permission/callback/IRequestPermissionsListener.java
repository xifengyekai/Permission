package com.llm.permission.callback;

import com.llm.permission.bean.Permission;

/**
 * 请求权限监听器
 *
 * @author v_luoliming01
 */
public interface IRequestPermissionsListener {

    /**
     * 请求权限结果
     *
     * @param permissions 权限结果
     */
    void onRequestPermissionResult(Permission[] permissions);
}
