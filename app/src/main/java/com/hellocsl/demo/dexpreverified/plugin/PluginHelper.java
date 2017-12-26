package com.hellocsl.demo.dexpreverified.plugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * 插件资源帮助类
 * Created by chensuilun on 2016/12/26.
 */
public class PluginHelper implements IDexPlugin, IPluginResource {
    private static final String TAG = "PluginHelper";
    public static final String ADD_ASSET_PATH = "addAssetPath";
    private DexClassLoader mDexClassLoader;
    private Resources mResources;
    private Context mContext;
    private String mApkPath;

    private AssetManager mAssetManager;
    private PluginContext mPluginContext;


    public PluginHelper(Context context, String apkPath) {
        mContext = context;
        mApkPath = apkPath;
        try {
            mAssetManager = AssetManager.class.newInstance();
            Method addAssetPath = mAssetManager.getClass().getMethod(ADD_ASSET_PATH, String.class);
            addAssetPath.invoke(mAssetManager, mApkPath);
            Resources superRes = context.getResources();
            mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resources getResources() {
        return mResources;
    }

    @Override
    public AssetManager getAssetManager() {
        return mAssetManager;
    }

    @Override
    public DexClassLoader getDexClassLoader() {
        if (mDexClassLoader == null) {
            mDexClassLoader = new DexClassLoader(mApkPath, mContext.getDir("dex", 0)
                    .getAbsolutePath(), null, mContext.getClassLoader());
        }
        return mDexClassLoader;
    }

    /**
     * @param pkgName 插件包名
     * @return 插件包的 context ，{@link PluginContext}
     */
    public PluginContext getPluginContext(String pkgName) {
        if (mPluginContext == null) {
            mPluginContext = new PluginContext(pkgName, mAssetManager, mResources, getDexClassLoader(), mContext.getApplicationContext());
        }
        return mPluginContext;
    }

    private File mPluginFile;

    /**
     * @return 文件是否存在
     */
    public boolean isExist() {
        if (mPluginFile == null) {
            mPluginFile = new File(mApkPath);
        }
        return mPluginFile.exists();
    }

}
