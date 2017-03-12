package com.cross.util;


/**
 * @author czl 2017/2/20
 * @Package com.cross.util
 * @Title: DiffUtil
 * @Description: (APK端实现差分包工具类)
 * Create DateTime: 2017/2/20
 */

public class DiffUtil {

    static DiffUtil instance;

    public static DiffUtil getInstance() {
        if (instance == null) instance = new DiffUtil();
        return instance;
    }

    static {
        System.loadLibrary("bsdiff-win64-dll");
    }

    public int applyGenDiff(String oldApkPath, String newApkPath, String patchPath) {


        int ret = genDiff(oldApkPath, newApkPath, patchPath);
        return ret;

    }

    /**
     * native方法 比较路径为oldPath的apk与newPath的apk之间差异，并生成patch包，存储于patchPath
     * <p/>
     * 返回：0，说明操作成功
     *
     * @param oldApkPath 示例:/sdcard/old.apk
     * @param newApkPath 示例:/sdcard/new.apk
     * @param patchPath  示例:/sdcard/xx.patch
     * @return
     */
    private native int genDiff(String oldApkPath, String newApkPath, String patchPath);
}