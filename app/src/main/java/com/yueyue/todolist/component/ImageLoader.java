package com.yueyue.todolist.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

/**
 * author : yueyue on 2018/3/10 01:46
 * desc   : 图片加载类,统一适配(方便换库,方便管理)
 */

public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();

    private ImageLoader() {}

    public static void load(Context context, @DrawableRes int imageRes, ImageView view) {
        Glide.with(context).load(imageRes).crossFade().into(view);
    }

    public static void load(@NonNull Context context, @NonNull String imagePath,
                            int reqWidth, int reqHeight, Target target) {
        Glide.with(context)
                .load(imagePath)
                .asBitmap()
                .override(reqWidth, reqHeight)  // 设置大小
                .fitCenter()                                     // 不按照比例
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        //根据Bitmap对象创建ImageSpan对象
                        Log.d(TAG,"bitmap width:" + resource.getWidth() + "   height:" + resource.getHeight());
                       if (target!=null){
                           target.onResourceReady(resource,glideAnimation);
                       }
                    }
                });
    }


    public static void clear(Context context) {
        Glide.get(context).clearMemory();
    }


}

