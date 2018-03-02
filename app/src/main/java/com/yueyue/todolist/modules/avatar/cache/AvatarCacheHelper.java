package com.yueyue.todolist.modules.avatar.cache;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.blankj.utilcode.util.EncryptUtils;
import com.yueyue.todolist.base.BaseApplication;
import com.yueyue.todolist.component.cache.BitmapCache;

/**
 * author : yueyue on 2018/3/3 19:42
 * desc   : 头像存储工具类
 */
@Deprecated
public class AvatarCacheHelper {
    private static BitmapCache sBitmapCache;



    private AvatarCacheHelper() {
        sBitmapCache = new BitmapCache(BaseApplication.getContext(),
                BitmapCache.CACHE_AVATAR_IMAGE);
    }


    public static AvatarCacheHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    private static final class SingletonHolder {
        private static final AvatarCacheHelper sInstance = new AvatarCacheHelper();
    }


    public boolean putAvatar(String key, Bitmap value) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        String cacheBitmapKey = EncryptUtils.encryptMD5ToString(key);
        return value != null && sBitmapCache.add(key, value);
    }


    public Bitmap getAvatar(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        return sBitmapCache.get(key);
    }


}
