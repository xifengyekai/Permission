package com.llm.permission.bean;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 特殊权限
 * 通知开关/系统弹窗/未知来源应用安装权限
 *
 * @author v_luoliming01
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef(value = {
        SpecialPermission.NOTIFICATION,
        SpecialPermission.SYSTEM_ALERT_WINDOW,
        SpecialPermission.UNKNOWN_APP_SOURCE,
        SpecialPermission.WRITE_SETTINGS
})
public @interface SpecialPermission {
    /**
     * 通知权限
     */
    String NOTIFICATION = "notification";
    /**
     * 系统弹窗
     */
    String SYSTEM_ALERT_WINDOW = "system_alert_window";
    /**
     * 安装未知来源应用
     */
    String UNKNOWN_APP_SOURCE = "unknown_app_source";
    /**
     * 修改设置
     */
    String WRITE_SETTINGS = "write_settings";
}
