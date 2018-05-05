package com.forgernv;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by shenhua on 1/17/2017.
 * Email shenhuanet@126.com
 */
public class FileUtils {
    private static FileUtils instance;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    private Context context;
    private FileOperateCallback callback;
    private volatile boolean isSuccess;
    private String errorStr;

    public static FileUtils getInstance(Context context) {
        if (instance == null)
            instance = new FileUtils(context);
        return instance;
    }
    private FileUtils(Context context) {
        this.context = context;
    }
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (callback != null) {
                if (msg.what == SUCCESS) {
                    callback.onSuccess();
                }
                if (msg.what == FAILED) {
                    callback.onFailed(msg.obj.toString());
                }
            }
        }
    };

// context can get from following syntax
// Context context=getApplicationContext();

public FileUtils copyAssetsToSD(final String srcPath, final String sdPath) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                copyAssetsToDst(context, srcPath, sdPath);
                if (isSuccess)
                    handler.obtainMessage(SUCCESS).sendToTarget();
                else
                    handler.obtainMessage(FAILED, errorStr).sendToTarget();
            }
        }).start();
        return this;
    }

    public void setFileOperateCallback(FileOperateCallback callback) {
        this.callback = callback;
    }

    private void copyAssetsToDst(Context context, String srcPath, String dstPath) {
        try {
            String fileNames[] = context.getAssets().list(srcPath);

            if (fileNames.length > 0) {//有子文件及文件夹，表明当前是文件夹
                File file = new File(dstPath);
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    Log.i("tagpath", "filepath:="+srcPath + File.separator + fileName);
                    if (!srcPath.equals("")) { // assets/srcPath 文件夹下的所有文件与目录
                        copyAssetsToDst(context, srcPath + File.separator + fileName, dstPath + File.separator + fileName);
                    } else { // assets 文件夹下的所有文件与目录
                        copyAssetsToDst(context, fileName, dstPath + File.separator + fileName);
                    }
                }
            } else {//表明是文件
                InputStream fis = context.getAssets().open(srcPath);
                //FileInputStream fis = new FileInputStream(new File(srcPath));
                FileOutputStream fos = new FileOutputStream(new File(dstPath));
                byte[] buffer = new byte[8192];
                int byteCount;
                while ((byteCount = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                fis.close();
                fos.close();
            }
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            errorStr = e.getMessage();
            isSuccess = false;
        }
    }

    public interface FileOperateCallback {
        void onSuccess();

        void onFailed(String error);
    }

}