package com.yueyue.todolist.common.listener;

import java.util.List;

/**
 * author : yueyue on 2018/3/13 16:37
 * desc   :
 */

public interface LoadDataCallBack<T> {
     void onSuccess(List<T> list);

     void onFail();

}
