package com.cross.android;

import android.text.TextUtils;

import java.io.File;

/**
 * @author czl 2017/2/20
 * @Package com.cross.android
 * @Title: PatchUtil
 * @Description: (APK端合并差分包工具类)
 * Create DateTime: 2017/2/20
 */

public class PatchUtil {

    static PatchUtil instance;

    public static PatchUtil getInstance() {
        if (instance == null) instance = new PatchUtil();
        return instance;
    }

    static {
        System.loadLibrary("ApkPatchLibrary");
    }


    public int applyPatch(String oldApkPath, String newApkPath, String patchPath, String targetMd5Hex) {
        if (TextUtils.isEmpty(oldApkPath)) {
            return Constant.PATCH_FAIL;
        }
        File oldFile = new File(oldApkPath);
        if (!oldFile.exists()) {
            return Constant.PATCH_FAIL;
        }
        if (TextUtils.isEmpty(newApkPath)) {
            return Constant.PATCH_FAIL;
        }
        if (TextUtils.isEmpty(patchPath)) {
            return Constant.PATCH_FAIL;
        }
        File patchFile = new File(patchPath);
        if (!patchFile.exists()) {
            return Constant.PATCH_FAIL;
        }
        File newFile = new File(newApkPath);
        FileUtil.deleteFileOrDirectory(newFile);

        File parent = newFile.getParentFile();
        FileUtil.createDir(parent.getAbsolutePath());

        int ret = patch(oldApkPath, newApkPath, patchPath);
        if (ret == 0 && newFile.exists()) {
            if (SignUtil.checkMd5(newApkPath, targetMd5Hex)) {
                return Constant.PATCH_SUCCESS;
            } else {
                FileUtil.deleteFileOrDirectory(patchFile);
                FileUtil.deleteFileOrDirectory(newFile);
                return Constant.PATCH_FAIL;
            }
        } else {
            FileUtil.deleteFileOrDirectory(patchFile);
            FileUtil.deleteFileOrDirectory(newFile);
            return Constant.PATCH_FAIL;
        }

    }

    /**
     * native方法 使用路径为oldApkPath的apk与路径为patchPath的补丁包，合成新的apk，并存储于newApkPath
     * <p>
     * 返回：0，说明操作成功
     *
     * @param oldApkPath 示例:/sdcard/old.apk
     * @param newApkPath 示例:/sdcard/new.apk
     * @param patchPath  示例:/sdcard/xx.patch
     * @return
     */
    private native int patch(String oldApkPath, String newApkPath, String patchPath);
}
