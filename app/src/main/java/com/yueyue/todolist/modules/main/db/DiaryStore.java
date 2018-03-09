package com.yueyue.todolist.modules.main.db;

import com.yueyue.todolist.common.utils.DateUtils;
import com.yueyue.todolist.component.PLog;
import com.yueyue.todolist.modules.main.domain.DiaryEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * author : yueyue on 2018/3/3 10:16
 * desc   :
 */

public class DiaryStore {
    public static final String TAG = DiaryStore.class.getSimpleName();

    private HashSet<OnPlanTaskChangedListener> mOnPlanTaskChangedListeners = new HashSet<>();

    private DiaryStore() {
    }

    public static DiaryStore getInstance() {
        return SingletonHolder.sInstance;
    }

    private static final class SingletonHolder {
        private static final DiaryStore sInstance = new DiaryStore();
    }


    public List<DiaryEntity> getDiaryFromDb(int offset, String calendarStr) {
        try {
            return DiaryDbHelper.getInstance().pageSearch(offset, "calendar=?", calendarStr);
        } catch (Exception e) {
            PLog.e(TAG, "getTodayPlantask:" + e.toString());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<DiaryEntity> getTodayAllDiary(int offset) {
        Date date = Calendar.getInstance(Locale.CHINA).getTime();
        String dateStr = DateUtils.date2String(DateUtils.DEFUALT_DATE_FORMAT2, date);
        return getDiaryFromDb(offset, dateStr);
    }

    public List<DiaryEntity> getTomorrowAllDiary(int offset) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        String dateStr = DateUtils.date2String(DateUtils.DEFUALT_DATE_FORMAT2, calendar.getTime());
        return getDiaryFromDb(offset, dateStr);
    }




//    public void setCachePlanTaskList(List<DiaryEntity> list, boolean needNotify) {
//        if (list == null || list.size() == 0) return;
//        if (needNotify) {
//            notifyPlanTaskChanged();
//        }
//    }


//    public void addPlanTask(DiaryEntity task, boolean needNotify) {
//        if (mCachePlanTaskList == null || task == null) {
//            return;
//        }
//
//
//        removePlanTask(task, false);
//
//        mCachePlanTaskList.add(task);
//        if (needNotify) {
//            notifyPlanTaskChanged();
//        }
//    }
//
//    public void removePlanTask(DiaryEntity task, boolean needNotify) {
//        if (task == null || mCachePlanTaskList == null)
//            return;
//
//        Iterator<DiaryEntity> iterator = mCachePlanTaskList.iterator();
//        while (iterator.hasNext()) {
//            DiaryEntity planTask = iterator.next();
//            if (planTask.getTaskId() == task.getTaskId()) {
//                iterator.remove();
//                break;
//            }
//        }
//
//        if (needNotify) {
//            notifyPlanTaskChanged();
//        }
//    }
//
//    public void updatePlanTaskState(DiaryEntity task, boolean needNotify) {
//        if (mCachePlanTaskList == null || mCachePlanTaskList.size() <= 0)
//            return;
//
//        for (DiaryEntity planTask : mCachePlanTaskList) {
//            if (planTask.getTaskId() == task.getTaskId()) {
//                planTask.state = task.state;
//                break;
//            }
//        }
//
//        if (needNotify) {
//            notifyPlanTaskChanged();
//        }
//    }
//
//
//    public void reset() {
//        mCachePlanTaskList.clear();
//    }


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

        synchronized (DiaryStore.class) {
            if (!mOnPlanTaskChangedListeners.contains(listener)) {
                mOnPlanTaskChangedListeners.add(listener);
            }
        }
    }

    public void removePlanTaskChangedListener(OnPlanTaskChangedListener listener) {
        if (listener == null)
            return;
        synchronized (DiaryStore.class) {
            mOnPlanTaskChangedListeners.remove(listener);
        }
    }
}
