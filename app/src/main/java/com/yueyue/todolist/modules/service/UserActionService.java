package com.yueyue.todolist.modules.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yueyue.todolist.common.utils.entity.PlanTask;
import com.yueyue.todolist.component.CachePlanTaskStore;
import com.yueyue.todolist.modules.service.db.AbsolutePlanContract;

import java.util.ArrayList;
import java.util.List;

/**
 * author : yueyue on 2018/3/3 10:23
 * desc   :
 */

public class UserActionService extends IntentService {
    public static final String TAG = UserActionService.class.getSimpleName();

    public static final String INTENT_ACTION_CACHEPLANTASK = "com.yueyue.todolist.service.action.CACHE_PLANTASK";
    public static final String INTENT_ACTION_ADD_ONE_PLANTASK = "com.yueyue.todolist.service.action.ADD_ONE_PLANTASK";
    public static final String INTENT_ACTION_UPDATE_ONE_PLANTASK_STATE = "com.yueyue.todolist.service.action.UPDATE_ONE_PLANTASK_STATE";
    public static final String INTENT_ACTION_REMOVE_ONE_PLANTASK = "com.yueyue.todolist.service.action.REMOVE_ONE_PLANTASK";

    public static final String EXTRA_PLANTASK = "extra_plantask";

    public static final Uri URI_PLANTASK = AbsolutePlanContract.PlanTask.CONTENT_URI_PLANTASK;

    public UserActionService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();

        switch (action) {
            case INTENT_ACTION_CACHEPLANTASK:
                cachePlanTask();
                break;
            case INTENT_ACTION_ADD_ONE_PLANTASK:
                addOnePlantask(intent);
                break;
            case INTENT_ACTION_UPDATE_ONE_PLANTASK_STATE:
                updateOnePlantaskState(intent);
                break;
            case INTENT_ACTION_REMOVE_ONE_PLANTASK:
                removeOnePlantask(intent);
                break;
            default:
                break;
        }

    }

    private void cachePlanTask() {
        List<PlanTask> planTaskList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(URI_PLANTASK, null, null, null, null);
            if (cursor == null) {
                Log.e(TAG, "cachePlanTask, cursor is null");
                return;
            }
            while (cursor.moveToNext()) {
                PlanTask planTask = new PlanTask();

                planTask.taskId = cursor.getString(cursor.getColumnIndex(AbsolutePlanContract.PlanTask.TASK_ID));
                planTask.priority = cursor.getInt(cursor.getColumnIndex(AbsolutePlanContract.PlanTask.TASK_PRIORITY));
                planTask.title = cursor.getString(cursor.getColumnIndex(AbsolutePlanContract.PlanTask.TASK_TITLE));
                planTask.describe = cursor.getString(cursor.getColumnIndex(AbsolutePlanContract.PlanTask.TASK_DESCRIBE));
                planTask.time = cursor.getLong(cursor.getColumnIndex(AbsolutePlanContract.PlanTask.TASK_TIME));
                planTask.state = cursor.getInt(cursor.getColumnIndex(AbsolutePlanContract.PlanTask.TASK_STATE));

                planTaskList.add(planTask);
            }
        } catch (Exception ex) {
            Log.e(TAG, "cachePlanTask, ex: " + ex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        CachePlanTaskStore.getInstance().setCachePlanTaskList(planTaskList, true);
    }

    private void addOnePlantask(Intent intent) {
        PlanTask task = intent.getParcelableExtra(EXTRA_PLANTASK);
        if (task == null) {
            Log.e(TAG, "addOnePlantask, task is null");
            return;
        }

        ContentValues values = new ContentValues();
        values.clear();
        values.put(AbsolutePlanContract.PlanTask.TASK_ID, task.taskId);
        values.put(AbsolutePlanContract.PlanTask.TASK_PRIORITY, task.priority);
        values.put(AbsolutePlanContract.PlanTask.TASK_TITLE, task.title);
        values.put(AbsolutePlanContract.PlanTask.TASK_DESCRIBE, task.describe);
        values.put(AbsolutePlanContract.PlanTask.TASK_TIME, task.time);
        values.put(AbsolutePlanContract.PlanTask.TASK_STATE, task.state);

        Cursor cursor = null;
        try {
            String where = AbsolutePlanContract.PlanTask.TASK_ID + " = ?";
            cursor = getContentResolver().query(URI_PLANTASK, null, where, new String[]{task.taskId}, null);
            if (cursor != null && cursor.getCount() > 0) {
                int updateCount = getContentResolver().update(URI_PLANTASK, values, where, new String[]{task.taskId});
                if (updateCount > 0) {
                    Log.d(TAG, "update onePlantask success, plantask taskId : " + task.taskId);
                } else {
                    Log.e(TAG, "update onePlantask failed, plantask taskId : " + task.taskId);
                }
            } else {
                Uri uri = getContentResolver().insert(URI_PLANTASK, values);
                if (uri != null) {
                    Log.d(TAG, "addOnePlantask success, plantask taskId : " + task.taskId + ", uri: " + uri);
                } else {
                    Log.e(TAG, "addOnePlantask failed, plantask taskId : " + task.taskId);
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "addOnePlantask, ex: " + ex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void updateOnePlantaskState(Intent intent) {
        PlanTask task = intent.getParcelableExtra(EXTRA_PLANTASK);
        if (task == null) {
            Log.e(TAG, "updateOnePlantaskState, task is null");
            return;
        }

        Cursor cursor = null;
        int cursorCount = 0;
        String where = "";
        try {
            where = AbsolutePlanContract.PlanTask.TASK_ID + " = ?";
            cursor = getContentResolver().query(URI_PLANTASK, null, where,
                    new String[]{task.taskId}, null);
            if (cursor == null) {
                return;
            }

            cursorCount = cursor.getCount();
        } catch (Exception ex) {
            Log.e(TAG, "updateOnePlantaskState, 1 ex: " + ex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        ContentValues values = new ContentValues();
        values.clear();
        values.put(AbsolutePlanContract.PlanTask.TASK_ID, task.taskId);
        values.put(AbsolutePlanContract.PlanTask.TASK_PRIORITY, task.priority);
        values.put(AbsolutePlanContract.PlanTask.TASK_TITLE, task.title);
        values.put(AbsolutePlanContract.PlanTask.TASK_DESCRIBE, task.describe);
        values.put(AbsolutePlanContract.PlanTask.TASK_TIME, task.time);
        values.put(AbsolutePlanContract.PlanTask.TASK_STATE, task.state);

        try {
            if (cursorCount > 0) {
                int count = getContentResolver().update(URI_PLANTASK, values, where,
                        new String[]{task.taskId});
                if (count > 0) {
                    Log.d(TAG, "updateOnePlantaskState, update success, plantask taskId: " + task.taskId);
                } else {
                    Log.e(TAG, "updateOnePlantaskState, update failed, plantask taskId: " + task.taskId);
                }
            } else {
                Uri uri = getContentResolver().insert(URI_PLANTASK, values);
                if (uri != null) {
                    Log.d(TAG, "updateOnePlantaskState insert success, plantask taskId : " + task.taskId + ", uri: " + uri);
                } else {
                    Log.e(TAG, "updateOnePlantaskState insert failed, plantask taskId : " + task.taskId);
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "updateOnePlantaskState, 2 ex: " + ex);
        }
    }

    private void removeOnePlantask(Intent intent) {
        PlanTask task = intent.getParcelableExtra(EXTRA_PLANTASK);
        if (task == null) {
            Log.e(TAG, "removeOnePlantask, task is null");
            return;
        }

        try {
            String where = AbsolutePlanContract.PlanTask.TASK_ID + " = ?";
            int count = getContentResolver().delete(URI_PLANTASK, where, new String[]{task.taskId});
            if (count > 0) {
                Log.d(TAG, "removeOnePlantask success, plantask taskId : " + task.taskId);
            } else {
                Log.e(TAG, "removeOnePlantask failed, plantask taskId : " + task.taskId);
            }
        } catch (Exception ex) {
            Log.e(TAG, "removeOnePlantask, ex: " + ex);
        }
    }
}

