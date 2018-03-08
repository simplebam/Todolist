package com.yueyue.todolist.modules.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * author : yueyue on 2018/3/3 10:23
 * desc   :
 */
@Deprecated
public class UserActionService extends IntentService {
    public static final String TAG = UserActionService.class.getSimpleName();

    public static final String INTENT_ACTION_CACHEPLANTASK = "com.yueyue.todolist.service.action.CACHE_PLANTASK";
    public static final String INTENT_ACTION_ADD_ONE_PLANTASK = "com.yueyue.todolist.service.action.ADD_ONE_PLANTASK";
    public static final String INTENT_ACTION_UPDATE_ONE_PLANTASK_STATE = "com.yueyue.todolist.service.action.UPDATE_ONE_PLANTASK_STATE";
    public static final String INTENT_ACTION_REMOVE_ONE_PLANTASK = "com.yueyue.todolist.service.action.REMOVE_ONE_PLANTASK";
    public static final String INTENT_ACTION_GET_TODAY_PLANTASK = "com.yueyue.todolist.service.action.GET_TODAY_PLANTASK";

    public static final String EXTRA_PLANTASK = "extra_plantask";


    public UserActionService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


//    public static void startService(Context context, String action, DiaryEntity diaryEntity) {
//        Intent intent = new Intent(context, UserActionService.class);
//        intent.setAction(action);
//        if (diaryEntity != null) {
//            intent.putExtra(UserActionService.EXTRA_PLANTASK, diaryEntity);
//        }
//        context.startService(intent);
//    }
//
//    public static void startService(Context context, String action) {
//        startService(context, action, null);
//    }
//
////    //绑定服务
////    public void bindService(Context context,ServiceConnection connection) {
////        Intent intent = new Intent(context, UserActionService.class);
////        context.bindService(intent, connection, Service.BIND_AUTO_CREATE);
////    }
////
//
//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//        if (intent == null || intent.getAction() == null)
//            return;
//
//        String action = intent.getAction();
//
//        switch (action) {
//            case INTENT_ACTION_CACHEPLANTASK:
//                getCachePlanTask();
//                break;
//            case INTENT_ACTION_ADD_ONE_PLANTASK:
//                addOnePlantask(intent);
//                break;
//            case INTENT_ACTION_UPDATE_ONE_PLANTASK_STATE:
//                updateOnePlantaskState(intent);
//                break;
//            case INTENT_ACTION_REMOVE_ONE_PLANTASK:
//                removeOnePlantask(intent);
//                break;
//            case INTENT_ACTION_GET_TODAY_PLANTASK:
//                getCachePlanTaskByTime(Calendar.getInstance(Locale.CHINA).getTimeInMillis(),0);
//                break;
//            default:
//                break;
//        }
//
//    }
//
//    private List<DiaryEntity> getCachePlanTaskByTime(long time, int offset) {
//        List<DiaryEntity> diaryEntityList = DataSupport.where("time=?", time + "")
//                .limit(10)
//                .offset(offset)
//                .find(DiaryEntity.class);
//        return diaryEntityList;
//    }
//
//    private void getCachePlanTask() {
//        List<DiaryEntity> diaryEntityList = DataSupport.findAll(DiaryEntity.class);
//        DiaryStore.getInstance().setCachePlanTaskList(diaryEntityList, true);
//    }
//
//    private void addOnePlantask(Intent intent) {
//        DiaryEntity task = intent.getParcelableExtra(EXTRA_PLANTASK);
//        if (task == null) {
//            Log.e(TAG, "addOnePlantask: task is null");
//            return;
//        }
//
//        boolean b = false;
//        try {
//            b = task.saveOrUpdate("taskId=?", task.getTaskId() + "");
//        } catch (Exception e) {
//            b = false;
//            Log.e(TAG, "addOnePlantask:" + e.toString());
//            e.printStackTrace();
//        }
//
//        int msgId = b ? R.string.save_success : R.string.save_error;
//        ToastUtils.showShort(getString(msgId));
//
//    }
//
//    private void updateOnePlantaskState(Intent intent) {
//        DiaryEntity task = intent.getParcelableExtra(EXTRA_PLANTASK);
//        if (task == null) {
//            Log.e(TAG, "updateOnePlantaskState: task is null");
//            return;
//        }
//
//        int updateCount = 0;
//        try {
//            updateCount = task.updateAll("taskId=?", task.getTaskId() + "");
//        } catch (Exception e) {
//            updateCount = 0;
//            PLog.e(TAG, "updateOnePlantaskState:" + e.toString());
//            e.printStackTrace();
//        }
//
//        StringBuilder sb = new StringBuilder(getString(R.string.data_update))
//                .append(updateCount)
//                .append(getString(R.string.branch));
//        ToastUtils.showShort(sb.toString());
//
//    }
//
//    private void removeOnePlantask(Intent intent) {
//        DiaryEntity task = intent.getParcelableExtra(EXTRA_PLANTASK);
//        if (task == null) {
//            Log.e(TAG, "removeOnePlantask, task is null");
//            return;
//        }
//
//        int deleteCount = 0;
//        try {
//            deleteCount = DataSupport.deleteAll(DiaryEntity.class, "taskId=?", task.getTaskId() + "");
//        } catch (Exception e) {
//            deleteCount = 0;
//            PLog.e(TAG, "removeOnePlantask:" + e.toString());
//            e.printStackTrace();
//        }
//
//        StringBuilder sb = new StringBuilder(getString(R.string.data_delete))
//                .append(deleteCount)
//                .append(getString(R.string.branch));
//        ToastUtils.showShort(sb.toString());
//
//    }
}

