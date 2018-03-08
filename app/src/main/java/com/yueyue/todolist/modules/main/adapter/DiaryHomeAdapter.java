package com.yueyue.todolist.modules.main.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yueyue.todolist.modules.main.domain.DiaryEntity;

import java.util.List;

/**
 * author : yueyue on 2018/3/8 13:57
 * desc   :
 */

public class DiaryHomeAdapter extends BaseItemDraggableAdapter<DiaryEntity, BaseViewHolder> {

    public DiaryHomeAdapter(int layoutResId, @Nullable List<DiaryEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiaryEntity item) {

    }
}
