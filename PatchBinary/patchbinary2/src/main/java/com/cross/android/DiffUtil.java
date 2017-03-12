package com.cross.android;


import android.text.TextUtils;

import java.io.File;

/**
 * @author czl 2017/2/20
 * @Package com.cross.android
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
        System.loadLibrary("ApkPatchLibrary");
    }

    public int applyGenDiff(String oldApkPath, String newApkPath, String patchPath) {
        if (TextUtils.isEmpty(oldApkPath)) {
            return Constant.DIFF_FAIL;
        }
        File oldFile = new File(oldApkPath);
        if (!oldFile.exists()) {
            return Constant.DIFF_FAIL;
        }
        if (TextUtils.isEmpty(newApkPath)) {
            return Constant.DIFF_FAIL;
        }
        File newFile = new File(newApkPath);
        if (!newFile.exists()) {
            return Constant.DIFF_FAIL;
        }
        if (SignUtil.checkMd5(oldApkPath, SignUtil.getMd5ByFile(new File(newApkPath)))) {
            return Constant.DIFF_NONE;
        }
        File patchFile = new File(patchPath);
        FileUtil.deleteFileOrDirectory(patchFile);

        File parent = patchFile.getParentFile();
        FileUtil.createDir(parent.getAbsolutePath());

        int ret = genDiff(oldApkPath, newApkPath, patchPath);
        if (ret == 0 && patchFile.exists()) {
            return Constant.DIFF_SUCCESS;
        }
        FileUtil.deleteFileOrDirectory(patchFile);
        return Constant.DIFF_FAIL;

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