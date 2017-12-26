package com.hellocsl.demo.dexpreverified.plugin;

import dalvik.system.DexClassLoader;

/**
 * Dex 插件
 * <p>
 * Created by chensuilun on 2017/10/10.
 */
public interface IDexPlugin {

    /**
     * @return 插件包的类加载器
     */
    DexClassLoader getDexClassLoader();

}
