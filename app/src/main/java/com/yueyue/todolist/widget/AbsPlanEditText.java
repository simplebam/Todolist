package com.yueyue.todolist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * author : yueyue on 2018/3/7 19:08
 * desc   :
 */
@Deprecated
public class AbsPlanEditText extends EditText {
    public boolean mEnter;

    public AbsPlanEditText(Context context) {
        super(context);
    }

    public AbsPlanEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsPlanEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AbsPlanEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            mEnter = true;
        } else {
            mEnter = false;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mEnter) {
            mEnter = false;
        }
        super.onDraw(canvas);
    }
}