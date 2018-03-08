package com.yueyue.todolist.modules.main.impl;


import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.listener.OnItemSwipeListener;

/**
 * author : yueyue on 2018/3/8 15:00
 * desc   :
 */

public abstract class OnItemSwipeListenerImpl implements OnItemSwipeListener {
    @Override
    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

    }

    @Override
    public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

    }

    @Override
    public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {

    }

    @Override
    public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder,
                                  float dX, float dY, boolean isCurrentlyActive) {

    }
}
