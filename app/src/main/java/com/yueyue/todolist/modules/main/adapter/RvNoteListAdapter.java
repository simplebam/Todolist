package com.yueyue.todolist.modules.main.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yueyue.todolist.R;
import com.yueyue.todolist.common.C;
import com.yueyue.todolist.common.utils.DateUtils;
import com.yueyue.todolist.component.PreferencesManager;
import com.yueyue.todolist.modules.main.domain.NoteEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author : yueyue on 2018/3/13 20:10
 * desc   :
 */

public class RvNoteListAdapter extends BaseQuickAdapter<NoteEntity, BaseViewHolder> {

    public List<Boolean> mCheckList = new ArrayList<>();

    public List<Boolean> mAllCheckList;
    public List<NoteEntity> mAllDataList;

    public void addData(@NonNull Collection<? extends NoteEntity> newData) {
        addData(0, newData);
        for (int i = 0; i < newData.size(); i++) {
            mCheckList.add(false);
        }
    }

    public void addData(@NonNull NoteEntity data) {
        addData(0, data);
        mCheckList.add(false);
        notifyDataSetChanged();
    }

    public void setNewData(@Nullable List<NoteEntity> data) {
        super.setNewData(data);
        mCheckList.clear();
        for (int i = 0; i < data.size(); i++) {
            mCheckList.add(false);
        }
    }

    public RvNoteListAdapter() {
        super(R.layout.item_note);
    }


    @Override
    protected void convert(BaseViewHolder helper, NoteEntity item) {
        if (isLinearLayoutManager()) {
            setLinearLayout(helper, item);
        } else {
            setGridLayout(helper, item);
        }
    }

    /**
     * 是否是线性布局
     *
     * @describe
     */
    private boolean isLinearLayoutManager() {
        return PreferencesManager.getInstance().getNoteListShowMode(C.STYLE_LINEAR) == C.STYLE_LINEAR;
    }


    /**
     * 设置网格布局
     *
     * @describe
     */
    private void setGridLayout(BaseViewHolder helper, NoteEntity item) {

        helper.addOnClickListener(R.id.cv_note_list_grid);
        helper.addOnLongClickListener(R.id.cv_note_list_grid);

        helper.setGone(R.id.ll_note_list_linear, false);
        helper.setGone(R.id.cv_note_list_grid, true);

        TextView tvContent = helper.getView(R.id.tv_note_list_grid_content);
        if (item.isPrivacy == 1)
            helper.setText(R.id.tv_note_list_grid_content, Utils.getApp().getResources().getString(R.string.note_privacy_and_recycle));
        else
            parseTextContent(tvContent, "" + item.noteContent);

        // 设置便签的时间显示
        setNoteEntityTime(helper, item.modifiedTime);
        // 设置多选按钮
        setCheckBox(helper);
    }

    /**
     * 解析文本中的图片
     */
    private void parseTextContent(TextView textView, String content) {
        // TODO: 2017/7/11 0011 后续可以找到图片后，显示在列表item上 

        textView.setText("");

        Pattern p = Pattern.compile(C.imageTabBefore + "([^<]*)" + C.imageTabAfter);
        Matcher m = p.matcher(content);
        int tempIndex = 0;
        List<String> textList = new ArrayList<>();
        while (m.find()) {

            //  匹配到的数据中，第一个括号的中的内容（这里只有一个括号）
            String temp = m.group(1);

            //  查找图片标签的位置
            int index = content.indexOf(C.imageTabBefore, tempIndex);

            //  将本次开始位置到图片标签间的图片储存起来
            String text = content.substring(tempIndex, index);
            textList.add(text);

            // 将查询起始位置升级
            int flagLength = C.imageTabBefore.length() + C.imageTabAfter.length();
            tempIndex = index + flagLength + temp.length();
        }

        if (textList.size() != 0) {
            for (int i = 0; i < textList.size(); i++) {
                textView.append(textList.get(i));
                textView.append("[图片]");
            }
            // 将最后一个图片标签后面所有的文字添加
            textView.append(content.substring(tempIndex));
        } else {
            textView.setText(content);
        }
    }

    /**
     * 设置线性布局
     */
    private void setLinearLayout(BaseViewHolder helper, NoteEntity item) {

        helper.addOnClickListener(R.id.ll_note_list_line);
        helper.addOnLongClickListener(R.id.ll_note_list_line);

        // 显示竖排布局，隐藏网格布局
        helper.setGone(R.id.cv_note_list_grid, false);
        helper.setGone(R.id.ll_note_list_linear, true);

        TextView tvContent = helper.getView(R.id.tv_note_list_linear_content);
        if (item.isPrivacy == 1)
            helper.setText(R.id.tv_note_list_linear_content,
                    Utils.getApp().getResources().getString(R.string.note_privacy_and_recycle));
        else
            parseTextContent(tvContent, item.noteContent);

        // 设置便签的时间显示
        setNoteEntityTime(helper, item.modifiedTime);

        // 设置便签的分组显示
        setLinearLayoutGroup(helper, item.createdTime);
        // 设置多选按钮
        setCheckBox(helper);
    }

    /**
     * 设置便签的时间显示格式：
     * 便签修改时间与当前时间对比，
     * 同一天的显示为：HH：mm；
     * 同一年的显示为：MM-DD HH:mm
     * 其他显示为：yyyy-MM-DD HH:mm
     *
     * @param time 时间戳
     */
    private void setNoteEntityTime(BaseViewHolder helper, long time) {
        String dateFormatStr;
        if (DateUtils.getDistanceDaysToNow(time) == 0) {
            // 同一天
            dateFormatStr = DateUtils.DateStyle.HH_MM.getValue();
        } else if (DateUtils.isSameYear(time)) {
            //同一年
            dateFormatStr = DateUtils.DateStyle.MM_DD_HH_MM.getValue();
        } else {
            dateFormatStr = DateUtils.DateStyle.YYYY_MM_DD_HH_MM.getValue();
        }

        setNoteEntityTimeInfo(helper, time, new SimpleDateFormat(dateFormatStr));

    }

    /**
     * 设置便签显示的时间
     *
     * @param helper
     * @param time   时间戳
     * @param format 时间格式
     */
    private void setNoteEntityTimeInfo(BaseViewHolder helper, long time, SimpleDateFormat format) {
        if (isLinearLayoutManager()) {
            helper.setText(R.id.tv_note_list_linear_time, TimeUtils.millis2String(time, format));
        } else {
            helper.setText(R.id.tv_note_list_grid_time, TimeUtils.millis2String(time, format));
        }
    }

    /**
     * 设置线性布局时，列表的分组
     *
     * @param helper
     * @param time   时间戳
     * @describe
     */
    private void setLinearLayoutGroup(BaseViewHolder helper, long time) {

        // 当前position
        int position = helper.getLayoutPosition();

        // 如果是列表第一项,或者与上一个便签的创建时间不是在同一月，显示分组信息
        boolean isShow = position == 0 ||
                !DateUtils.isSameMonth(time, getData().get(position - 1).createdTime);
        showLineraLayoutGroup(isShow, helper, time);

    }

    /**
     * 显示是否线性布局时的分组信息
     *
     * @param helper
     * @param isShow 是否显示
     * @param time   时间戳
     * @describe
     */
    private void showLineraLayoutGroup(boolean isShow, BaseViewHolder helper, long time) {
        // 有分组的列，marginTop为8dp,否则，为0dp
        LinearLayout ll = helper.getView(R.id.ll_note_list_linear);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
        if (isShow) {
            helper.setVisible(R.id.tv_note_list_linear_month, true);
            setLinearGroupStyle(helper, time);

            params.setMargins(SizeUtils.dp2px(0), SizeUtils.dp2px(8), SizeUtils.dp2px(0), SizeUtils.dp2px(0));
            ll.setLayoutParams(params);

        } else {
            helper.setVisible(R.id.tv_note_list_linear_month, false);
            params.setMargins(SizeUtils.dp2px(0), SizeUtils.dp2px(0), SizeUtils.dp2px(0), SizeUtils.dp2px(0));
            ll.setLayoutParams(params);
        }
    }

    /**
     * 设置线性布局时的分组的格式
     *
     * @describe
     */
    private void setLinearGroupStyle(BaseViewHolder helper, long time) {

        String dateFormatStr;
        if (DateUtils.isSameYear(time)) {
            // 如果同一年 显示为：x月
            dateFormatStr = DateUtils.DateStyle.MM_CN.getValue();
        } else {
            //否则 显示为：xxxx年x月
            dateFormatStr = DateUtils.DateStyle.YYYY_MM_CN.getValue();
        }

        helper.setText(R.id.tv_note_list_linear_month,
                TimeUtils.millis2String(time, new SimpleDateFormat(dateFormatStr)));
    }


    /**
     * 设置多选按钮
     *
     * @describe
     */
    private void setCheckBox(BaseViewHolder helper) {

        int position = helper.getLayoutPosition();
        CheckBox checkBox;
        if (isLinearLayoutManager()) {
            checkBox = (CheckBox) helper.getView(R.id.cb_note_list_liear_check);
        } else {
            checkBox = (CheckBox) helper.getView(R.id.cb_note_list_grid_check);
        }
        showCheckBox(checkBox, position);
    }

    /**
     * 是否显示多选按钮
     *
     * @describe
     */
    private void showCheckBox(CheckBox checkBox, int position) {
        if (C.isShowMultiSelectAction) {
            checkBox.setVisibility(View.VISIBLE);
            if (mCheckList.get(position))
                checkBox.setChecked(true);
            else
                checkBox.setChecked(false);
        } else {
            checkBox.setVisibility(View.INVISIBLE);
            checkBox.setChecked(false);
        }
    }
}
