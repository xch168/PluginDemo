package com.github.xch168.plugindemo;

import android.os.Build;

import com.github.xch168.plugindemo.util.ReflectUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by XuCanHui on 2019/1/22.
 */
public class HookHelper {

    public static final String TARGET_INTENT = "target_intent";

    public static void hookAMS() throws Exception {
        Object singleton;
        if (Build.VERSION.SDK_INT >= 26) {
            Class<?> clazz = Class.forName("android.app.ActivityManager");
            singleton = ReflectUtil.getField(clazz, null, "IActivityManagerSingleton");
        } else {
            Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
            singleton = ReflectUtil.getField(activityManagerNativeClass, null, "gDefault");
        }
        Class<?> singletonClass = Class.forName("android.util.Singleton");
        Method getMethod = singletonClass.getMethod("get");
        Object iActivityManager = getMethod.invoke(singleton);
        Class<?> iActivityManagerClass = Class.forName("android.app.IActivityManager");
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{iActivityManagerClass}, new IActivityManagerProxy(iActivityManager));
        ReflectUtil.setField(singletonClass, singleton, "mInstance", proxy);
    }
}
