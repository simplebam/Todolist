package com.yueyue.todolist.modules.main.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yueyue.todolist.R;

/**
 * author : yueyue on 2018/3/21 16:01
 * desc   :
 */

public class NoteBottomSheetFolderAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public NoteBottomSheetFolderAdapter() {
        super(R.layout.item_note_bottom_folder);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_folder_title_bottom_sheet,item);
    }
}