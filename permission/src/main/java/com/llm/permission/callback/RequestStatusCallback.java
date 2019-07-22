package com.llm.permission.callback;

/**
 * 请求权限状态检查的回调
 *
 * @author v_luoliming01
 */
public interface RequestStatusCallback {
    /**
     * 状态 ok
     */
    void onStatusOK();

    /**
     * 状态错误
     */
    void onError();
}
