package com.yueyue.todolist.event;

/**
 * author : yueyue on 2018/3/19 18:53
 * desc   :
 */

public class MainTabsShowModeEvent {
    private int mode;

    public MainTabsShowModeEvent(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

}
