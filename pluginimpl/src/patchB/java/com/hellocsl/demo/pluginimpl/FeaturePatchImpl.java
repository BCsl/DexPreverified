package com.hellocsl.demo.pluginimpl;

import com.hellocsl.demo.plugin.FeatureA;

/**
 * Created by chensuilun on 2017/12/26.
 */
public class FeaturePatchImpl extends FeatureA {
    @Override
    public String getSign() {
        return "PatchB Sign";
    }
}
