package com.forgernv;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import com.facebook.react.ReactActivity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MainActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "ForgeRNV";
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //final static String TARGET_BASE_PATH = "/sdcard/ForgeRNV/";
        //TARGET_BASE_PATH =getFilesDir().getAbsolutePath()+"/"; //data/data/user/0/com.ForgeRNV/files
        //final static String TARGET_BASE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/";
        String TARGET_BASE_PATH = getExternalFilesDir(null)+"/";  // "/storage/emulated/0/Android/data/com.xxx.android.react.xxx/files"
        String TARGET_FILE_NAME = "www";
        Log.i("tag", "TARGET_BASE_PATH="+TARGET_BASE_PATH);
        try {
       // Context context=getApplicationContext();
       // if(context!=null) Log.i("tag", "context get successfully");
       // FileUtils.getInstance(context).copyAssetsToSD("hh.zip",TARGET_BASE_PATH);
           //定义assetmanager对象
            AssetManager assetManager = getAssets();
            // 需要解压的对象
            InputStream dataSource = assetManager.open(TARGET_FILE_NAME+".zip");
            // 調用解压的方法
            //copyBigDataToSD(TARGET_BASE_PATH+"/hh.zip");
            //FileInputStream fis = new FileInputStream(new File(TARGET_BASE_PATH+"/hh.zip"));
            //upZipFile(TARGET_BASE_PATH+"/hh.zip",TARGET_BASE_PATH+"/hh");
           unzip(dataSource, TARGET_BASE_PATH+"/"+TARGET_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*    private void copyBigDataToSD(String strOutFileName) throws IOException
    {
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(strOutFileName);
        myInput = this.getAssets().open("hh.zip");
        byte[] buffer = new byte[8192];
        int length = myInput.read(buffer);
        while(length > 0)
        {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }
        myOutput.flush();
        myInput.close();
        myOutput.close();
    }*/

   /* public void upZipFile(String zipPath, String folderPath) throws ZipException, IOException {

        File zipFile = new File(zipPath);
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();)
        {
            ZipEntry entry = ((ZipEntry)entries.nextElement());
            InputStream in = zf.getInputStream(entry);
            String str = folderPath + File.separator + entry.getName();
            File desFile = new File(str);
            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                desFile.createNewFile();
            }
            OutputStream out = new FileOutputStream(desFile);
            byte buffer[] = new byte[4096];
            int realLength;
            while ((realLength = in.read(buffer)) > 0) {
            out.write(buffer, 0, realLength);
        }
            in.close();
            out.close();
        }
    }*/

    public static void unzip(InputStream zipFileName, String outputDirectory) {
        try {
            ZipInputStream in = new ZipInputStream(zipFileName);
            // 获取ZipInputStream中的ZipEntry条目，一个zip文件中可能包含多个ZipEntry，
            // 当getNextEntry方法的返回值为null，则代表ZipInputStream中没有下一个ZipEntry，
            // 输入流读取完成；
            ZipEntry entry = in.getNextEntry();
            while (entry != null) {
                // 创建以zip包文件名为目录名的根目录
                File file = new File(outputDirectory);
                file.mkdir();
                if (entry.isDirectory()) {
                    String name = entry.getName();
                    name = name.substring(0, name.length() - 1);
                    file = new File(outputDirectory + File.separator + name);
                    file.mkdir();
                } else {
                    file = new File(outputDirectory + File.separator + entry.getName());
                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    byte buffer[] = new byte[8192];//here decide the speed to copy the data
                    int realLength;
                    while ((realLength = in.read(buffer)) > 0) {
                        out.write(buffer, 0, realLength);
                    }
                    out.close();
                }
                // 读取下一个ZipEntry
                entry = in.getNextEntry();
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
    }
}

