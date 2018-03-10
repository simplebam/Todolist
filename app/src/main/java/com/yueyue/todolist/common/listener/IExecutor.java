package com.yueyue.todolist.common.listener;

/**
 * author : yueyue on 2018/3/10 13:19
 * desc   :
 */

public interface IExecutor<T> {
    void execute();

    void onPrepare();

    void onExecuteSuccess(T t);

    void onExecuteFail(Exception e);
}