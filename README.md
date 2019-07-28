# Permission
android runtime permission library/Android 运行时权限库

基于 soulPermission 库进行优化：
1.优化权限检查
2.权限检查及请求时使用动态代理
3.添加startActivityForResult及结果回调功能

# 引用库
两种方式任选一种，引入后无需手动初始化，permission库内部已经做了自动初始化

方式一.引用源码，前往https://github.com/xifengyekai/Permission下载源码并以module形式引入。
在应用module build.gradle 的 dependencies 中添加 implementation project(":permission")

方式二.直接在应用module build.gradle 的 dependencies 中添加 
    implementation 'com.llm.permission:permission:1.0.0'

注意：如果是Android X 项目引入本库需要在项目 gradle.properties 中做如下配置：

    1.方式一引入需添加：android.useAndroidX=true
    2.方式二引入需添加：android.enableJetifier=true
    
# 用法
1.请求Runtime Permission:

    单个权限：
        PermissionHelper.getInstance().checkAndRequestPermission(
                        Manifest.permission.CALL_PHONE, new PermissionCallback() {
                            @Override
                            public void onPermissionGranted(Permission permission) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Uri uri = Uri.parse("tel://10086");
                                intent.setData(uri);
                                if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                                    startActivity(intent);
                                } else {
                                    // 找不到对应Activity
                                }
                            }
                            @Override
                            public void onPermissionDenied(Permission permission) {
                                // 权限被拒绝
                            }
                        });
    多个权限：
        PermissionHelper.getInstance().checkAndRequestPermission(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        new PermissionsCallback() {
                            @Override
                            public void onAllPermissionGranted(Permission[] permissions) {
                                // 所有权限被授予
                            }
                            @Override
                            public void onPermissionDenied(Permission[] permissions) {
                                // 有权限被拒绝，permissions被决绝的权限
        
                            }
                        });
                        
2.请求特殊权限：

    PermissionHelper.getInstance().checkAndRequestPermission(SpecialPermission.SYSTEM_ALERT_WINDOW,
                    new SpecialPermissionCallback() {
                        @Override
                        public void onGranted(String permission) {
                            // 授予权限
                        }
                        @Override
                        public void onDenied(String permission) {
                            // 权限被拒绝
                        }
                    });
    特殊权限说明：SpecialPermission.SYSTEM_ALERT_WINDOW,允许创建显示在其他应用上的window，
                                                      也可以使dialog覆盖在statusbar和navigationbar上
                SpecialPermission.WRITE_SETTINGS,修改系统设置（system app才有效）
                SpecialPermission.NOTIFICATION,通知开关
                SpecialPermission.UNKNOWN_APP_SOURCE,8.0安装未知来源的应用权限

3.startActivityForResult

    step1:
        ActivityResultHelper.init(activity)
                        .startActivityForResult(SetResultActivity.class, new ResultCallback() {
                            @Override
                            public void onActivityResult(int resultCode, Intent data) {
                                // 收到结果
                            }
                        });
    step2:  
        setResult(code, intent);
        finish();
