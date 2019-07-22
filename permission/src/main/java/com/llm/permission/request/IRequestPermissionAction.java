package com.llm.permission.request;

import com.llm.permission.bean.SpecialPermission;
import com.llm.permission.callback.IRequestPermissionsListener;
import com.llm.permission.callback.SpecialPermissionCallback;

/**
 * 请求权限
 *
 * @author v_luoliming01
 */
public interface IRequestPermissionAction {

    /**
     * 请求危险权限
     *
     * @param permissions         权限实体{@link android.Manifest.permission}
     * @param permissionsListener 结果回调
     */
    void requestPermissions(String[] permissions, IRequestPermissionsListener permissionsListener);

    /**
     * 请求特殊权限
     *
     * @param permission         权限 {@link com.llm.permission.bean.SpecialPermission}
     * @param permissionCallback 结果回调
     */
    void requestSpecialPermission(@SpecialPermission String permission, SpecialPermissionCallback permissionCallback);
}
