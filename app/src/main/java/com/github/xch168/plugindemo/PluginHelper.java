package com.github.xch168.plugindemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.github.xch168.plugindemo.util.ReflectUtil;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;

/**
 * Created by XuCanHui on 2019/1/22.
 */
public class PluginHelper {

    private static final String TAG = "PluginHelper";

    private static final String CLASS_DEX_PATH_LIST = "dalvik.system.DexPathList";
    private static final String FIELD_PATH_LIST = "pathList";
    private static final String FIELD_DEX_ELEMENTS = "dexElements";

    public static void loadPlugin(Context context, ClassLoader hostClassLoader) throws Exception {
        File pluginFile = context.getExternalFilesDir("plugin");
        Log.i(TAG, "pluginPath:" + pluginFile.getAbsolutePath());
        if (pluginFile == null || !pluginFile.exists() || pluginFile.listFiles().length == 0) {
            Toast.makeText(context, "插件文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        pluginFile = pluginFile.listFiles()[0];
        DexClassLoader pluginClassLoader = new DexClassLoader(pluginFile.getAbsolutePath(), pluginFile.getAbsolutePath(), null, hostClassLoader);
        Object pluginDexPathList = ReflectUtil.getField(BaseDexClassLoader.class, pluginClassLoader, FIELD_PATH_LIST);
        Object pluginElements = ReflectUtil.getField(Class.forName(CLASS_DEX_PATH_LIST), pluginDexPathList, FIELD_DEX_ELEMENTS);

        Object hostDexPathList = ReflectUtil.getField(BaseDexClassLoader.class, hostClassLoader, FIELD_PATH_LIST);
        Object hostElements = ReflectUtil.getField(Class.forName(CLASS_DEX_PATH_LIST), hostDexPathList, FIELD_DEX_ELEMENTS);

        Object array = combineArray(hostElements, pluginElements);
        ReflectUtil.setField(Class.forName(CLASS_DEX_PATH_LIST), hostDexPathList, FIELD_DEX_ELEMENTS, array);
        initPluginResource(context);
        Toast.makeText(context, "插件加载成功", Toast.LENGTH_SHORT).show();
    }

    private static Object combineArray(Object hostElements, Object pluginElements) {
        Class<?> componentType = hostElements.getClass().getComponentType();
        int i = Array.getLength(hostElements);
        int j = Array.getLength(pluginElements);
        int k = i + j;
        Object result = Array.newInstance(componentType, k);
        System.arraycopy(pluginElements, 0, result, 0, j);
        System.arraycopy(hostElements, 0, result, j, i);
        return result;
    }

    public static void initPluginResource(Context context) throws Exception {
        Class<AssetManager> clazz = AssetManager.class;
        AssetManager assetManager = clazz.newInstance();
        Method method = clazz.getMethod("addAssetPath", String.class);
        method.invoke(assetManager, context.getExternalFilesDir("plugin").listFiles()[0].getAbsolutePath());
        Resources pluginResources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        ((App)context.getApplicationContext()).setPluginResources(pluginResources);
    }

}
