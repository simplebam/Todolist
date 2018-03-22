package com.yueyue.todolist.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

/**
 * author : yueyue on 2018/3/10 01:46
 * desc   : 图片加载类,统一适配(方便换库,方便管理)
 */

public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();

    private ImageLoader() {
    }

    public static void load(Context context, @DrawableRes int imageRes, ImageView view) {
        Glide.with(context).load(imageRes).
                transition(new DrawableTransitionOptions().crossFade(800)).into(view);
    }

    public static void load(@NonNull Context context, @NonNull String imagePath,
                            int reqWidth, int reqHeight, Target target) {
        RequestOptions options = new RequestOptions()
                .override(reqWidth, reqHeight)
                .centerCrop();

        Glide.with(context)
                .asBitmap()
                .load(imagePath)
                .apply(options)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        //根据Bitmap对象创建ImageSpan对象
                        Log.d(TAG, "bitmap width:" + resource.getWidth() + "   height:" + resource.getHeight());
                        if (target != null) {
                            target.onResourceReady(resource, transition);
                        }
                    }
                });
    }

    /**
     * 自定义RequestOptions使用
     *
     * @param activity
     * @param url            图片链接
     * @param requestOptions
     * @param imageView      目标view
     */
    public static void LoadImage(Activity activity, Object url, ImageView imageView, RequestOptions requestOptions) {
        Glide.with(activity).load(url)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions().crossFade(800))
                .into(imageView);
    }

    public static void clear(Context context) {
        Glide.get(context).clearMemory();
    }


}

