package com.hellocsl.demo.dexpreverified;

import com.hellocsl.demo.plugin.FeatureA;

/**
 * Created by chensuilun on 2017/12/26.
 */
public class FeatureAImpl implements FeatureA {
    @Override
    public String getSign() {
        return "HelloWorld";
    }
}
