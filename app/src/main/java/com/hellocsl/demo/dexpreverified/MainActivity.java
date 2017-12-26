package com.hellocsl.demo.dexpreverified;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hellocsl.demo.dexpreverified.plugin.PluginHelper;
import com.hellocsl.demo.plugin.FeatureA;

import static com.hellocsl.demo.dexpreverified.AppApplication.PATCH_A_NAME;
import static com.hellocsl.demo.dexpreverified.AppApplication.PATCH_B_NAME;

/***
 * 测试1：使用 provided 提供依赖的时候只会在编译的时候生效，而运行是没有具体依赖的
 *  如果项目是 library 项目，那么具体的依赖可以通过使用该 library 的提供真实依赖，这也是开发 library 解决多重依赖和兼容的维他奶
 *  如果项目是 application 项目，那么可以通过动态的插入 dex 文件的形式来提供依赖
 *
 * 测试2：防止特定的类被打上 CLASS_ISPREVERIFIED 标志
 *  HotFix 一般的方案是每一个类（除了 Application）都直接引用一个其他 dex 的类，详情见 https://mp.weixin.qq.com/s?__biz=MzI1MTA1MzM2Nw==&mid=400118620&idx=1&sn=b4fdd5055731290eef12ad0d17f39d4a&scene=1&srcid=1106Imu9ZgwybID13e7y2nEi#wechat_redirect
 *  HotFix 应对的是需要热修复类不可预测，如果可预见的某部分接口呢？那么就可以把这部分独立到一个 library 单独打包成 dex 文件，
 *  然后单独插入到主程序，这样就可以实现这部分功能的动态变更，而且不用重启，因为插件包的 dex 不需要添加到主工程的 ClassLoader 下
 *  还有个问题就是主程序、插件包的对动态变更的 library 的依赖需要用 provided
 *
 *
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String FEATURE_CLASS = "com.hellocsl.demo.pluginimpl.FeaturePatchImpl";
    TextView mTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = findViewById(R.id.main_tv);
        final PluginHelper pluginA = new PluginHelper(this, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + PATCH_A_NAME);
        final PluginHelper pluginB = new PluginHelper(this, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + PATCH_B_NAME);
        findViewById(R.id.main_btn_patcha).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pluginA.getDexClassLoader() != null) {
                    try {
                        FeatureA featureA = (FeatureA) pluginA.getDexClassLoader().loadClass(FEATURE_CLASS).newInstance();
                        mTv.setText(featureA.getSign());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onClick:patch A classloader null");
                }
            }
        });
        findViewById(R.id.main_btn_patchb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pluginB.getDexClassLoader() != null) {
                    try {
                        FeatureA featureA = (FeatureA) pluginB.getDexClassLoader().loadClass(FEATURE_CLASS).newInstance();
                        mTv.setText(featureA.getSign());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onClick:patch B classloader null");
                }
            }
        });
        FeatureA a = new FeatureAImpl();
        mTv.setText(a.getSign());
    }
}
