package com.cross.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;

/**
 * @author czl 2017/2/22
 * @Package com.cross.android
 * @Title: APKUtil
 * @Description: (APK 操作工具类)
 * Create DateTime: 2017/2/22
 */

public class APKUtil {
    /**
     * 获取已安装Apk文件的源Apk文件
     *
     * @param context     上下文
     * @param packageName 应用包名
     * @return
     */
    public static String getSourceApkPath(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) return null;

        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            return appInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 安装Apk
     *
     * @param context 上下文
     * @param apkPath 需要安装的APK路径
     */
    public static void installApk(Context context, String apkPath) {
        File file = new File(apkPath);
        if (file.isFile() && file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
