package com.yueyue.todolist.component;

import android.content.Context;
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
    public static final String TAG = CachePlanTaskStore.class.getSimpleName();

    private List<PlanTask> mCachePlanTaskList = new ArrayList<>();
    private HashSet<OnPlanTaskChangedListener> mOnPlanTaskChangedListeners = new HashSet<>();

    private CachePlanTaskStore() {
    }

    public static CachePlanTaskStore getInstance() {
        return SingletonHolder.sInstance;
    }

    private static final class SingletonHolder {
        private static final CachePlanTaskStore sInstance = new CachePlanTaskStore();
    }


    public static void initialize(Context context) {
        Log.d(TAG, "initialize , start intent service: UserActionService");
        UserActionService.startService(context, UserActionService.INTENT_ACTION_CACHEPLANTASK);
    }


    public void setCachePlanTaskList(List<PlanTask> list, boolean needNotify) {
        if (list == null || list.size() == 0) return;
        mCachePlanTaskList.clear();
        mCachePlanTaskList.addAll(list);
        if (needNotify) {
            notifyPlanTaskChanged();
        }
    }


    public void addPlanTask(PlanTask task, boolean needNotify) {
        if (mCachePlanTaskList == null || task == null) {
            return;
        }


        removePlanTask(task, false);

        mCachePlanTaskList.add(task);
        if (needNotify) {
            notifyPlanTaskChanged();
        }
    }

    public void removePlanTask(PlanTask task, boolean needNotify) {
        if (task == null || mCachePlanTaskList == null)
            return;

        Iterator<PlanTask> iterator = mCachePlanTaskList.iterator();
        while (iterator.hasNext()) {
            PlanTask planTask = iterator.next();
            if (planTask.getTaskId() == task.getTaskId()) {
                iterator.remove();
                break;
            }
        }

        if (needNotify) {
            notifyPlanTaskChanged();
        }
    }

    public void updatePlanTaskState(PlanTask task, boolean needNotify) {
        if (mCachePlanTaskList == null || mCachePlanTaskList.size() <= 0)
            return;

        for (PlanTask planTask : mCachePlanTaskList) {
            if (planTask.getTaskId() == task.getTaskId()) {
                planTask.state = task.state;
                break;
            }
        }

        if (needNotify) {
            notifyPlanTaskChanged();
        }
    }


    public void reset() {
        mCachePlanTaskList.clear();
    }


    public interface OnPlanTaskChangedListener {
        void onPlanTaskChanged();
    }


    private void notifyPlanTaskChanged() {
        if (mOnPlanTaskChangedListeners == null) {
            return;
        }

        for (OnPlanTaskChangedListener listener : mOnPlanTaskChangedListeners) {
            listener.onPlanTaskChanged();
        }
    }

    public void addOnPlanTaskChangedListener(OnPlanTaskChangedListener listener) {
        if (listener == null)
            return;

        synchronized (CachePlanTaskStore.class) {
            if (!mOnPlanTaskChangedListeners.contains(listener)) {
                mOnPlanTaskChangedListeners.add(listener);
            }
        }
    }

    public void removePlanTaskChangedListener(OnPlanTaskChangedListener listener) {
        if (listener == null)
            return;
        synchronized (CachePlanTaskStore.class) {
            mOnPlanTaskChangedListeners.remove(listener);
        }
    }
}
