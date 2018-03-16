package com.yueyue.todolist.common.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * author : yueyue on 2018/3/14 10:09
 * desc   :
 */

public class ProgressDialogUtils {

    private ProgressDialog mProgressDialog;
    private Context mContext;

    public ProgressDialogUtils(Context context){
        this.mContext=context;
    }

    public void show(String message){
        if(mProgressDialog==null)
            mProgressDialog=new ProgressDialog(mContext);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    public void hide(){
        if(mProgressDialog!=null)
            mProgressDialog.cancel();
    }
}
