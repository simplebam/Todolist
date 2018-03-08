package com.yueyue.todolist.modules.diary.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hubert.library.Builder;
import com.app.hubert.library.Controller;
import com.app.hubert.library.HighLight;
import com.app.hubert.library.NewbieGuide;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseActivity;
import com.yueyue.todolist.common.utils.DateUtils;
import com.yueyue.todolist.modules.main.db.DbHelper;
import com.yueyue.todolist.modules.main.domain.DiaryEntity;
import com.yueyue.todolist.modules.main.impl.OnGuideChangedListenerImpl;
import com.yueyue.todolist.widget.CenteredImageSpan;
import com.yueyue.todolist.widget.LinedEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;

public class AddDiaryActivity extends BaseActivity {
    // FIXME: 2018/3/8 做完数据查询然后做修改diary
    private static final String TAG = AddDiaryActivity.class.getSimpleName();

    private static final String KEY_GUIDE_TIME = "guide_time";
    private static final String KEY_GUIDE_SAVE = "guide_save";
    private static final String TAG_DATEPICKER_DIALOG = "datePicker_Dialog";
    private static final String TAG_TIMEPICKER_DIALOG = "timePicker_Dialog";

    private static final String LAUNCH_TYPE = "launch_type";
    public static final String TYPE_ADD = "add";
    public static final String TYPE_MODIFY = "modify";

    private static final String KEY_EXTRA_DIARY = "modify";

    @BindView(R.id.tv_toolbar_time)
    TextView mTVToolbarTime;
    @BindView(R.id.tv_toolbar_date)
    TextView mTVToolbarDate;

    @BindView(R.id.et_body_title)
    EditText mEtBodyTitle;
    @BindView(R.id.et_body_content)
    LinedEditText mEtBodyContent;


    @BindView(R.id.fam_labels)
    FloatingActionsMenu mFamLabels;
    @BindView(R.id.fab_share)
    FloatingActionButton mFabShare;
    @BindView(R.id.fab_save)
    FloatingActionButton mFabSave;

    private Calendar mCalendar = Calendar.getInstance(Locale.CHINA);
    private String showType;
    private DiaryEntity mDiaryEntity;

    /**
     * @param diaryEntity add也要创建DiaryEntity实体带过来!!!
     */
    public static void launch(Context context, String showType, DiaryEntity diaryEntity) {
        Intent intent = new Intent(context, AddDiaryActivity.class);
        intent.putExtra(LAUNCH_TYPE, showType);
        if (diaryEntity != null) {
            intent.putExtra(KEY_EXTRA_DIARY, diaryEntity);
        }
        context.startActivity(intent);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_add_diary;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar();
        setupView();
        showGuideTime();
    }

    private void initToolBar() {
        setToolbarTitle(getString(R.string.note));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

    }

    private void setupView() {
        Intent intent = getIntent();
        showType = intent.getStringExtra(LAUNCH_TYPE);
        mDiaryEntity = intent.getParcelableExtra(KEY_EXTRA_DIARY);

        if (mDiaryEntity != null) {
            mEtBodyTitle.setText(mDiaryEntity.title);
            mEtBodyContent.setText(mDiaryEntity.content);
            mCalendar.setTime(new Date(mDiaryEntity.time));
        }

        setYearMonthDay();
        setHourMinute();

    }


    private void setYearMonthDay() {
        StringBuilder sb = new StringBuilder();
        int distanceDays = (int) DateUtils.getDistanceDaysToNow(mCalendar.getTime());
        switch (distanceDays) {
            case 0:
                sb.append(getString(R.string.today));
                break;
            case 1:
                sb.append(getString(R.string.tomorrow));
                break;
            default:
                int currentYear = Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
                int year = mCalendar.get(Calendar.YEAR);
                if (currentYear != year) {
                    sb.append(year).append(getString(R.string.year)).append("-");
                }
                String dateStr = DateUtils.date2String(DateUtils.DEFUALT_DATE_FORMAT3, mCalendar.getTime());
                sb.append(dateStr);
                break;
        }
        mTVToolbarDate.setText(createStringWithLeftPicture(R.drawable.ic_date_plantask, sb.toString()));
    }

    private void setHourMinute() {
        String time = DateUtils.date2String(DateUtils.DEFUALT_DATE_FORMAT4, mCalendar.getTime());
        mTVToolbarTime.setText(createStringWithLeftPicture(R.drawable.ic_time_diary, time));
    }

    private SpannableString createStringWithLeftPicture(int drawableId, String str) {
        Resources res = getResources();
        String replacedStr = "image";
        final SpannableString spannableString = new SpannableString(replacedStr + str);
        Drawable drawable = res.getDrawable(drawableId);
        drawable.setBounds(0, 0, ConvertUtils.dp2px(23), ConvertUtils.dp2px(23));
        CenteredImageSpan span = new CenteredImageSpan(drawable);
        spannableString.setSpan(span, 0, replacedStr.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    @OnClick(R.id.fab_share)
    public void share() {
        // FIXME: 2018/3/8  文字合成图片
        Toast.makeText(this, "文字合成图片分享", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        back();
    }

    @OnClick(R.id.iv_toolbar_back)
    public void back() {
        String bodyTitle = mEtBodyTitle.getText().toString();
        String bodyContent = mEtBodyContent.getText().toString();

        boolean titleChanged = !TextUtils.equals(mDiaryEntity.title, bodyTitle);
        boolean contentChanged = !TextUtils.equals(mDiaryEntity.content, bodyContent);
        boolean timeChanged = mDiaryEntity.time != mCalendar.getTimeInMillis();

        if (!titleChanged && !contentChanged && !timeChanged) {
            finish();
        } else {
            showSaveDialog(titleChanged, contentChanged, timeChanged);
        }

    }

    @OnClick(R.id.fab_save)
    public void save() {

        String bodyTitle = mEtBodyTitle.getText().toString();
        String bodyContent = mEtBodyContent.getText().toString();

        boolean titleChanged = !TextUtils.equals(mDiaryEntity.title, bodyTitle);
        boolean contentChanged = !TextUtils.equals(mDiaryEntity.content, bodyContent);
        boolean timeChanged = mDiaryEntity.time != mCalendar.getTimeInMillis();

        if (!titleChanged && !contentChanged && !timeChanged) {
            finish();
        } else {
            showSaveDialog(titleChanged, contentChanged, timeChanged);
        }


    }

    private void showSaveDialog(boolean titleChanged, boolean contentChanged, boolean timeChanged) {
        SaveDialog dialog = new SaveDialog(this);
        dialog.setSubTitle(spanDialogSubTitle(titleChanged, contentChanged, timeChanged))
                .setOnSaveListener(v -> {
                    dialog.dimissDialog();
                    savePlanTask();
                })
                .setOnExitListener(v -> {
                    dialog.dimissDialog();
                    finish();
                })
                .showDialog();

    }


    private SpannableString spanDialogSubTitle(boolean titleChanged, boolean contentChanged, boolean timeChanged) {

        StringBuilder sb = new StringBuilder(getString(R.string.modify));
        if (titleChanged) {
            sb.append(getString(R.string.title_point));
        }
        if (contentChanged) {
            sb.append(getString(R.string.content_point));
        }
        if (timeChanged) {
            sb.append(getString(R.string.time_point));
        }

        //删除字符串"标题、时间、" 最后那个、
        int pointLastPos = sb.lastIndexOf("、");
        if (pointLastPos == (sb.length() - 1)) {
            sb.deleteCharAt(pointLastPos);
        }

        sb.append(",").append(getString(R.string.not_save_yeah));

        int textSize = 16;

        SpannableString spanString = new SpannableString(sb.toString());

        ArrayList<Integer> TextPosList = new ArrayList<>();
        int textTitlePos = sb.indexOf(getString(R.string.title));
        if (textTitlePos != -1) {
            TextPosList.add(textTitlePos);
        }

        int textContentPos = sb.indexOf(getString(R.string.content));
        if (textContentPos != -1) {
            TextPosList.add(textContentPos);
        }


        int textTimePos = sb.indexOf(getString(R.string.time));
        if (textTimePos != -1) {
            TextPosList.add(textTimePos);
        }

        for (int startPos : TextPosList) {
            spanString.setSpan(new AbsoluteSizeSpan(textSize, true),
                    startPos,
                    startPos + 2,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                    startPos, startPos + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spanString;
    }

    private void savePlanTask() {
        int msgId=R.string.save_error;
        if (DbHelper.getInstance().addPlantask(mDiaryEntity)) {
            msgId=R.string.save_success;
        }
        ToastUtils.showShort(getString(msgId));
        finish();
    }


    @OnClick(R.id.tv_toolbar_date)
    void showDatePicker() {
        DatePickerDialog pickerDialog = DatePickerDialog.newInstance(
                (datePickerDialog, year, month, dayOfMonth) -> {
                    mCalendar.set(Calendar.YEAR, year);
                    mCalendar.set(Calendar.MONTH, month);
                    mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    setYearMonthDay();
                    showGuideSave();
                },
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));

        pickerDialog.setOnCancelListener(dialog -> showGuideSave());
        pickerDialog.setThemeDark(false);
        pickerDialog.vibrate(false);
        pickerDialog.dismissOnPause(true);
        pickerDialog.showYearPickerFirst(false);
        pickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        pickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        pickerDialog.show(getFragmentManager(), TAG_DATEPICKER_DIALOG);
    }

    @OnClick(R.id.tv_toolbar_time)
    void showTimePicker() {
        TimePickerDialog pickerDialog = TimePickerDialog.newInstance(
                (timePickerDialog, hourOfDay, minute, second) -> {
                    mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mCalendar.set(Calendar.MINUTE, minute);
                    setHourMinute();
                }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);

        pickerDialog.setThemeDark(false);
        pickerDialog.vibrate(false);
        pickerDialog.dismissOnPause(true);
        pickerDialog.enableSeconds(false);
        pickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        pickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        pickerDialog.setOnCancelListener(dialogInterface -> {
        });
        pickerDialog.show(getFragmentManager(), TAG_TIMEPICKER_DIALOG);
    }


    private void showGuideTime() {
        initNewbieGuide(R.layout.guide_time_view_layout, KEY_GUIDE_TIME)
                .setOnGuideChangedListener(new OnGuideChangedListenerImpl() {
                    @Override
                    public void onRemoved(Controller controller) {
                        showDatePicker();
                    }
                })
                .addHighLight(mTVToolbarDate)
                .addHighLight(mTVToolbarTime)
                .build()
                .show();
    }

    private void showGuideSave() {
        initNewbieGuide(R.layout.guide_save_view_layout, KEY_GUIDE_SAVE)
                .setOnGuideChangedListener(new OnGuideChangedListenerImpl())
                .addHighLight(mFamLabels, HighLight.Type.CIRCLE)
                .build()
                .show();

    }

    private Builder initNewbieGuide(int layoutRes, String label) {
        // setBackgroundColor 设置引导层背景色，建议有透明度，默认背景色为：0xb2000000
        //setEveryWhereCancelable 设置点击任何区域消失，默认为true
        //setLayoutRes 自定义的提示layout,第二个可变参数为点击隐藏引导层view的id
        //alwaysShow 是否每次都显示引导层，默认false
        return NewbieGuide.with(this)
                .setBackgroundColor(getResources().getColor(R.color.guide_bg_color))
                .setEveryWhereCancelable(true)
                .setLayoutRes(layoutRes)
                .alwaysShow(false)
                .setLabel(label);
    }

}

