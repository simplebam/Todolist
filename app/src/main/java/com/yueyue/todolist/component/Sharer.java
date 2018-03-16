package com.yueyue.todolist.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * author : yueyue on 2018/3/14 10:18
 * desc   :
 */

public class Sharer {
    public static void shareText(Context context, String title, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, title));
    }

    public static void shareImage(Context context, String title, Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);//设置分享行为
        intent.setType("image/*");//设置分享内容的类型
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent = Intent.createChooser(intent, title);
        context.startActivity(intent);

    }
}