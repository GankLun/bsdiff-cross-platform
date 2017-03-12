package com.cross.environment;

import java.io.IOException;

/**
 * @author czl 2017/2/18
 * @Package com.cross.environment
 * @Title: Platform
 * @Description: (平台接口，用于执行不同的操作指令)
 * Create DateTime: 2017/2/18
 */
public interface Platform {
    /**
     * 执行对应操作系统平台bsdiff命令
     *
     * @param oldApkPath
     * @param newApkPath
     * @param patchPath
     * @return
     * @throws IOException
     */
    Process execDiff(String oldApkPath, String newApkPath, String patchPath) throws IOException;

    /**
     * JNI方式执行差分调用
     *
     * @param oldApkPath
     * @param newApkPath
     * @param patchPath
     * @return
     * @throws IOException
     */
    int applyGenDiff(String oldApkPath, String newApkPath, String patchPath);
}
