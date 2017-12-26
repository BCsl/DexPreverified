package com.hellocsl.demo.dexpreverified.plugin;

import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * Created by chensuilun on 2017/10/10.
 */
public interface IPluginResource {
    /**
     * @return 插件包资源
     */
    Resources getResources();

    /**
     * @return
     */
    AssetManager getAssetManager();
}
