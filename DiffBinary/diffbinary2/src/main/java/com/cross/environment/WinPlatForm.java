package com.cross.environment;

import com.cross.util.DiffUtil;

import java.io.IOException;

/**
 * @author czl 2017/2/18
 * @Package com.cross.environment
 * @Title: MacPlatForm
 * @Description: (windows操作系统平台)
 * Create DateTime: 2017/2/18
 */
public class WinPlatForm implements Platform {
    @Override
    public Process execDiff(String oldApkPath, String newApkPath, String patchPath) throws IOException {
        Process process = Runtime.getRuntime().exec("cmd /c bsdiff " + oldApkPath + " " + newApkPath + " " + patchPath);
        return process;
    }

    @Override
    public int applyGenDiff(String oldApkPath, String newApkPath, String patchPath) {
        return DiffUtil.getInstance().applyGenDiff(oldApkPath, newApkPath, patchPath);
    }
}
