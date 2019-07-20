package com.llm.activityresult;

import android.content.Intent;

/**
 * startActivity Action
 *
 * @author v_luoliming01
 * @date 2019-07-05 14:33
 */
public interface IStartActivityAction {
    /**
     * startActivityForResult
     *
     * @param intent         start Intent
     * @param resultCallback 结果回调
     */
    void startActivityForResult(Intent intent, ResultCallback resultCallback);
}
