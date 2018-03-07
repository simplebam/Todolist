package com.yueyue.todolist.modules.details.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hubert.library.Builder;
import com.app.hubert.library.Controller;
import com.app.hubert.library.HighLight;
import com.app.hubert.library.NewbieGuide;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yueyue.todolist.R;
import com.yueyue.todolist.base.BaseActivity;
import com.yueyue.todolist.common.utils.StringUtils;
import com.yueyue.todolist.common.utils.entity.DateUtils;
import com.yueyue.todolist.common.utils.entity.PlanTask;
import com.yueyue.todolist.component.CachePlanTaskStore;
import com.yueyue.todolist.modules.details.impl.TextWatcherImpl;
import com.yueyue.todolist.modules.main.impl.OnGuideChangedListenerImpl;
import com.yueyue.todolist.modules.service.UserActionService;
import com.yueyue.todolist.widget.CenteredImageSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 后期需要参考
 */
@Deprecated
public class PlanTaskActivity extends BaseActivity {

    private static final String TAG = PlanTaskActivity.class.getSimpleName();
    private static final String TAG_DATEPICKERDIALOG = "Datepickerdialog";
    private static final String TAG_TIMEPICKERDIALOG = "Timepickerdialog";

    private static final String KEY_SHOW_TYPE = "show_type";
    private static final String KEY_GUIDE_TIME = "guide_time";
    private static final String KEY_GUIDE_SAVE_PLANTASK = "guide_save_plantask";
    private static final String KEY_IS_TOMORROW = "is_tomorrow";
    private static final String KEY_EXTRA_PLANTASK = "extra_plantask";

    private static String mShowType;
    public static final String TYPE_NEW_BUILD = "new_build";

    public static final String TYPE_MODIFY = "modify";


    ////Greek  //Arab

    private Calendar mCalendar = Calendar.getInstance(Locale.CHINA);
    private PlanTask mPlanTask;


    @BindView(R.id.iv_back)
    ImageView mIvBack;

    //--toolbar_header---
    @BindView(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_toolbar_time)
    TextView mTvToolbarTime;
    @BindView(R.id.tv_toolbar_date)
    TextView mTvHeaderDate;

    //-- toolbar_action---
    @BindView(R.id.iv_toolbar_save)
    ImageView mIvToolbarSave;
    @BindView(R.id.iv_toolbar_share)
    ImageView mIvToolbarShare;
    @BindView(R.id.iv_toolbar_list_greek)   //希腊文字排序
            ImageView mIvToolbarListGreek;
    @BindView(R.id.iv_toolbar_list_arab)
    ImageView mIvToolbarListArab;  //阿拉伯排序

    //--正文body --
    @BindView(R.id.et_body_title)
    EditText mEtBodyTitle;
    @BindView(R.id.et_body_content)
    EditText mEtBodyContent;

    public static void launch(Context context, String showType, PlanTask planTask, boolean isTomorrow) {
        mShowType = showType;
        Intent intent = new Intent(context, PlanTaskActivity.class);
        intent.putExtra(KEY_IS_TOMORROW, isTomorrow);
        intent.putExtra(KEY_EXTRA_PLANTASK, planTask);
        context.startActivity(intent);
    }

    public static void launch(Context context, String showType, PlanTask planTask) {
        launch(context, showType, planTask, false);
    }


    @Override
    protected int initLayoutId() {
        return R.layout.activity_plantask;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();
        initData();
        showGuideTime();

    }

    private void initData() {
        mPlanTask = getIntent().getParcelableExtra(KEY_EXTRA_PLANTASK);
        if (mPlanTask != null) {
            mEtBodyTitle.setText(mPlanTask.title);
            mEtBodyContent.setText(mPlanTask.describe);
            mCalendar.setTime(new Date(mPlanTask.time));
        } else {
            mPlanTask = new PlanTask();
        }

        boolean isTomorrow = getIntent().getBooleanExtra(KEY_IS_TOMORROW, false);
        if (isTomorrow) {
            mCalendar.setTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
        }
    }

    private void setupView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        changeActionListState(false);


        boolean isTypeModify = TYPE_MODIFY.equals(mShowType);

        mTvToolbarTitle.setText(isTypeModify ? "查看计划" : "新建计划");
        mEtBodyTitle.setEnabled(!isTypeModify);
        mEtBodyContent.setEnabled(!isTypeModify);
        mTvHeaderDate.setClickable(!isTypeModify);
        mTvToolbarTime.setClickable(!isTypeModify);

        int drawbleId = isTypeModify ? R.drawable.ic_modify_plantask : R.drawable.ic_save_plantask;
        mIvToolbarSave.setImageDrawable(getResources().getDrawable(drawbleId));

        setYearMonthDay();
        setHourMinute();

        initListener();

    }

    private void initListener() {

        mEtBodyContent.setOnFocusChangeListener((v, hasFocus) -> changeActionListState(hasFocus));


        mEtBodyContent.addTextChangedListener(new TextWatcherImpl() {
            private boolean flag = true;
            private final String enterSymbol = "\n";
            Pattern arabPattern = Pattern.compile("^(\\d+)(.*)");

            /**
             * @param s      最后的文字（即你进行操作之后看到的那些）
             * @param start  起始光标
             * @param before 选择数量
             * @param count  添加的数量
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (flag) return;
                if (count == 1 && "\n".equals(s.subSequence(start, start + 1))) {
                    //输入的是一个字符
                    performAddEnter(mEtBodyContent.getText(), s, start);
                }
            }

            /**
             * 处理回车操作
             */
            private void performAddEnter(Editable editable, CharSequence source, int start) {
                flag = true;
                //获取回车之前的字符
                String tempStr = source.subSequence(0, start).toString();

                tempStr = tempStr.trim();
                String firstStr = tempStr.length() > 0 ? String.valueOf(tempStr.charAt(0)) : "";
                if (tempStr.length() > 0) {
                    if (TextUtils.equals(firstStr, TAG_LIST_GREEK)) {
                        editable.insert(start + 1, TAG_LIST_GREEK + " ");
                    } else {
                        Pattern pattern = Pattern.compile("^(\\d+)(.*)");
                        Matcher matchers = pattern.matcher(tempStr);
                        if (matchers.matches()) {//数字开头
                            editable.insert(start + 1, (Integer.parseInt(matchers.group(1)) + 1) + ". ");
                        }
                    }
                }
                flag = false;
            }

        });
    }

    private void changeActionListState(boolean hasFocus) {
        mIvToolbarListGreek.setClickable(hasFocus);
        mIvToolbarListArab.setClickable(hasFocus);
        float alpha = hasFocus ? 1f : 0.5f;
        mIvToolbarListGreek.setAlpha(alpha);
        mIvToolbarListArab.setAlpha(alpha);
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
                }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        pickerDialog.setOnCancelListener(dialog -> showGuideSave());
        pickerDialog.setThemeDark(false);
        pickerDialog.vibrate(false);
        pickerDialog.dismissOnPause(true);
        pickerDialog.showYearPickerFirst(false);
        pickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        pickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        pickerDialog.show(getFragmentManager(), TAG_DATEPICKERDIALOG);
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
        pickerDialog.show(getFragmentManager(), TAG_TIMEPICKERDIALOG);
    }

    private void setHourMinute() {
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);

        StringBuilder sb = new StringBuilder()
                .append(StringUtils.fillZero(hour))
                .append(":")
                .append(StringUtils.fillZero(minute));

        mTvToolbarTime.setText(createStringWithLeftPicture(R.drawable.ic_time_plantask, sb.toString()));
    }


    private void setYearMonthDay() {
        Calendar currentCalendar = Calendar.getInstance(Locale.CHINA);

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH) + 1;
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        StringBuilder sb = new StringBuilder();

        int distanceDays = (int) DateUtils.getDistanceDays(mCalendar.getTime(),
                currentCalendar.getTime());
        switch (distanceDays) {
            case 0:
                sb.append(getString(R.string.today));
                break;
            case 1:
                sb.append(getString(R.string.tomorrow));
                break;
            default:
                if (currentCalendar.get(Calendar.YEAR) != year) {
                    sb.append(year).append(getString(R.string.year));
                }

                sb.append(StringUtils.fillZero(month))
                        .append(getString(R.string.month))
                        .append(StringUtils.fillZero(day))
                        .append(getString(R.string.day));
                break;
        }

        mTvHeaderDate.setText(createStringWithLeftPicture(R.drawable.ic_date_plantask, sb.toString()));
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

    public static final String TAG_LIST_GREEK = "● ";
    public static final String TAG_LIST_GREEK_SIGN = "●";
    public static final String TAG_LIST_ARAB = "1. ";

    @OnClick({R.id.iv_toolbar_list_arab, R.id.iv_toolbar_list_greek})
    void performList(View v) {
        String tag = R.id.iv_toolbar_list_arab == v.getId() ? TAG_LIST_ARAB : TAG_LIST_GREEK;
//        String source = mEtBodyContent.getText().toString();
//        int selectionStart = mEtBodyContent.getSelectionStart();
//        int selectionEnd = mEtBodyContent.getSelectionEnd();
//        String substring = source.substring(0, selectionStart);
//        int line = substring.lastIndexOf(10);
//
//
//        selectionStart = line == -1 ? 0 : (line + 1);
//        substring = source.substring(selectionStart, selectionEnd);
//
//        String[] splits = substring.split("\n");
//        StringBuffer sb = new StringBuffer();
//
//        for (String s : splits) {
//            if (s.length() == 0 && sb.length() != 0) {
//                sb.append("\n");
//                continue;
//            }
//            if (sb.length() > 0) sb.append("\n");
//            if (!s.trim().startsWith(tag)) {
//                //不是 空行或者已经是序号开头
//                sb.append(tag).append(" ").append(s);
//            } else {
//                sb.append(s);
//            }
//        }
//
//        if (sb.length() == 0) {
//            sb.append(tag).append(" ");
//        }
//        mEtBodyContent.getText().replace(selectionStart, selectionEnd, sb.toString());
//        mEtBodyContent.setSelection(sb.length() + selectionStart);
        String source = mEtBodyContent.getText().toString();
        int selectionStart = mEtBodyContent.getSelectionStart();
        int selectionEnd = mEtBodyContent.getSelectionEnd();
        String substring = source.substring(0, selectionStart);
        int line = substring.lastIndexOf(10);


        if (line != -1) {
            selectionStart = line + 1;
        } else {
            selectionStart = 0;
        }
        substring = source.substring(selectionStart, selectionEnd);

        String[] split = substring.split("\n");
        StringBuffer stringBuffer = new StringBuffer();

        if (split != null && split.length > 0)
            for (String s : split) {
                if (s.length() == 0 && stringBuffer.length() != 0) {
                    stringBuffer.append("\n");
                    continue;
                }
                if (!s.trim().startsWith(tag)) {
                    //不是 空行或者已经是序号开头
                    if (stringBuffer.length() > 0) stringBuffer.append("\n");
                    stringBuffer.append(tag).append(s);
                } else {
                    if (stringBuffer.length() > 0) stringBuffer.append("\n");
                    stringBuffer.append(s);
                }
            }

        if (stringBuffer.length() == 0) {
            stringBuffer.append(tag);
        }
        mEtBodyContent.getText().replace(selectionStart, selectionEnd, stringBuffer.toString());
        mEtBodyContent.setSelection(stringBuffer.length() + selectionStart);
    }


    @OnClick(R.id.iv_toolbar_save)
    void save() {
        switch (mShowType) {
            case TYPE_MODIFY:
                mTvToolbarTitle.setText(getString(R.string.modify_plan));
                mIvToolbarSave.setImageDrawable(getDrawable(R.drawable.ic_save_plantask));
                mEtBodyTitle.setEnabled(true);
                mEtBodyContent.setEnabled(true);
                mTvHeaderDate.setClickable(true);
                mTvToolbarTime.setClickable(true);
                break;
            default:
                savePlanTask();
                finish();
                break;
        }

    }

    @OnClick(R.id.iv_back)
    void showSaveDialog() {
        String bodyTitle = mEtBodyTitle.getText().toString();
        String bodyContent = mEtBodyContent.getText().toString();


        boolean titleChanged = !TextUtils.equals(mPlanTask.title, bodyTitle);
        boolean contentChanged = !TextUtils.equals(mPlanTask.describe, bodyContent);
        boolean timeChanged = mPlanTask.time != mCalendar.getTimeInMillis();

        if (!titleChanged && !contentChanged && !timeChanged) {
            finish();
            return;
        }

        SaveDialog dialog = new SaveDialog(this);
        dialog.setSubTitle(spanDialogSubTitle(titleChanged, contentChanged, timeChanged))
                .setOnSaveListener(v -> {
                    dialog.dimissDialog();
                    savePlanTask();
                    finish();
                })
                .setOnExitListener(v -> {
                    dialog.dimissDialog();
                    finish();
                })
                .showDialog();


    }

    @SuppressLint("LongLogTag")
    private void savePlanTask() {

        String bodyTitle = mEtBodyTitle.getText().toString();
        String bodyContent = mEtBodyContent.getText().toString();

        if (TextUtils.isEmpty(bodyTitle) && TextUtils.isEmpty(bodyContent)) {
            return;
        }

        mPlanTask.time = mCalendar.getTimeInMillis();
        mPlanTask.title = TextUtils.isEmpty(bodyTitle) ? "\"标题\"" : bodyTitle;
        mPlanTask.describe = TextUtils.isEmpty(bodyContent) ? "\"描述\"" : bodyContent;


        KeyboardUtils.hideSoftInput(this);

        if (TYPE_NEW_BUILD.equals(mShowType)) {
            CachePlanTaskStore.getInstance().addPlanTask(mPlanTask, true);
            UserActionService.startService(this, UserActionService.INTENT_ACTION_ADD_ONE_PLANTASK, mPlanTask);

        } else {
            CachePlanTaskStore.getInstance().updatePlanTaskState(mPlanTask, true);
            UserActionService.startService(this, UserActionService.INTENT_ACTION_UPDATE_ONE_PLANTASK_STATE, mPlanTask);
        }

    }


    private SpannableString spanDialogSubTitle(boolean titleChanged, boolean contentChanged, boolean timeChanged) {
        if (!titleChanged && !contentChanged && !timeChanged) {
            return null;
        }

        StringBuilder sb = new StringBuilder(getString(R.string.modify));
        if (titleChanged) {
            sb.append(getString(R.string.title_point));
        }
        if (contentChanged) {
            sb.append(getString(R.string.content_point));
        }
        if (timeChanged) {
            sb.append(getString(R.string.time));
        }

        //删除字符串"标题、时间、" 最后那个、
        int pointLastPos = sb.lastIndexOf("、");
        if (pointLastPos != (sb.length() - 1)) {
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


    @Override
    public void onBackPressed() {
        showSaveDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showGuideTime() {
        initNewbieGuide(R.layout.guide_time_view_layout, KEY_GUIDE_TIME)
                .setOnGuideChangedListener(new OnGuideChangedListenerImpl() {
                    @Override
                    public void onRemoved(Controller controller) {
                        showDatePicker();
                    }
                })
                .addHighLight(mTvHeaderDate)
                .addHighLight(mTvToolbarTime)
                .build()
                .show();
    }

    private void showGuideSave() {
        initNewbieGuide(R.layout.guide_save_view_layout, KEY_GUIDE_SAVE_PLANTASK)
                .setOnGuideChangedListener(new OnGuideChangedListenerImpl())
                .addHighLight(mIvToolbarSave, HighLight.Type.CIRCLE)
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
