package com.yueyue.todolist.component.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.LruCache;

import com.blankj.utilcode.util.Utils;

/**
 * author : yueyue on 2018/3/16 09:13
 * desc   : BitMap 内存管理以及Disk管理
 * quote  : <a href="http://blog.csdn.net/guolin_blog/article/details/9316683">Android高效加载大图、多图解决方案，有效避免程序OOM - CSDN博客</a>
 * <a href="http://blog.csdn.net/jxxfzgy/article/details/44885623">LruCache详解之 Android 内存优化 - CSDN博客</a>
 */

public class BitMapHelper {

    private Context mContext;
    private LruCache<String, Bitmap> mLruCache;
    private BitmapDiskHelper mBitmapDiskHelper;

    //  this();//使用运行内存的1/8
    public BitMapHelper(@NonNull Context context) {
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        this.mContext = context;
        initBitmapDisk(context);
        initLruCache(maxSize);
    }

    private void initBitmapDisk(Context context) {
        if (context == null) {
            context = Utils.getApp();
        }
        mBitmapDiskHelper = new BitmapDiskHelper(context, BitmapDiskHelper.CACHE_NOTE_IMAGE);
    }

    private void initLruCache(int maxSize) {
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }

            @Override
            protected Bitmap create(String key) {
                return mBitmapDiskHelper.get(key);
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                //if (newValue != null) {
                //可以进入这里的只有LruCache的put(),get()才可以,
                // 其他的remove()以及trimToSize的newValue参数都是为null
                //这里不进行mBitmapDiskHelper.add操作是因为考虑到LruCache的get()多线程操作导致的相同值多次add操作
                //}
                if (evicted && oldValue != null) {
                    oldValue.recycle();
                    //这里不进行 mBitmapDiskHelper.remove操作,因为可能会把新的value值删除
                }
            }
        };

    }

    public void put(@NonNull String key, @NonNull Bitmap value) {
        mLruCache.put(key, value);
        mBitmapDiskHelper.add(key, value);
    }

    public Bitmap get(String key) {
        return mLruCache.get(key);
    }

    public void remove(String key) {
        mLruCache.remove(key);
        mBitmapDiskHelper.remove(key);
    }

    public void clear() {
        mLruCache.evictAll();
    }

}
