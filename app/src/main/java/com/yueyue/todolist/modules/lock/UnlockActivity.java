package com.yueyue.todolist.modules.lock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseActivity;
import com.yueyue.todolist.component.PreferencesManager;
import com.yueyue.todolist.modules.main.ui.MainActivity;
import com.yueyue.todolist.widget.lock.LockPatternView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : yueyue on 2018/3/22 09:34
 * desc   :
 */

public class UnlockActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @BindView(R.id.lock_pattern_view)
    LockPatternView mLockPatternView;

    private String mPasswordStr;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, UnlockActivity.class);
        activity.startActivityForResult(intent, MainActivity.UNLOCK_REQUEST_CODE);
    }


    @Override
    protected int initLayoutId() {
        return R.layout.activity_lock;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvents();
    }

    private void initEvents() {
        mLockPatternView.setLockListener(new LockPatternView.OnLockListener() {
            String password = PreferencesManager.getInstance().getLockPassword("");

            @Override
            public void getStringPassword(String password) {
                mPasswordStr = password;
            }

            @Override
            public boolean isPassword() {
                if (!password.equals(mPasswordStr)) {
                    ToastUtils.showShort(getString(R.string.incorrect_password));
                    return false;
                }

                ToastUtils.showShort(getString(R.string.correct_password));
                setResult(RESULT_OK);
                UnlockActivity.this.finish();
                return true;
            }
        });
    }

    @OnClick(R.id.iv_back)
    void back() {
        onBackPressed();
    }

}