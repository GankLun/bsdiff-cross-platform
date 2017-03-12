package com.cross.util;

import java.io.File;

/**
 * @author czl 2017/2/22
 * @Package com.cross.patch
 * @Title: FileUtil
 * @Description: (文件操作工具类)
 * Create DateTime: 2017/2/22
 */

public class FileUtil {


    /**
     * 创建文件夹
     *
     * @param dirPath
     * @return
     */
    public static void createDir(String dirPath) {
        try {
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除文件或者文件夹
     *
     * @param file
     */
    public static void deleteFileOrDirectory(File file) {
        try {
            if (file.isFile() && file.exists()) {
                file.delete();
                return;
            }
            if (file.isDirectory() && file.exists()) {
                File[] childFiles = file.listFiles();
                // 删除空文件夹
                if (childFiles == null || childFiles.length == 0) {
                    file.delete();
                    return;
                }
                // 递归删除文件夹下的子文件
                for (int i = 0; i < childFiles.length; i++) {
                    deleteFileOrDirectory(childFiles[i]);
                }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
