package com.llm.permission.simple;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.llm.permission.PermissionHelper;
import com.llm.permission.bean.Permission;
import com.llm.permission.bean.SpecialPermission;
import com.llm.permission.callback.PermissionCallback;
import com.llm.permission.callback.PermissionsCallback;
import com.llm.permission.callback.SpecialPermissionCallback;
import com.llm.permission.log.Logger;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onCallPhone(View view) {
        PermissionHelper.getInstance().checkAndRequestPermission(
                Manifest.permission.CALL_PHONE, new PermissionCallback() {
                    @Override
                    public void onPermissionGranted(Permission permission) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Uri uri = Uri.parse("tel://10086");
                        intent.setData(uri);
                        startActivity(intent);
                        Logger.d("授予权限", permission.toString());
                    }

                    @Override
                    public void onPermissionDenied(Permission permission) {
                        Logger.d("权限被拒绝", permission.toString());
                    }
                });
    }

    public void requestPermissions(View view) {
        PermissionHelper.getInstance().checkAndRequestPermission(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                new PermissionsCallback() {
                    @Override
                    public void onAllPermissionGranted(Permission[] permissions) {
                        Logger.d("授予权限", Arrays.toString(permissions));
                    }

                    @Override
                    public void onPermissionDenied(Permission[] permissions) {
                        Logger.d("权限被拒绝", Arrays.toString(permissions));

                    }
                });
    }

    public void modifySettings(View view) {
        PermissionHelper.getInstance().checkAndRequestPermission(SpecialPermission.WRITE_SETTINGS,
                new SpecialPermissionCallback() {
                    @Override
                    public void onGranted(String permission) {
                        Logger.d("授予权限", permission);
                    }

                    @Override
                    public void onDenied(String permission) {
                        Logger.d("权限被拒绝", permission);
                    }
                });
    }

    public void drawOverlay(View view) {
        PermissionHelper.getInstance().checkAndRequestPermission(SpecialPermission.SYSTEM_ALERT_WINDOW,
                new SpecialPermissionCallback() {
                    @Override
                    public void onGranted(String permission) {
                        Logger.d("授予权限", permission);
                    }

                    @Override
                    public void onDenied(String permission) {
                        Logger.d("权限被拒绝", permission);
                    }
                });
    }

    public void modifyNotification(View view) {
        PermissionHelper.getInstance().checkAndRequestPermission(SpecialPermission.NOTIFICATION,
                new SpecialPermissionCallback() {
                    @Override
                    public void onGranted(String permission) {
                        Logger.d("授予权限", permission);
                    }

                    @Override
                    public void onDenied(String permission) {
                        Logger.d("权限被拒绝", permission);
                    }
                });
    }

    public void installPermission(View view) {
        PermissionHelper.getInstance().checkAndRequestPermission(SpecialPermission.UNKNOWN_APP_SOURCE,
                new SpecialPermissionCallback() {
                    @Override
                    public void onGranted(String permission) {
                        Logger.d("授予权限", permission);
                    }

                    @Override
                    public void onDenied(String permission) {
                        Logger.d("权限被拒绝", permission);
                    }
                });
    }


    public void checkPermission(View view) {
        Class c;
        try {
            c = Class.forName("android.app.ActivityManager");
            Method m = c.getMethod("checkUidPermission", String.class, int.class);
            int result = (int) m.invoke(null, Manifest.permission.SYSTEM_ALERT_WINDOW, Binder.getCallingUid());
            Logger.d("ActivityManager,checkUidPermission", "result==PERMISSION_GRANTED:"
                    + (result == PackageManager.PERMISSION_GRANTED));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
