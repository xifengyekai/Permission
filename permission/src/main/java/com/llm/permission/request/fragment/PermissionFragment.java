package com.llm.permission.request.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;

import com.llm.permission.PermissionUtils;
import com.llm.permission.annotation.PermissionType;
import com.llm.permission.bean.Permission;
import com.llm.permission.callback.IRequestPermissionsListener;
import com.llm.permission.callback.SpecialPermissionCallback;
import com.llm.permission.examine.PermissionExaminerFactory;
import com.llm.permission.log.Logger;
import com.llm.permission.request.IRequestPermissionAction;

import static android.os.Build.VERSION_CODES.M;


/**
 * 请求权限fragment（android.app.Fragment）
 *
 * @author v_luoliming01
 */
public class PermissionFragment extends Fragment implements IRequestPermissionAction {
    private static final String TAG = PermissionFragment.class.getSimpleName();
    /**
     * 回调接口
     * 每个request code 对应一个回调
     */
    private SparseArray<IRequestPermissionsListener> mPermissionsListeners = new SparseArray<>();
    /**
     * 特殊权限回调接口
     * 每个request code 对应一个回调
     */
    private SparseArray<SpecialPermissionCallback> mSpecialPermissionCallbacks = new SparseArray<>(0);
    /**
     * 缓存特殊权限
     * 每个request code 对应一个权限
     */
    private SparseArray<String> mSpecialPermissions = new SparseArray<>(0);

    /**
     * newInstance
     *
     * @return PermissionFragment
     */
    public static PermissionFragment newInstance() {
        return new PermissionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @TargetApi(M)
    @Override
    public void requestPermissions(String[] permissions, IRequestPermissionsListener permissionsListener) {
        int requestCode = PermissionUtils.generateRequestCode(mPermissionsListeners);
        mPermissionsListeners.put(requestCode, permissionsListener);
        requestPermissions(permissions, requestCode);
        Logger.d(TAG, "request permission,requestCode is  " + requestCode
                + ", IRequestPermissionsListener is " + permissionsListener);

    }

    @TargetApi(M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        IRequestPermissionsListener permissionsListener = mPermissionsListeners.get(requestCode);
        mPermissionsListeners.remove(requestCode);
        Logger.d(TAG, "request permission on result, requestCode is  " + requestCode
                + "IRequestPermissionsListener is " + permissionsListener);
        // 监听为null 或者当前activity不可用时不回调
        if (permissionsListener == null || !PermissionUtils.isActivityAvailable(getActivity())) {
            return;
        }
        // 封装结果permission
        Permission[] permissionResults = new Permission[permissions.length];
        Permission permission = null;
        for (int i = 0; i < permissions.length; i++) {
            permissionResults[i] = new Permission(
                    permissions[i], grantResults[i], shouldShowRequestPermissionRationale(permissions[i]));
        }
        // 回调
        permissionsListener.onRequestPermissionResult(permissionResults);
    }

    @Override
    public void requestSpecialPermission(String permission, SpecialPermissionCallback permissionCallback) {
        Intent intent = PermissionUtils.newSpecialPermissionIntentInstance(permission);
        if (intent == null) {
            return;
        }
        int requestCode = PermissionUtils.generateRequestCode(mSpecialPermissionCallbacks);
        mSpecialPermissions.put(requestCode, permission);
        mSpecialPermissionCallbacks.put(requestCode, permissionCallback);
        startActivityForResult(intent, requestCode);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!PermissionUtils.isActivityAvailable(getActivity())) {
            Logger.d(TAG, "host activity is not available.");
            return;
        }
        SpecialPermissionCallback permissionCallback = mSpecialPermissionCallbacks.get(requestCode);
        mSpecialPermissionCallbacks.remove(requestCode);
        String permission = mSpecialPermissions.get(requestCode);
        mSpecialPermissions.remove(requestCode);
        if (permissionCallback == null || TextUtils.isEmpty(permission)) {
            Logger.d(TAG, "SpecialPermissionCallback is null or SpecialPermission is empty.");
            return;
        }
        boolean isGranted = PermissionExaminerFactory.createExaminer(PermissionType.PERMISSION_SPECIAL)
                .checkPermission(permission);
        if (isGranted) {
            permissionCallback.onGranted(permission);
        } else {
            permissionCallback.onDenied(permission);
        }
    }
}
