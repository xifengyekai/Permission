package com.llm.permission.bean;

import android.content.pm.PackageManager;

import java.util.Arrays;

/**
 * Permission 包装类
 *
 * @author v_luoliming01
 * @date 2019-06-27 14:25
 */
public class Permission {
    /**
     * 日志标签
     */
    private static final String TAG = Permission.class.getSimpleName();

    /**
     * flag:未授权
     */
    public static final int FLAG_DENIED = 0;
    /**
     * flag:已授权
     */
    public static final int FLAG_GRANTED = 1;
    /**
     * flag：需要给用户一个解释
     */
    private static final int FLAG_SHOULD_RATIONALE = 1 << 1;

    private int mFlag = FLAG_DENIED;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 构造器
     *
     * @param name          权限名称
     * @param isGranted     是否已经授权，取值为PackageManager.PERMISSION_GRANTED/PackageManager.PERMISSION_DENIED
     * @param showRationale 是否需要解释
     */
    public Permission(String name, int isGranted, boolean showRationale) {
        this.name = name;
        if (isGranted == PackageManager.PERMISSION_GRANTED) {
            mFlag |= FLAG_GRANTED;
        }
        if (showRationale) {
            mFlag |= FLAG_SHOULD_RATIONALE;
        }
    }

    public String getName() {
        return name;
    }

    /**
     * 是否已授权
     *
     * @return
     */
    public boolean isGranted() {
        return (mFlag & FLAG_GRANTED) != FLAG_DENIED;
    }

    /**
     * 是否需要给用户一个解释
     *
     * @return
     */
    public boolean isShouldRational() {
        return (mFlag & FLAG_SHOULD_RATIONALE) != 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Permission that = (Permission) o;
        return mFlag == that.mFlag &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{mFlag,name});
    }


    @Override
    public String toString() {
        return "Permission{"
                + "name=" + name
                + ", isGranted=" + isGranted() + '\''
                + ", isShouldRational=" + isShouldRational()
                + '}';
    }
}
