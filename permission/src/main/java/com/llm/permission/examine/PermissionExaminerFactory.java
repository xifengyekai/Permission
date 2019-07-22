package com.llm.permission.examine;

import android.util.SparseArray;

import com.llm.permission.PermissionUtils;
import com.llm.permission.annotation.PermissionType;
import com.llm.permission.log.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 创建权限检查员工厂类
 *
 * @author v_luoliming01
 */
public class PermissionExaminerFactory {
    private static final String TAG = PermissionExaminerFactory.class.getSimpleName();

    private static SparseArray<IPermissionExaminer> sExaminerProxies = null;

    /**
     * 创建权限检查员
     *
     * @param type
     * @return
     */
    public static IPermissionExaminer createExaminer(@PermissionType int type) {
        if (sExaminerProxies == null) {
            sExaminerProxies = new SparseArray<>();
        }
        IPermissionExaminer permissionExaminer = sExaminerProxies.get(type);
        if (permissionExaminer != null) {
            return permissionExaminer;
        }
        permissionExaminer = createExaminerProxy(type);
        sExaminerProxies.put(type, permissionExaminer);
        return permissionExaminer;
    }

    /**
     * 创建代理
     * @param type
     * @return
     */
    private static IPermissionExaminer createExaminerProxy(@PermissionType int type) {
        IPermissionExaminer permissionExaminer = null;
        switch (type) {
            case PermissionType.PERMISSION_DANGEROUS:
            default:
                permissionExaminer = createDangerousExaminer();
                break;
            case PermissionType.PERMISSION_SPECIAL:
                permissionExaminer = createSpecialExaminer();
                break;
        }
        final IPermissionExaminer finalPermissionExaminer = permissionExaminer;
        return (IPermissionExaminer) Proxy.newProxyInstance(
                finalPermissionExaminer.getClass().getClassLoader(),
                finalPermissionExaminer.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Logger.d(TAG, "proxy check permission start");
                        Object object = method.invoke(finalPermissionExaminer, args);
                        Logger.d(TAG, "proxy check permission end");
                        return object;
                    }
                });
    }

    /**
     * 创建危险检查员
     *
     * @return
     */
    private static IPermissionExaminer createDangerousExaminer() {
        if (PermissionUtils.isOldPermissionSystem()) {
            return new AppOpsPermissionExaminer();
        } else {
            return new RuntimePermissionExaminer();
        }
    }

    /**
     * 创建特殊检查员
     *
     * @return
     */
    private static SpecialExaminer createSpecialExaminer() {
        return new SpecialExaminer();
    }
}
