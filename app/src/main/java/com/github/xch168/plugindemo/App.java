package com.github.xch168.plugindemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by XuCanHui on 2019/1/22.
 */
public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        try {
            HookHelper.hookAMS();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
