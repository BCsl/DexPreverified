package com.hellocsl.demo.dexpreverified.hotfix;

import android.annotation.TargetApi;
import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by chensuilun on 2017/9/21.
 */
public final class HotFix {


    public static void patch(Context context, String patchDexFile, String patchClassName) {
        if (patchDexFile != null && new File(patchDexFile).exists()) {
            try {
                if (hasDexClassLoader()) {
                    injectAboveEqualApiLevel14(context, patchDexFile, patchClassName);
                } else {
                    injectBelowApiLevel14(context, patchDexFile, patchClassName);

                }
            } catch (Throwable th) {
            }
        }
    }


    private static boolean hasDexClassLoader() {
        try {
            Class.forName("dalvik.system.BaseDexClassLoader");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    @TargetApi(14)
    private static void injectBelowApiLevel14(Context context, String str, String str2)
            throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        PathClassLoader obj = (PathClassLoader) context.getClassLoader();
        DexClassLoader dexClassLoader =
                new DexClassLoader(str, context.getDir("dex", 0).getAbsolutePath(), str, context.getClassLoader());
        dexClassLoader.loadClass(str2);
        setField(obj, PathClassLoader.class, "mPaths",
                appendArray(getField(obj, PathClassLoader.class, "mPaths"), getField(dexClassLoader, DexClassLoader.class,
                        "mRawDexPath")
                ));
        setField(obj, PathClassLoader.class, "mFiles",
                combineArray(getField(obj, PathClassLoader.class, "mFiles"), getField(dexClassLoader, DexClassLoader.class,
                        "mFiles")
                ));
        setField(obj, PathClassLoader.class, "mZips",
                combineArray(getField(obj, PathClassLoader.class, "mZips"), getField(dexClassLoader, DexClassLoader.class,
                        "mZips")));
        setField(obj, PathClassLoader.class, "mDexs",
                combineArray(getField(obj, PathClassLoader.class, "mDexs"), getField(dexClassLoader, DexClassLoader.class,
                        "mDexs")));
        obj.loadClass(str2);
    }

    private static void injectAboveEqualApiLevel14(Context context, String str, String str2)
            throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        Object a = combineArray(getDexElements(getPathList(pathClassLoader)),
                getDexElements(getPathList(
                        new DexClassLoader(str, context.getDir("dex", 0).getAbsolutePath(), str, context.getClassLoader()))));
        Object a2 = getPathList(pathClassLoader);
        setField(a2, a2.getClass(), "dexElements", a);
        pathClassLoader.loadClass(str2);
    }

    private static Object getPathList(Object obj) throws ClassNotFoundException, NoSuchFieldException,
            IllegalAccessException {
        return getField(obj, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    private static Object getDexElements(Object obj) throws NoSuchFieldException, IllegalAccessException {
        return getField(obj, obj.getClass(), "dexElements");
    }

    private static Object getField(Object obj, Class cls, String str)
            throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField.get(obj);
    }

    private static void setField(Object obj, Class cls, String str, Object obj2)
            throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        declaredField.set(obj, obj2);
    }

    private static Object combineArray(Object obj, Object obj2) {
        Class componentType = obj2.getClass().getComponentType();
        int length = Array.getLength(obj2);
        int length2 = Array.getLength(obj) + length;
        Object newInstance = Array.newInstance(componentType, length2);
        for (int i = 0; i < length2; i++) {
            if (i < length) {
                Array.set(newInstance, i, Array.get(obj2, i));
            } else {
                Array.set(newInstance, i, Array.get(obj, i - length));
            }
        }
        return newInstance;
    }

    private static Object appendArray(Object obj, Object obj2) {
        Class componentType = obj.getClass().getComponentType();
        int length = Array.getLength(obj);
        Object newInstance = Array.newInstance(componentType, length + 1);
        Array.set(newInstance, 0, obj2);
        for (int i = 1; i < length + 1; i++) {
            Array.set(newInstance, i, Array.get(obj, i - 1));
        }
        return newInstance;
    }
}
