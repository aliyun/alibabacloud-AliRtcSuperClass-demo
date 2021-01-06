package com.aliyun.rtc.superclassroom.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil {

    private static final String TAG = ZipUtil.class.getSimpleName();

    public static List<File> unZipAssetsFolder(Context context, String zipFileString, String
            outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(context.getAssets().open(zipFileString));
        ZipEntry zipEntry;
        String szName = null;
        List<File> files = new ArrayList<>();
        while ((zipEntry = inZip.getNextEntry()) != null) {
            File file = null;
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                //获取部件的文件夹名
                szName = szName != null ? szName.substring(0, szName.length() - 1) : szName;
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                Log.e(TAG, outPathString + File.separator + szName);
                String[] split = szName.split("/");
                if (split.length > 0) {
                    szName = split[split.length - 1];
                }
                //mac 上的临时文件
                if (szName.startsWith(".")){
                    continue;
                }
                file = new File(outPathString + File.separator + szName);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                // 获取文件的输出流
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // 读取（字节）字节到缓冲区
                while ((len = inZip.read(buffer)) != -1) {
                    // 从缓冲区（0）位置写入（字节）字节
                    out.write(buffer, 0, len);
                    out.flush();
                }
                files.add(file);
            }
        }
        inZip.close();
        return files;
    }
}
