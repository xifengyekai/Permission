package com.llm.permission.callback;

/**
 * 请求权限状态检查的回调
 *
 * @author v_luoliming01
 * @date 2019-07-05 10:50
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
