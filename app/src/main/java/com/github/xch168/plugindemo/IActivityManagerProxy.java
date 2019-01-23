package com.github.xch168.plugindemo;

import android.content.Intent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by XuCanHui on 2019/1/22.
 */
public class IActivityManagerProxy implements InvocationHandler {
    private static final String TAG = "IActivityManagerProxy";

    private Object mActivityManager;

    public IActivityManagerProxy(Object activityManager) {
        mActivityManager = activityManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startActivity".equals(method.getName())) {
            Intent intent;
            int index = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            intent = (Intent) args[index];
            Intent stubIntent = new Intent();
            String packageName = "com.github.xch168.plugindemo";
            stubIntent.setClassName(packageName, packageName + ".StubActivity");
            stubIntent.putExtra(HookHelper.TARGET_INTENT, intent);
            args[index] = stubIntent;
        }
        return method.invoke(mActivityManager, args);
    }
}
