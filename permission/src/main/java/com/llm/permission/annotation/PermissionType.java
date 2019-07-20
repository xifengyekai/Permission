package com.llm.permission.annotation;

import android.support.annotation.IntDef;

/**
 * 权限类型
 *
 * @author v_luoliming01
 * @date 2019-07-03 16:00
 */
@IntDef(value = {PermissionType.PERMISSION_DANGEROUS, PermissionType.PERMISSION_SPECIAL})
public @interface PermissionType {
    /**
     * 危险权限，6.0需要动态申请
     */
    int PERMISSION_DANGEROUS = 0;
    /**
     * 特殊权限，需要发送请求用户授权的意图。系统通过向用户显示详细的管理屏幕来响应意图。
     */
    int PERMISSION_SPECIAL = 1;
}