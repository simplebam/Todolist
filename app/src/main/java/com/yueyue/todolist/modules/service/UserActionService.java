package com.yueyue.todolist.modules.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.yueyue.todolist.R;
import com.yueyue.todolist.common.utils.entity.PlanTask;
import com.yueyue.todolist.component.CachePlanTaskStore;
import com.yueyue.todolist.component.PLog;
import com.yueyue.todolist.modules.service.db.AbsolutePlanContract;

import org.litepal.crud.DataSupport;

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


    public static void startService(Context context, String action, PlanTask planTask) {
        Intent intent = new Intent(context, UserActionService.class);
        intent.setAction(action);
        if (planTask != null) {
            intent.putExtra(UserActionService.EXTRA_PLANTASK, planTask);
        }
        context.startService(intent);
    }

    public static void startService(Context context, String action) {
        startService(context, action, null);
    }

//    //绑定服务
//    public void bindService(Context context,ServiceConnection connection) {
//        Intent intent = new Intent(context, UserActionService.class);
//        context.bindService(intent, connection, Service.BIND_AUTO_CREATE);
//    }
//

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();

        switch (action) {
            case INTENT_ACTION_CACHEPLANTASK:
                getCachePlanTask();
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

    private void getCachePlanTask() {
        List<PlanTask> planTaskList = DataSupport.findAll(PlanTask.class);
        CachePlanTaskStore.getInstance().setCachePlanTaskList(planTaskList, true);
    }

    private void addOnePlantask(Intent intent) {
        PlanTask task = intent.getParcelableExtra(EXTRA_PLANTASK);
        if (task == null) {
            Log.e(TAG, "addOnePlantask: task is null");
            return;
        }

        boolean b = false;
        try {
            b = task.saveOrUpdate("taskId=?", task.getTaskId() + "");
        } catch (Exception e) {
            b = false;
            Log.e(TAG, "addOnePlantask:" + e.toString());
            e.printStackTrace();
        }

        int msgId = b ? R.string.save_success : R.string.save_error;
        ToastUtils.showShort(getString(msgId));

    }

    private void updateOnePlantaskState(Intent intent) {
        PlanTask task = intent.getParcelableExtra(EXTRA_PLANTASK);
        if (task == null) {
            Log.e(TAG, "updateOnePlantaskState: task is null");
            return;
        }

        int updateCount = 0;
        try {
            updateCount = task.updateAll("taskId=?", task.getTaskId() + "");
        } catch (Exception e) {
            updateCount = 0;
            PLog.e(TAG, "updateOnePlantaskState:" + e.toString());
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder(getString(R.string.data_update))
                .append(updateCount)
                .append(getString(R.string.branch));
        ToastUtils.showShort(sb.toString());

    }

    private void removeOnePlantask(Intent intent) {
        PlanTask task = intent.getParcelableExtra(EXTRA_PLANTASK);
        if (task == null) {
            Log.e(TAG, "removeOnePlantask, task is null");
            return;
        }

        int deleteCount = 0;
        try {
            deleteCount = DataSupport.deleteAll(PlanTask.class, "taskId=?", task.getTaskId() + "");
        } catch (Exception e) {
            deleteCount = 0;
            PLog.e(TAG, "removeOnePlantask:" + e.toString());
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder(getString(R.string.data_delete))
                .append(deleteCount)
                .append(getString(R.string.branch));
        ToastUtils.showShort(sb.toString());

    }
}

