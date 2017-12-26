package com.hellocsl.demo.dexpreverified.plugin;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;

import com.hellocsl.demo.dexpreverified.BuildConfig;
import com.hellocsl.demo.dexpreverified.plugin.inflate.PluginLayoutInflater;

import java.io.File;

import dalvik.system.DexClassLoader;

/**
 * 插件类使用的Context
 * Created by chensuilun on 2016/12/28.
 */
public class PluginContext extends ContextWrapper {
    private static final String TAG = "PluginContext";
    private String mPluginPackageName;
    private AssetManager mPluginAssetManager;
    private Resources mPluginResources;
    private DexClassLoader mPluginDexClassLoader;
    private Context mApplicationContext;
    private PluginLayoutInflater mLayoutInflater;
    private Resources.Theme mTheme;

    /**
     * @param pkgName              插件包名
     * @param pluginAssetManager   插件的AssetManager
     * @param pluginResources      插件包的Resources
     * @param pluginDexClassLoader 插件包的DexClassLoader
     * @param applicationContext   宿主应用的Context
     */
    public PluginContext(String pkgName, AssetManager pluginAssetManager, Resources pluginResources, DexClassLoader pluginDexClassLoader, Context applicationContext) {
        super(applicationContext);
        mPluginPackageName = pkgName;
        mPluginAssetManager = pluginAssetManager;
        mPluginResources = pluginResources;
        mPluginDexClassLoader = pluginDexClassLoader;
        mApplicationContext = applicationContext;
        this.mTheme = pluginResources.newTheme();
        this.mTheme.setTo(mApplicationContext.getTheme());
    }

    @Override
    public AssetManager getAssets() {
        return mPluginAssetManager;
    }

    @Override
    public Resources getResources() {
        return mPluginResources;
    }


    @Override
    public ClassLoader getClassLoader() {
        return mPluginDexClassLoader;
    }

    @Override
    public String getPackageName() {
        return mPluginPackageName;
    }


    @Override
    public boolean moveSharedPreferencesFrom(Context sourceContext, String name) {
        return false;
    }

    @Override
    public boolean deleteSharedPreferences(String name) {
        return false;
    }

    @Override
    public boolean deleteFile(String name) {
        return false;
    }

    @Override
    public File getFileStreamPath(String name) {
        return mApplicationContext.getFileStreamPath(name);
    }

    public Resources.Theme getTheme() {
        return this.mTheme;
    }

    @Override
    public Object getSystemService(String name) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getSystemService() called with: " + "name = [" + name + "]");
        }
        Object host = super.getSystemService(name);
        if (host instanceof LayoutInflater) {
            /**
             * 插件包可能需要加载布局文件，就需要用到 LayoutInflater，这里获取到的 LayoutInflater 只能
             */
            if (this.mLayoutInflater == null) {
                this.mLayoutInflater = PluginLayoutInflater.create((LayoutInflater) host, this);
            }
            host = this.mLayoutInflater;
        }
        return host;
    }

    @Override
    public boolean deleteDatabase(String name) {
        return false;
    }

}
