package com.llm.permission.request;


import com.llm.permission.PermissionUtils;
import com.llm.permission.bean.Permission;
import com.llm.permission.bean.SpecialPermission;
import com.llm.permission.callback.IRequestPermissionsListener;
import com.llm.permission.callback.SpecialPermissionCallback;

/**
 * 权限请求器
 *
 * @author v_luoliming01
 * @date 2019-06-28 10:19
 */
public class PermissionRequester {
    private static final String TAG = PermissionRequester.class.getSimpleName();

    /**
     * 请求危险权限
     *
     * @param permissions         权限数组
     * @param permissionsListener 回调监听
     */
    public void requestPermissions(Permission[] permissions, IRequestPermissionsListener permissionsListener) {
        PermissionFragmentFactory.createFragmentProxy()
                .requestPermissions(PermissionUtils.buildPermissions(permissions), permissionsListener);
    }

    /**
     * 请求特殊权限
     *
     * @param specialPermission         特殊权限
     * @param specialPermissionCallback 特殊权限回调
     */
    public void requestSpecialPermission(@SpecialPermission String specialPermission,
                                         SpecialPermissionCallback specialPermissionCallback) {
        PermissionFragmentFactory.createFragmentProxy()
                .requestSpecialPermission(specialPermission, specialPermissionCallback);
    }
}
