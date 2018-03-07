package com.yueyue.todolist.modules.details.impl;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * author : yueyue on 2018/3/5 11:35
 * desc   :
 */
@Deprecated
public class TextWatcherImpl implements TextWatcher {

    /**
     * @param s     上一次文字（即你还没有操作之前看到的那些）
     * @param start 起始光标
     * @param count 选择数量
     * @param after 替换增加的文字数
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * @param s      最后的文字（即你进行操作之后看到的那些）
     * @param start  起始光标
     * @param before 选择数量
     * @param count  添加的数量
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * @param s 最后的文字（即你进行操作之后看到的那些）
     */
    @Override
    public void afterTextChanged(Editable s) {

    }
}
