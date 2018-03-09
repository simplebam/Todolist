package com.yueyue.todolist.component;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * author : yueyue on 2018/3/9 16:26
 * desc   :
 * quote  :   RxJava 的 Subject - 简书
 *                       https://www.jianshu.com/p/99bd603881bf
 *           Android RxBus的使用 - Angelica - CSDN博客
 *                       http://blog.csdn.net/qq_20785431/article/details/72910619
 *
 */

public class RxBus {
    private final Subject<Object> mBus;

    private RxBus() {
        mBus = PublishSubject.create();
    }

    public static RxBus getDefault() {
        return RxBusHolder.sInstance;
    }

    private static class RxBusHolder {
        private static final RxBus sInstance = new RxBus();
    }

    public void post(Object o) {
        mBus.onNext(o);
    }

    //返回指定类型的Observable实例
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }

    /**
     * 是否已有观察者订阅
     */
    public boolean hasObservers() {
        return mBus.hasObservers();
    }



}
