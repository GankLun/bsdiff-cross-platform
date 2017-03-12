package com.cross.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cross.android.APKUtil;
import com.cross.android.Constant;
import com.cross.android.DiffUtil;
import com.cross.android.FileUtil;
import com.cross.android.PatchUtil;
import com.cross.android.SignUtil;

import java.io.File;

/**
 * @author czl 2017/2/21
 * @Package com.cross.app
 * @Title: MainActivity
 * @Description: (patch测试)
 * Create DateTime: 2017/2/21
 */

public class MainActivity extends Activity {
    private String oldApkPath;
    private String patchPath;
    private String newApkPath;
    private String downLoadApkPath;
    private String targetMd5Hex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        downLoadApkPath = FileUtil.getCachePath(MainActivity.this) + File.separator + "download" + File.separator +
                "lujing_driver_V2.5.0_00_2017012116.apk";
        targetMd5Hex = SignUtil.getMd5ByFile(new File(downLoadApkPath));
        patchPath = FileUtil.getCachePath(MainActivity.this) + File.separator + "patch" + File.separator + "tradeDriver.patch";
        newApkPath = FileUtil.getCachePath(MainActivity.this) + File.separator + "new" + File.separator + "lujing_driver_V2.5.0_00_2017012116_patch.apk";

        findViewById(R.id.btn_diff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DiffTask diffTask = new DiffTask();
                diffTask.execute();

            }

        });

        findViewById(R.id.btn_patch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PatchTask patchTask = new PatchTask();
                patchTask.execute();
            }
        });

    }

    class DiffTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            oldApkPath = APKUtil.getSourceApkPath(getApplicationContext(), "com.cross.app");
            Log.e("oldApkPath", TextUtils.isEmpty(oldApkPath) ? "未安装" : oldApkPath);
            int ret = DiffUtil.getInstance().applyGenDiff(oldApkPath, downLoadApkPath, patchPath);
            return ret;
        }

        @Override
        protected void onPostExecute(Integer ret) {
            super.onPostExecute(ret);
            if (ret == Constant.DIFF_NONE) {
                Toast.makeText(MainActivity.this, "已经是最新版本无需差分", Toast.LENGTH_LONG).show();
                return;
            }
            if (ret == Constant.DIFF_FAIL) {
                Toast.makeText(MainActivity.this, "生成补丁文件失败", Toast.LENGTH_LONG).show();
                return;
            }
            if (ret == Constant.DIFF_SUCCESS) {
                Toast.makeText(MainActivity.this, "生成补丁文件成功，补丁文件位于：" + patchPath, Toast.LENGTH_LONG).show();
            }
        }
    }

    class PatchTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            int ret = PatchUtil.getInstance().applyPatch(oldApkPath, newApkPath, patchPath, targetMd5Hex);
            return ret;
        }

        @Override
        protected void onPostExecute(Integer ret) {
            super.onPostExecute(ret);
            if (ret == Constant.PATCH_FAIL) {
                Toast.makeText(MainActivity.this, "合成APK失败", Toast.LENGTH_LONG).show();
            } else if (ret == Constant.DIFF_SUCCESS) {
                Toast.makeText(MainActivity.this, "合成APK成功", Toast.LENGTH_LONG).show();
                APKUtil.installApk(MainActivity.this, newApkPath);
            }
        }
    }
}
