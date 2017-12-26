package com.hellocsl.demo.dexpreverified.plugin.inflate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xmlpull.v1.XmlPullParser;

/**
 * Created by chensuilun on 2017/12/22.
 */
public class PluginLayoutInflater extends LayoutInflater {
    private LayoutInflater mParent;
    private LayoutInflater mPlugin;

    public PluginLayoutInflater(LayoutInflater parent, LayoutInflater plugin, Context pluginContext) {
        super(plugin, pluginContext);
        this.mParent = parent;
        this.mPlugin = plugin;
    }

    public View inflate(int resource, ViewGroup root) {
        try {
            return this.mPlugin.inflate(resource, root);
        } catch (Exception e) {
            e.printStackTrace();
            return this.mParent.inflate(resource, root);
        }
    }

    public View inflate(XmlPullParser parser, ViewGroup root) {
        try {
            return this.mPlugin.inflate(parser, root);
        } catch (Exception e) {
            e.printStackTrace();
            return this.mParent.inflate(parser, root);
        }
    }

    public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        try {
            return this.mPlugin.inflate(resource, root, attachToRoot);
        } catch (Exception e) {
            e.printStackTrace();
            return this.mParent.inflate(resource, root, attachToRoot);
        }
    }

    public LayoutInflater cloneInContext(Context newContext) {
        return this.mPlugin.cloneInContext(newContext);
    }

    /**
     * @param parent 宿主的 LayoutInflater，具体实现类为 PhoneLayoutInflater
     * @param plugin 插件的 Context
     * @return 代理后的插件可用的 LayoutInflater，主要的 ClassLoader 的替换
     */
    public static PluginLayoutInflater create(LayoutInflater parent, Context plugin) {
        LayoutInflater pluginInflater = parent.cloneInContext(plugin);
        PluginInflaterFactory factory = new PluginInflaterFactory(pluginInflater.getFactory(), plugin.getClassLoader());
        pluginInflater.setFactory(factory);
        return new PluginLayoutInflater(parent, pluginInflater, plugin);
    }
}
