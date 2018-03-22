package com.yueyue.todolist.modules.lock;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseActivity;
import com.yueyue.todolist.component.PreferencesManager;
import com.yueyue.todolist.modules.main.ui.MainActivity;
import com.yueyue.todolist.widget.lock.LockPatternView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : yueyue on 2018/3/22 09:33
 * desc   :
 */

public class SetLockActivity extends BaseActivity {

    @BindView(R.id.tv_activity_set_lock_title)
    TextView mTitleTv;
    @BindView(R.id.lock_pattern_view)
    LockPatternView mLockPatternView;
    @BindView(R.id.btn_password_clear)
    Button mClearBtn;

    private String mPassword;
    /**
     * 是否是第一次输入密码
     */
    private boolean isFirst = true;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, SetLockActivity.class);
        activity.startActivityForResult(intent, MainActivity.SET_LOCK_REQUEST_CODE);
    }


    @Override
    protected int initLayoutId() {
        return R.layout.activity_set_lock;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();
        initEvents();
    }

    private void setupView() {
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(ConvertUtils.dp2px(2));
        gd.setColor(getResources().getColor(R.color.colorPrimary));
        mClearBtn.setBackground(gd);
    }


    private void initEvents() {
        mLockPatternView.setLockListener(new LockPatternView.OnLockListener() {
            @Override
            public void getStringPassword(String password) {
                if (isFirst) {
                    mTitleTv.setText(getString(R.string.reset_gestures_password));
                    isFirst = false;
                    mPassword = password;
                    mClearBtn.setVisibility(View.VISIBLE);
                    return;
                }

                //不是第一次输入密码
                if (!password.equals(mPassword)) {
                    ToastUtils.showShort("两次密码不一致，请重新设置");
                    mTitleTv.setText(getString(R.string.set_gestures_password));
                    clearPassword();
                    return;
                }

                ToastUtils.showShort(getString(R.string.set_gestures_password_success));
                setPasswordToPreference(password);
                setResult(RESULT_OK);
                SetLockActivity.this.finish();
            }

            @Override
            public boolean isPassword() {
                return false;
            }
        });

    }

    @OnClick(R.id.btn_password_clear)
    void clearPassword() {
        mPassword = "";
        isFirst = true;
        mClearBtn.setVisibility(View.GONE);
    }

    private void setPasswordToPreference(String password) {
        PreferencesManager.getInstance().saveLockPassword(password);
    }

}
