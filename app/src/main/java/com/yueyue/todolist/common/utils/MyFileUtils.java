package com.yueyue.todolist.common.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * author : yueyue on 2018/3/15 11:39
 * desc   :
 */

public class MyFileUtils {
    public static boolean isStorageMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable();
    }


    public static File getDiskCacheDirFile(Context context, String uniqueName) {
        return new File(getDiskCacheDirFileStr(context, uniqueName));
    }

    public static String getDiskCacheDirFileStr(Context context, String uniqueName) {
        String cachePath;
        if (isStorageMounted()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator + uniqueName;
    }


    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }


    public static boolean copy(String from, String to) {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            File file = new File(from);

            File toF = new File(to);
            if (!toF.exists()) {
                toF.createNewFile();
            }
            toF.setReadable(true);
            toF.setWritable(true);

            if (file.exists()) {
                is = new FileInputStream(from);
                os = new FileOutputStream(toF);

                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
