package com.github.xch168.plugindemo;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by XuCanHui on 2019/1/22.
 */
public class App extends Application {

    private Resources mPluginResources;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        try {
            //HookHelper.hookAMS();
            HookHelper.hookInstrumentation(base);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPluginResources(Resources resources) {
        mPluginResources = resources;
    }

    @Override
    public Resources getResources() {
        return mPluginResources == null ? super.getResources() : mPluginResources;
    }

}
