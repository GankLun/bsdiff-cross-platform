package com.cross.diff;

import com.cross.util.FileUtil;
import com.cross.environment.LinuxPlatForm;
import com.cross.environment.MacPlatForm;
import com.cross.environment.Platform;
import com.cross.environment.WinPlatForm;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author czl 2017/2/18
 * @Package com.cross.diff
 * @Title: DiffHelper
 * @Description: (二进制文件差分提供外部调用类)
 * Create DateTime: 2017/2/18
 */
public class DiffHelper {
    public final static int DIFF_APK_SUCCESS = 0;
    public final static int CHECK_APK_FAIL = 1;
    public final static int CHECK_APK_SUCCESS = 2;
    public final static int DIFF_APK_FAIL = 3;

    private static int check(String oldApkPath, String newApkPath, String patchPath) {
        System.out.println("-------start diff-------");
        if (oldApkPath == null || oldApkPath.equals("") || !new File(oldApkPath).exists()) {
            System.out.println("please pass the correct old apk path");
            System.out.println("-------diff over-------");
            return CHECK_APK_FAIL;
        }

        if (newApkPath == null || newApkPath.equals("") || !new File(newApkPath).exists()) {
            System.out.println("please pass the correct new apk path");
            System.out.println("-------diff over-------");
            return CHECK_APK_FAIL;
        }

        if (patchPath == null || patchPath.equals("")) {
            System.out.println("please pass the correct patch path");
            System.out.println("-------diff over-------");
            return CHECK_APK_FAIL;
        }
        try {
            SAXParserFactory saxfac = SAXParserFactory.newInstance();
            SAXParser saxparser = saxfac.newSAXParser();
            ApkHandler apkHandler = new ApkHandler();

            String oldXmlContent = AXmlPrinter.getManifestXMLFromAPK(oldApkPath);
            InputStream oldInput = new ByteArrayInputStream(oldXmlContent.getBytes("UTF-8"));
            saxparser.parse(oldInput, apkHandler);
            oldInput.close();
            String oldPackageName = apkHandler.getPackageName();
            int oldVersionCode = apkHandler.getVersionCode();
            String oldVersionName = apkHandler.getVersionName();
            System.out.println("oldPackageName:" + oldPackageName);
            System.out.println("oldVersionCode:" + oldVersionCode);
            System.out.println("oldVersionName:" + oldVersionName);

            String newXmlContent = AXmlPrinter.getManifestXMLFromAPK(newApkPath);
            InputStream newInput = new ByteArrayInputStream(newXmlContent.getBytes("UTF-8"));
            saxparser.parse(newInput, apkHandler);
            newInput.close();
            String newPackageName = apkHandler.getPackageName();
            int newVersionCode = apkHandler.getVersionCode();
            String newVersionName = apkHandler.getVersionName();
            System.out.println("newPackageName:" + newPackageName);
            System.out.println("newVersionCode:" + newVersionCode);
            System.out.println("newVersionName:" + newVersionName);

            if (!oldPackageName.equals(newPackageName)) {
                System.out.println("packageName must be the same");
                return CHECK_APK_FAIL;
            }
            if (newVersionCode <= oldVersionCode) {
                System.out.println("the versionCode of new apk must greater than the versionCode of old apk");
                return CHECK_APK_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CHECK_APK_FAIL;
        }
        return CHECK_APK_SUCCESS;

    }

    /**
     * 以command形式执行差分
     *
     * @param oldApkPath 旧版APK绝对路径
     * @param newApkPath 新版APK绝对路径
     * @param patchPath  差分包绝对路径
     * @return
     */
    public static int startDiffByCmd(String oldApkPath, String newApkPath, String patchPath) {
        int code = check(oldApkPath, newApkPath, patchPath);
        if (code == CHECK_APK_FAIL) {
            return code;
        }
        String osName = System.getProperty("os.name");
        if (osName == null || osName.equals("")) {
            osName = "linux";
        }
        Platform platform = null;
        if (osName.toLowerCase().contains("windows")) {
            platform = new WinPlatForm();

        } else if (osName.toLowerCase().contains("linux")) {
            platform = new LinuxPlatForm();
        } else if (osName.toLowerCase().contains("mac")) {
            platform = new MacPlatForm();
        }

        File patchFile = new File(patchPath);
        FileUtil.deleteFileOrDirectory(patchFile);
        File parent = patchFile.getParentFile();
        FileUtil.createDir(parent.getAbsolutePath());

        Process process = null;
        BufferedReader br = null;
        try {
            if (platform != null) {
                process = platform.execDiff(oldApkPath, newApkPath, patchPath);
            }

            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String lineStr;
            while ((lineStr = br.readLine()) != null) {
                System.out.println(lineStr);
            }
            if (process.waitFor() == 0 && patchFile.exists()) {
                System.out.println("diff apk success:the target patch is " + patchPath);
                return DIFF_APK_SUCCESS;
            } else {
                FileUtil.deleteFileOrDirectory(patchFile);
                System.out.println("diff apk fail");
                return DIFF_APK_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            FileUtil.deleteFileOrDirectory(patchFile);
            System.out.println("diff apk fail");
            return DIFF_APK_FAIL;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("-------diff over-------");
        }
    }

    /**
     * 执行差分（JNI方式调用）
     *
     * @param oldApkPath 旧版APK绝对路径
     * @param newApkPath 新版APK绝对路径
     * @param patchPath  差分包绝对路径
     * @return
     */
    public static int startDiff(String oldApkPath, String newApkPath, String patchPath) {
        int code = check(oldApkPath, newApkPath, patchPath);
        if (code == CHECK_APK_FAIL) {
            return code;
        }
        String osName = System.getProperty("os.name");
        if (osName == null || osName.equals("")) {
            osName = "linux";
        }
        Platform platform = null;
        if (osName.toLowerCase().contains("windows")) {
            platform = new WinPlatForm();

        } else if (osName.toLowerCase().contains("linux")) {
            platform = new LinuxPlatForm();
        } else if (osName.toLowerCase().contains("mac")) {
            platform = new MacPlatForm();
        }

        File patchFile = new File(patchPath);
        FileUtil.deleteFileOrDirectory(patchFile);
        File parent = patchFile.getParentFile();
        FileUtil.createDir(parent.getAbsolutePath());

        int ret = platform.applyGenDiff(oldApkPath, newApkPath, patchPath);
        if (ret == 0 && patchFile.exists()) {
            System.out.println("diff apk success:the target patch is " + patchPath);
            System.out.println("-------diff over-------");
            return DIFF_APK_SUCCESS;
        } else {
            FileUtil.deleteFileOrDirectory(patchFile);
            System.out.println("diff apk fail");
            System.out.println("-------diff over-------");
            return DIFF_APK_FAIL;
        }

    }
}
