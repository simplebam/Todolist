package com.yueyue.todolist.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Environment;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yueyue.todolist.R;
import com.yueyue.todolist.component.PLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * author : yueyue on 2018/3/15 16:15
 * desc   :
 */

public class BitmapUtils {

    private static final java.lang.String TAG = BitmapUtils.class.getSimpleName();

    private BitmapUtils() {
    }

    /**
     * 生成透明背景的圆形图片
     */
    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        try {
            Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(circleBitmap);

            final Paint paint = new Paint();
            paint.setAntiAlias(true);

            float radius = Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2.0f;

            //绘制透明底
            canvas.drawARGB(0, 0, 0, 0);

            //绘制背景圆
            paint.setColor(Color.WHITE);
            canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, radius, paint);

            //取两层绘制交集。显示上层。
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            //绘制图片并取交集，显示上层
            //取中间区域绘制
            int left = (int) ((bitmap.getWidth() - radius * 2) / 2);
            int top = (int) ((bitmap.getHeight() - radius * 2) / 2);
            canvas.drawBitmap(bitmap, -left, -top, paint);

            bitmap.recycle();
            bitmap = null;

            return circleBitmap;

        } catch (Exception e) {
            return bitmap;
        }
    }

    /**
     * 将 jpg 格式图片转成 png 格式
     */
    public static Bitmap jpg2Png(Context context, Bitmap source) {

        FileOutputStream fos = null;
        File file = null;
        try {
            String fileP = "temp" + File.separator + EncryptUtils.encryptMD5ToString(source.toString());
            file = MyFileUtils.getDiskCacheDirFile(context, fileP);
            fos = new FileOutputStream(file);
            source.compress(Bitmap.CompressFormat.PNG, 100, fos);

            Bitmap b = BitmapFactory.decodeFile(file.getCanonicalPath());
            if (b != null)
                return b;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return source;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return source;
    }

    //调整bitmap 大小
    public static Bitmap bitmapResizeFromResource(Resources res, int id, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        //只解析图片的原始宽高，而不正真加载图片
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, options);

        int inSampleSize = 1;
        if (reqWidth > 0 && reqHeight > 0) {
            final int height = options.outHeight;
            final int width = options.outWidth;

            //如果宽或高有任一者不满足要求就进行调整
            if (height > reqHeight || width > reqHeight) {

                // inSampleSize 为 1 没有作用，使从 2 开始增加
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth)
                    inSampleSize *= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, id, options);
    }

    /**
     * 以压缩尺寸的方式从文件中读取图片，要求的图片宽高任一者大于原始图片宽或高时才会压缩尺寸，否则原图返回
     */
    public static Bitmap bitmapResizeFromFile(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        int inSampleSize = 1;
        if (reqWidth != 0 && reqHeight != 0) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            if (height > reqHeight || width > reqHeight) {
                int halfHeight = height / 2;
                int halfWidth = width / 2;
                while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth)
                    inSampleSize *= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 保存文件到系统图库
     */
    public static void saveImageToGallery(Context context, Bitmap bitmap) {
        // 首先保存图片
        if (!MyFileUtils.isStorageMounted()) {
            ToastUtils.showShort(context.getString(R.string.unable_to_save_photo_because_of_the_stroage_error));
            return;
        }

        if (bitmap == null) {
            ToastUtils.showShort(context.getString(R.string.the_photo_not_exist));
            return;
        }


        File desFile = null;
        try {
            File parentFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String fileName = System.currentTimeMillis() + ".jpg";
            desFile = MyFileUtils.createNewFile(parentFile, fileName);
        } catch (IOException e) {
            PLog.e(TAG, "saveImageToGallery: " + e.toString());
            e.printStackTrace();
        }

        if (desFile == null) {
            return;
        }

        boolean b = MyFileUtils.saveImageToLoacl(bitmap, Bitmap.CompressFormat.JPEG,
                60, desFile.getPath());

        if (!b) {
            ToastUtils.showShort(context.getString(R.string.save_error));
            return;
        }

        //把文件插入到系统图库
        //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

        //保存图片后发送广播通知更新数据库
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(desFile)));
        ToastUtils.showShort(context.getString(R.string.save_success) + ":" + desFile.getPath());
    }

    public static void recycleBitamp(Bitmap... bitmaps) {
        if (bitmaps == null || bitmaps.length == 0) {
            return;
        }

        for (Bitmap bitmap : bitmaps) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }


}



