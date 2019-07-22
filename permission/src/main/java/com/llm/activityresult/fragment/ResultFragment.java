package com.llm.activityresult.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.llm.activityresult.IStartActivityAction;
import com.llm.activityresult.ResultCallback;
import com.llm.activityresult.util.Tools;

/**
 * 发起startActivityForResult fragment
 *
 * @author v_luoliming01
 */
public class ResultFragment extends Fragment implements IStartActivityAction {
    public static final String TAG = ResultFragment.class.getSimpleName();

    private SparseArray<ResultCallback> mResultCallbacks = new SparseArray<>(0);

    public static ResultFragment newInstance() {
        return new ResultFragment();
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
        if (!Tools.isActivityAvailable(getActivity())) {
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
