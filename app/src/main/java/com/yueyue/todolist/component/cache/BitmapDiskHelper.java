package com.yueyue.todolist.component.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.blankj.utilcode.util.EncryptUtils;
import com.yueyue.todolist.common.utils.MyFileUtils;
import com.yueyue.todolist.component.PLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * author : yueyue on 2018/3/15 11:37
 * desc   : 位图磁盘缓存
 * quote  : Android DiskLruCache完全解析，硬盘缓存的最佳方案
 * http://blog.csdn.net/guolin_blog/article/details/28863651
 */

public class BitmapDiskHelper {
    private final static String TAG = BitmapDiskHelper.class.getSimpleName();

    private DiskLruCache mDiskLruCache = null;



    private final static String DEFAULT_PIC_KEY = "default_key";

    public static final String CACHE_NOTE_IMAGE = "note_image";

    private final String uniqueName;

    public BitmapDiskHelper(Context context, String uniqueName) {
        this.uniqueName = uniqueName;
        initDiskCacheControl(context, uniqueName);
    }

    public String getUniqueName() {
        return uniqueName;
    }

    private void initDiskCacheControl(Context context, String uniqueName) {
        try {
            File cacheDir = MyFileUtils.getDiskCacheDirFile(context, uniqueName);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }

            mDiskLruCache = DiskLruCache.open(cacheDir,
                    getAppVersion(context),
                    1,
                    10 * 1024 * 1024);

        } catch (IOException e) {
            PLog.e(TAG, "initDiskCacheControl: "+e.toString());
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
            PLog.e(TAG, "remove: "+ e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 从缓存中获得图片
     *
     * @param key key，应使用{@link EncryptUtils#encryptMD5ToString(String)}方法转换为 MD5
     * @return 位图，获取失败返回 null
     */
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
            PLog.e(TAG, "get: "+ e.toString());
            e.printStackTrace();
            return result;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    PLog.e(TAG, "get: "+ e.toString());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 添加图片到缓存中
     *
     * @param key    key，应使用{@link EncryptUtils#encryptMD5ToString(String)}方法转换为 MD5
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
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, os);
            editor.commit();

            flush();
            return true;
        } catch (IOException e) {
            PLog.e(TAG, "add: "+ e.toString());
            e.printStackTrace();
            try {
                editor.abort();
            } catch (IOException e1) {
                PLog.e(TAG, "add: "+ e1.toString());
                e1.printStackTrace();
            }
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    PLog.e(TAG, "add: "+ e.toString());
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
            PLog.e(TAG, "flush: "+ e.toString());
            e.printStackTrace();
        }
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            PLog.e(TAG, "getAppVersion: "+ e.toString());
            e.printStackTrace();
        }
        return 1;
    }

    //初始化默认的专辑图片，第一次启动应用时完成
    public void initDefaultBitmap(Bitmap bitmap) {
        add(EncryptUtils.encryptMD5ToString(DEFAULT_PIC_KEY), bitmap);
    }

    public Bitmap getDefaultBitmap() throws Exception {
        Bitmap b = get(EncryptUtils.encryptMD5ToString(DEFAULT_PIC_KEY));
        if (b == null) {
            throw new Exception("you need call com.yueyue.todolist.component.cache.BitmapDiskHelper#initDefaultBitmap(Bitmap b) first");
        }
        return get(EncryptUtils.encryptMD5ToString(DEFAULT_PIC_KEY));
    }
}
