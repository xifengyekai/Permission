package com.llm.activityresult.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.llm.activityresult.IStartActivityAction;
import com.llm.activityresult.ResultCallback;
import com.llm.activityresult.util.Tools;

/**
 * 发起startActivityForResult fragment
 *
 * @author v_luoliming01
 * @date 2019-07-05 14:24
 */
public class ResultSupportFragment extends Fragment implements IStartActivityAction {
    public static final String TAG = ResultSupportFragment.class.getSimpleName();

    private SparseArray<ResultCallback> mResultCallbacks = new SparseArray<>(0);

    public static ResultSupportFragment newInstance(){
        return new ResultSupportFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void startActivityForResult(Intent intent, ResultCallback resultCallback) {
        int requestCode = Tools.generateRequestCode(mResultCallbacks);
        mResultCallbacks.put(requestCode, resultCallback);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!Tools.isActivityAvailable(requireActivity())) {
            // host Activity is not available
            return;
        }
        ResultCallback resultCallback = mResultCallbacks.get(requestCode);
        mResultCallbacks.remove(requestCode);
        if (resultCallback == null) {
            // ResultCallback is null
            return;
        }
        resultCallback.onActivityResult(resultCode, data);
    }
}
