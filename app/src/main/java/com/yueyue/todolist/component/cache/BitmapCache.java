package com.yueyue.todolist.component.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * author : yueyue on 2018/3/3 16:30
 * desc   : 位图磁盘缓存
 * quote  : Android DiskLruCache完全解析，硬盘缓存的最佳方案 - CSDN博客
 * http://blog.csdn.net/guolin_blog/article/details/28863651
 */
@Deprecated
public class BitmapCache {

    private final static String TAG = BitmapCache.class.getSimpleName();

    private DiskLruCache mDiskLruCache = null;

    private Context mContext;


    public static final String CACHE_AVATAR_IMAGE = "avatar_image";

    private final String name;

    public BitmapCache(Context context, String name) {
        this.mContext = context;
        this.name = name;
        initDiskCacheControl(context, name);
    }

    public String getName() {
        return name;
    }

    private void initDiskCacheControl(Context context, String name) {
        try {
            File cacheDir = getDiskCacheDirFile(context, name);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            int maxSize = 10 * 1024 * 1024;
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DiskLruCache getCacheControl() {
        return mDiskLruCache;
    }

    /**
     * 从缓存中移除某张图片
     */
    public void remove(String key) {
        try {
            mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从缓存中获得图片
     *
     * @param key key应该 MD5 转换一下才得
     * @return 位图，获取失败返回 null
     */
    @Nullable
    public Bitmap get(String key) {
        Bitmap result = null;
        InputStream is = null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                is = snapshot.getInputStream(0);
                result = BitmapFactory.decodeStream(is);
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 添加图片到缓存中
     *
     * @param key    key应该 MD5 转换一下才得
     * @param bitmap 位图
     * @return 添加成功返回 true
     */
    public boolean add(String key, Bitmap bitmap) {
        if (bitmap == null)
            return false;

        OutputStream os = null;
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskLruCache.edit(key);
            os = editor.newOutputStream(0);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 50, os)) {
                editor.commit();
            } else {
                editor.abort();
            }

            flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将内存中的操作记录同步到日志文件（也就是journal文件）
     * 并不是每次写入缓存都要调用一次flush()方法
     * 在Activity的onPause()方法中去调用flush()方法就可以了。
     */
    public void flush() {
        try {
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private File getDiskCacheDirFile(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //应用关联缓存目录,不需要申请权限即可使用
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

}
