package com.yueyue.todolist.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yueyue.todolist.component.PLog;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 * author : yueyue on 2018/3/15 11:39
 * desc   :
 * qoute  :
 * <p>
 * <a href="https://juejin.im/entry/57a4aac3a633bd00603e83e5">通俗编程——白话 NIO 之 Channel - 后端 - 掘金 </a>
 * <a href="http://blog.csdn.net/o279642707/article/details/78954064">Android okio简析 - CSDN博客  </a>
 * </p>
 */

public class MyFileUtils {
    private static final String TAG = MyFileUtils.class.getSimpleName();

    private MyFileUtils() {

    }

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


    public static File getDiskPicturesDirFile(Context context, String uniqueName) {
        return new File(getDiskPicturesDirFileStr(context, uniqueName));
    }

    public static String getDiskPicturesDirFileStr(Context context, String uniqueName) {
        String picturePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
        return picturePath + File.separator + uniqueName;
    }


    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static File createNewFile(File parentFile, String fileName) throws IOException {
        String filePath = parentFile.getPath() + File.separator + fileName;
        return createNewFile(filePath);
    }

    public static File createNewFile(@NonNull String desPath) throws IOException {
        File desFile = new File(desPath);
        if (desFile.exists()) {
            desFile.delete();
        }
        desFile.createNewFile();
        return desFile;
    }

    //保存文件到指定路径
    public static boolean saveImageToLoacl(Bitmap bitmap, Bitmap.CompressFormat format, int bitmapQuality,
                                           String desPath) {
        if (bitmap == null || !MyFileUtils.isStorageMounted() || TextUtils.isEmpty(desPath)) {
            return false;
        }

        FileOutputStream fos = null;
        try {
            File file = createNewFile(desPath);
            fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            //通过io流的方式来压缩保存图片
            bitmap.compress(format, bitmapQuality, bos);
            fos.flush();
            return true;
        } catch (IOException e) {
            PLog.e(TAG, "saveImageToGallery: " + e.toString());
            e.printStackTrace();
            return false;
        } finally {
            closeIOQuietly(fos);
        }
    }


    public static void copy(@NonNull String sourcePath, @NonNull String desPath) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(sourcePath);
            File desFile = createNewFile(desPath);
            fos = new FileOutputStream(desFile);
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            //强制将通道中未写入磁盘的数据立刻写入到磁盘。
            outChannel.force(true);//表示将文件的元数据一同写入到磁盘
        } catch (Exception e) {
            PLog.e(TAG, "copy: " + e.toString());
            e.printStackTrace();
        } finally {
            closeIOQuietly(fos, fis);
        }

    }

    public static void highCopy(@NonNull String sourcePath, @NonNull String desPath) {
        BufferedSource bufferSource = null;
        BufferedSink bufferSink = null;
        try {
            Source source = Okio.source(new File(sourcePath));
            bufferSource = Okio.buffer(source);

            Sink sink = Okio.sink(createNewFile(desPath));
            bufferSink = Okio.buffer(sink);

            bufferSink.writeAll(bufferSource);

            bufferSink.flush();

        } catch (IOException e) {
            PLog.e(TAG, "highCopy: " + e.toString());
            e.printStackTrace();
        } finally {
            closeIOQuietly(bufferSink, bufferSource);
        }
    }

    /**
     * @deprecated 使用 {@link #copy(String, String)} 更好一点
     */
    @Deprecated
    public static void lowCopy(@NonNull String sourcePath, @NonNull String desPath) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            File sourceFile = new File(sourcePath);
            File desFile = createNewFile(desPath);

            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(desFile);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            PLog.e(TAG, "lowCopy: " + e.toString());
            e.printStackTrace();
        } finally {
            closeIOQuietly(fos, fis);
        }
    }


    public static void closeIOQuietly(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    PLog.e(TAG, "closeIOQuietly: " + e.toString());
                    e.printStackTrace();
                }
            }
        }
    }
}
