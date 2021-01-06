package com.aliyun.rtc.superclassroom.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    public static String getExternalCacheDirPath(Context context) {
        String path = null;
        if (context != null) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null && externalCacheDir.exists()) {
                path = externalCacheDir.getPath();
            } else if (externalCacheDir != null) {
                externalCacheDir.mkdirs();
                path = externalCacheDir.getPath();
            }
        }
        return path;
    }

    public static String getExternalCacheDirPath(Context context, String child) {
        String externalCacheDirPath = getExternalCacheDirPath(context);
        if (externalCacheDirPath != null) {
            return externalCacheDirPath + File.separator + child;
        }
        return null;
    }

    public static File[] getFiles(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File[] files = null;
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            files = file.listFiles();
        }
        return files;
    }

    public static File writeFile(InputStream inputStream, String outPutPath) {
        File file = new File(outPutPath);
        FileOutputStream out = null;
        try {
            if (!file.exists()) {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            // 获取文件的输出流
            out = new FileOutputStream(file);
            int len;
            byte[] buffer = new byte[1024];
            // 读取（字节）字节到缓冲区
            while ((len = inputStream.read(buffer)) != -1) {
                // 从缓冲区（0）位置写入（字节）字节
                out.write(buffer, 0, len);
                out.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
