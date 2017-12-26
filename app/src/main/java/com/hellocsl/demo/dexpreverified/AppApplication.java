package com.hellocsl.demo.dexpreverified;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.hellocsl.demo.dexpreverified.hotfix.HotFix;
import com.hellocsl.demo.dexpreverified.utils.FileUtils;

import java.io.File;

/**
 * Created by chensuilun on 2017/12/26.
 */
public class AppApplication extends Application {
    private static final String TAG = "AppApplication";
    public static final String DEX_NAME = "feature.dex";
    public static final String PATCH_A_NAME = "pluginimpl-patchA.zip";
    public static final String PATCH_B_NAME = "pluginimpl-patchB.zip";
    public static final String CLASS_NAME = "com.hellocsl.demo.plugin.FeatureA";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dexFile = new File(path, DEX_NAME);
        File patchAFile = new File(path, PATCH_A_NAME);
        File patchBFile = new File(path, PATCH_B_NAME);
        if (!dexFile.exists()) {
            FileUtils.copyAssets(base, DEX_NAME, dexFile.getAbsolutePath());
        }
        HotFix.patch(base, dexFile.getAbsolutePath(), CLASS_NAME);
        Log.e(TAG, "attachBaseContext: " + getClassLoader());
        if (!patchAFile.exists()) {
            FileUtils.copyAssets(base, PATCH_A_NAME, patchAFile.getAbsolutePath());
        }
        if (!patchBFile.exists()) {
            FileUtils.copyAssets(base, PATCH_B_NAME, patchBFile.getAbsolutePath());
        }
    }
}
