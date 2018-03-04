package com.yueyue.todolist.component;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yueyue.todolist.common.utils.entity.PlanTask;
import com.yueyue.todolist.modules.service.UserActionService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * author : yueyue on 2018/3/3 10:16
 * desc   :
 */

public class CachePlanTaskStore {
    public static final String TAG = "CachePlanTaskStore";

    private volatile static CachePlanTaskStore sCachePlanTaskStore;
    private List<PlanTask> mCachePlanTaskList;
    private boolean mIsPlanTaskInitFinished;
    private final Object mCachePlanTaskStoreLock = new Object();
    private HashSet<OnPlanTaskChangedListener> mOnPlanTaskChangedListeners;

    public interface OnPlanTaskChangedListener {
        void onPlanTaskChanged();
    }

    public static CachePlanTaskStore getInstance() {
        if (sCachePlanTaskStore == null) {
            synchronized (CachePlanTaskStore.class) {
                if (sCachePlanTaskStore == null) {
                    sCachePlanTaskStore = new CachePlanTaskStore();
                }
            }
        }
        return sCachePlanTaskStore;
    }

    public static void initialize(Context context) {
        Log.d(TAG, "initialize , start intent service: UserActionService");
        Intent intent = new Intent(context, UserActionService.class);
        intent.setAction(UserActionService.INTENT_ACTION_CACHEPLANTASK);
        context.startService(intent);
    }

    private CachePlanTaskStore() {
        mCachePlanTaskList = new ArrayList<>();
        mOnPlanTaskChangedListeners = new HashSet<>();
    }

    public void setCachePlanTaskList(List<PlanTask> list, boolean needNotify) {
        synchronized (mCachePlanTaskStoreLock) {
            reset();
            mCachePlanTaskList = list;
            mIsPlanTaskInitFinished = true;
            if (needNotify) {
                notifyPlanTaskChanged();
            }
        }
    }

    public List<PlanTask> getCachePlanTaskList() {
        synchronized (mCachePlanTaskStoreLock) {
            List<PlanTask> planTaskList = new ArrayList<>();
            if (mCachePlanTaskList == null || mCachePlanTaskList.size() <= 0)
                return planTaskList;

            return mCachePlanTaskList;
        }
    }

    public void addPlanTask(PlanTask task, boolean needNotify) {
        synchronized (mCachePlanTaskStoreLock) {
            if (mCachePlanTaskList == null)
                return;

            if (mCachePlanTaskList.size() > 0) {
                Iterator iterator = mCachePlanTaskList.iterator();
                while (iterator.hasNext()) {
                    PlanTask planTask = (PlanTask) iterator.next();
                    if (planTask.taskId.equals(task.taskId)) {
                        mCachePlanTaskList.remove(planTask);
                        break;
                    }
                }
            }
            mCachePlanTaskList.add(task);
            if (needNotify) {
                notifyPlanTaskChanged();
            }
        }
    }

    public void removePlanTask(PlanTask task, boolean needNotify) {
        synchronized (mCachePlanTaskStoreLock) {
            if (mCachePlanTaskList == null || mCachePlanTaskList.size() <= 0)
                return;

            Iterator<PlanTask> iterator = mCachePlanTaskList.iterator();
            while (iterator.hasNext()) {
                PlanTask planTask = iterator.next();
                if (planTask.taskId.equals(task.taskId)) {
                    iterator.remove();
                    break;
                }
            }

            if (needNotify) {
                notifyPlanTaskChanged();
            }
        }
    }

    public void updatePlanTaskState(PlanTask task, boolean needNotify) {
        synchronized (mCachePlanTaskStoreLock) {
            if (mCachePlanTaskList == null || mCachePlanTaskList.size() <= 0)
                return;

            for (int i = 0; i < mCachePlanTaskList.size(); i++) {
                PlanTask planTask = mCachePlanTaskList.get(i);
                if (planTask.taskId.equals(task.taskId)) {
                    planTask.state = task.state;
                    break;
                }
            }
            if (needNotify) {
                notifyPlanTaskChanged();
            }
        }
    }

    public boolean isPlanTaskInitializedFinished() {
        return mIsPlanTaskInitFinished;
    }

    public void reset() {
        synchronized (mCachePlanTaskStoreLock) {
            mCachePlanTaskList.clear();
        }
    }

    public void notifyPlanTaskChanged() {
        if (mOnPlanTaskChangedListeners == null || mOnPlanTaskChangedListeners.size() <= 0)
            return;

        synchronized (mCachePlanTaskStoreLock) {
            for (OnPlanTaskChangedListener listener : mOnPlanTaskChangedListeners) {
                listener.onPlanTaskChanged();
            }
        }
    }

    public void addOnPlanTaskChangedListener(OnPlanTaskChangedListener listener) {
        if (listener == null)
            return;

        synchronized (mCachePlanTaskStoreLock) {
            if (!mOnPlanTaskChangedListeners.contains(listener)) {
                mOnPlanTaskChangedListeners.add(listener);
            }
        }
    }

    public void removePlanTaskChangedListener(OnPlanTaskChangedListener listener) {
        if (listener == null)
            return;

        synchronized (mCachePlanTaskStoreLock) {
            mOnPlanTaskChangedListeners.remove(listener);
        }
    }
}
