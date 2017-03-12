package com.cross.app;

import android.app.Application;

import com.cross.android.FileUtil;


/**
 * @author czl 2017/2/25
 * @Package com.cross.app
 * @Title: App
 * @Description: (应用入口)
 * Create DateTime: 2017/2/25
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileUtil.createDir(FileUtil.getCachePath(getApplicationContext()));
    }
}
