package com.github.xch168.plugin;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PluginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
    }

    @Override
    public Resources getResources() {
        return getApplication() != null && getApplication().getResources() != null ? getApplication().getResources() : super.getResources();
    }
}
