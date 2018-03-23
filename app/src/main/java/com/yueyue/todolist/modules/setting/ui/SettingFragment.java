package com.yueyue.todolist.modules.setting.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.CleanUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.bugtags.library.Bugtags;
import com.yueyue.todolist.R;
import com.yueyue.todolist.component.ImageLoader;
import com.yueyue.todolist.component.PreferencesManager;
import com.yueyue.todolist.modules.main.db.NoteDbHelper;
import com.yueyue.todolist.modules.personal.PersonalActivity;
import com.yueyue.todolist.modules.service.AutoUpdateService;
import com.yueyue.todolist.modules.setting.impl.OnSeekBarChangeListenerImpl;

import java.io.File;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author : yueyue on 2018/3/22 21:26
 * desc   :
 */

public class SettingFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener,
        Preference.OnPreferenceChangeListener {

    private static String TAG = SettingFragment.class.getSimpleName();

    public static final String MODIFY_LOCK = "modify_lock";//修改私密便签密码
    public static final String CLEAR_RECYCLE_IN = "clear_recycle_in";//清空回收站


    public static final String CHANGE_UPDATE_TIME = "change_update_time";//自动刷新频率
    public static final String NOTIFICATION_MODEL = "notification_model";//通知栏常驻


    public static final String AUTO_CHECK_VERSION = "auto_check_version";//自动检查更新
    public static final String APP_FEEDBACK = "app_feedback";//应用反馈
    public static final String LINK_ME = "link_me";//应用反馈
    public static final String CLEAR_CACHE = "clear_cache";//清除缓存

    private Preference mModifyLock;
    private Preference mClearRecycleIn;

    private Preference mChangeUpdateTime;
    private CheckBoxPreference mNotificationModel;

    private Preference mClearCache;
    private EditTextPreference mAppFeedback;
    private SwitchPreference mAutoCheckVersion;
    private Preference mLinkMe;

    private PreferencesManager mPreferencesManager;

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        mPreferencesManager = PreferencesManager.getInstance();

        mModifyLock = findPreference(MODIFY_LOCK);
        mClearRecycleIn = findPreference(CLEAR_RECYCLE_IN);

        mChangeUpdateTime = findPreference(CHANGE_UPDATE_TIME);
        mNotificationModel = (CheckBoxPreference) findPreference(NOTIFICATION_MODEL);

        mAutoCheckVersion = (SwitchPreference) findPreference(AUTO_CHECK_VERSION);
        mAppFeedback = (EditTextPreference) findPreference(APP_FEEDBACK);
        mClearCache = findPreference(CLEAR_CACHE);
        mLinkMe = findPreference(LINK_ME);


        String updateStr = mPreferencesManager.getAutoUpdate() == 0 ? "禁止刷新" :
                "每" + mPreferencesManager.getAutoUpdate() + "小时更新";
        mChangeUpdateTime.setSummary(updateStr);

        boolean resident = mPreferencesManager.getNotificationResident();
        mNotificationModel.setChecked(resident);

        boolean autoCheckVersion = mPreferencesManager.getIsAutoCheckVersion(true);
        mAutoCheckVersion.setChecked(autoCheckVersion);


        refreshCache();


        initListener();
    }

    private void initListener() {
        mModifyLock.setOnPreferenceClickListener(this);
        mClearRecycleIn.setOnPreferenceClickListener(this);
        mClearCache.setOnPreferenceClickListener(this);
        mChangeUpdateTime.setOnPreferenceClickListener(this);
        mAutoCheckVersion.setOnPreferenceClickListener(this);
        mLinkMe.setOnPreferenceClickListener(this);

        mNotificationModel.setOnPreferenceChangeListener(this);
        mAppFeedback.setOnPreferenceChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == rootView) {
            rootView = super.onCreateView(inflater, container, savedInstanceState);
        }
        return rootView;

    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        switch (key) {
            case MODIFY_LOCK:
                ToastUtils.showShort("该功能还在测试,暂时不开放");
                break;
            case CLEAR_RECYCLE_IN:
                NoteDbHelper.getInstance().deleteAllNoteRecycleIn();
                ToastUtils.showShort(getString(R.string.clear_complete));
                break;
            case CHANGE_UPDATE_TIME:
                showUpdateDialog();
                break;
            case AUTO_CHECK_VERSION:
                boolean autoCheckVersion = mPreferencesManager.getIsAutoCheckVersion(true);
                mPreferencesManager.saveIsAutoCheckVersion(!autoCheckVersion);
                mAutoCheckVersion.setChecked(!autoCheckVersion);
                break;
            case CLEAR_CACHE:
                ImageLoader.clear(getActivity());
                Observable.just(CleanUtils.cleanInternalCache())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(aBoolean -> aBoolean)
                        .doOnNext(success -> {
                            if (success) {
                                refreshCache();
                                Snackbar.make(rootView, "缓存已清除", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(rootView, "缓存清除失败", Snackbar.LENGTH_SHORT).show();
                            }
                        })
                        .subscribe();
                break;
            case LINK_ME:
                PersonalActivity.launch(getActivity());
                break;
            default:
                break;

        }

        return true;
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        switch (key) {
            case APP_FEEDBACK:
                if (TextUtils.isEmpty((CharSequence) newValue)) return true;
                Bugtags.sendFeedback((String) newValue);
                ToastUtils.showShort(getString(R.string.thanks_for_feedback));
                break;
            case NOTIFICATION_MODEL:
                boolean b = (boolean) newValue;
                mPreferencesManager.saveNotificationResident(b);
                break;
            default:
                break;
        }

        return true;
    }


    private void showUpdateDialog() {
        //将 SeekBar 放入 Dialog 的方案 http://stackoverflow.com/questions/7184104/how-do-i-put-a-seek-bar-in-an-alert-dialog
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.dialog_update, (ViewGroup) getActivity().findViewById(
                R.id.dialog_root));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(dialogLayout);
        final AlertDialog alertDialog = builder.create();

        final SeekBar mSeekBar = (SeekBar) dialogLayout.findViewById(R.id.time_seekbar);
        final TextView tvShowHour = (TextView) dialogLayout.findViewById(R.id.tv_showhour);
        TextView tvDone = (TextView) dialogLayout.findViewById(R.id.done);

        mSeekBar.setMax(24);
        mSeekBar.setProgress(mPreferencesManager.getAutoUpdate());
        tvShowHour.setText(String.format("每%s小时", mSeekBar.getProgress()));
        alertDialog.show();

        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImpl() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvShowHour.setText(String.format("每%s小时", mSeekBar.getProgress()));
            }
        });
        tvDone.setOnClickListener(v -> {
            mPreferencesManager.saveAutoUpdate(mSeekBar.getProgress());
            String updateStr = mPreferencesManager.getAutoUpdate() == 0 ?
                    "禁止刷新"
                    : String.format(Locale.CHINA, "每%d小时更新", mPreferencesManager.getAutoUpdate());
            mChangeUpdateTime.setSummary(updateStr);
            getActivity().startService(new Intent(getActivity(), AutoUpdateService.class));
            alertDialog.dismiss();
        });
    }


    private void refreshCache() {
        String cache = String.format(getString(R.string.set_current_cache) + " %s", getDataSize());
        mClearCache.setSummary(cache);
    }

    private String getDataSize() {
        File file = Utils.getApp().getCacheDir();
        return FileUtils.getDirSize(file);
    }

}
