package com.yueyue.todolist.modules.diary.ui;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yueyue.todolist.R;

/**
 * author : yueyue on 2018/3/6 11:49
 * desc   :
 */

public class SaveDialog extends Dialog {


    private TextView mDialogSubtitle;
    private Button mDialogExit;
    private Button mDialogSave;
    private OnSaveListener mOnSaveListener;
    private OnExitListener mOnExitListener;
    private Context mContext;

    public SaveDialog(@NonNull Context context) {
        this(context, 0);
    }


    public SaveDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.NormalDialogStyle);
        mContext = context;
        View view = View.inflate(context, R.layout.dialog_diary, null);
        view.setMinimumHeight((int) (context.getResources().getDisplayMetrics().widthPixels * 0.23f));
        setContentView(view);

        mDialogSubtitle = (TextView) findViewById(R.id.dialog_subtitle);
        mDialogSave = (Button) view.findViewById(R.id.dialog_save);
        mDialogExit = (Button) view.findViewById(R.id.dialog_exit);
        initDialog(context);
    }

    private void initDialog(Context context) {
        //使得点击对话框外部不消失对话框
        setCanceledOnTouchOutside(true);

        //设置对话框的大小
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams params = dialogWindow.getAttributes();
            params.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.75f);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.CENTER;
            dialogWindow.setAttributes(params);
        }

        mDialogSave.setOnClickListener(v -> {
            if (mOnSaveListener != null) {
                mOnSaveListener.onClick(v);
            }
        });


        mDialogExit.setOnClickListener(v -> {
            if (mOnExitListener != null) {
                mOnExitListener.onClick(v);
            }
        });
    }

    public void showDialog() {
        show();
    }

    public void dimissDialog() {
        dismiss();
        mContext = null;
    }

    public interface OnSaveListener {
        void onClick(View v);
    }

    public interface OnExitListener {
        void onClick(View v);
    }

    public SaveDialog setOnSaveListener(OnSaveListener onSaveListener) {
        mOnSaveListener = onSaveListener;
        return this;
    }

    public SaveDialog setOnExitListener(OnExitListener onExitListener) {
        mOnExitListener = onExitListener;
        return this;
    }

    public SaveDialog setSubTitle(SpannableString spannableString) {
        if (mDialogSubtitle != null) {
            mDialogSubtitle.setText(spannableString);
        }
        return this;
    }

    public SaveDialog setSubTitle(String string) {
        if (mDialogSubtitle != null) {
            mDialogSubtitle.setText(string);
        }
        return this;
    }

}
