package com.llm.activityresult;

import android.content.Intent;

/**
 * startActivityForResult 回调接口
 *
 * @author v_luoliming01
 */
public interface ResultCallback {
    /**
     * 当收到ActivityResult时回调
     *
     * @param resultCode setResult()中的resultCode
     * @param data       setResult()中 Intent
     */
    void onActivityResult(int resultCode, Intent data);
}
