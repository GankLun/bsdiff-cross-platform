package com.cross.environment;

import com.cross.util.DiffUtil;

import java.io.IOException;

/**
 * @author czl 2017/2/18
 * @Package com.cross.environment
 * @Title: LinuxPlatForm
 * @Description: (Linux操作系统平台)
 * Create DateTime: 2017/2/18
 */
public class LinuxPlatForm implements Platform {
    @Override
    public Process execDiff(String oldApkPath, String newApkPath, String patchPath) throws IOException {
        Process process = Runtime.getRuntime().exec("bsdiff " + oldApkPath + " " + newApkPath + " " + patchPath);
        return process;
    }

    @Override
    public int applyGenDiff(String oldApkPath, String newApkPath, String patchPath) {
        return DiffUtil.getInstance().applyGenDiff(oldApkPath, newApkPath, patchPath);
    }
}
