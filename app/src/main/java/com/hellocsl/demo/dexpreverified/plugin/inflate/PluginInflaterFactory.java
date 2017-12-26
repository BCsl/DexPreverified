package com.hellocsl.demo.dexpreverified.plugin.inflate;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.hellocsl.demo.dexpreverified.BuildConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by chensuilun on 2017/12/22.
 */
public class PluginInflaterFactory implements LayoutInflater.Factory {
    private static final String TAG = "PluginInflaterFactory";
    private LayoutInflater.Factory mBaseFactory;
    private ClassLoader mClassLoader;

    public PluginInflaterFactory(LayoutInflater.Factory base, ClassLoader classLoader) {
        if (null == classLoader) {
            throw new IllegalArgumentException("classLoader is null");
        } else {
            this.mBaseFactory = base;
            this.mClassLoader = classLoader;
        }
    }

    public View onCreateView(String s, Context context, AttributeSet attributeSet) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateView() called with: " + "s = [" + s + "], context = [" + context + "]");
        }
        View v = null;
        try {
            //插件的自定义 view 加载需要用插件的 ClassLoader
            if (s != null && s.contains(".")) {
                Class<?> clazz = Class.forName(s, true, this.mClassLoader);
                Constructor c = clazz.getConstructor(new Class[]{Context.class, AttributeSet.class});
                v = (View) c.newInstance(new Object[]{context, attributeSet});
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return v != null ? v : (this.mBaseFactory != null ? this.mBaseFactory.onCreateView(s, context, attributeSet) : null);
    }
}

