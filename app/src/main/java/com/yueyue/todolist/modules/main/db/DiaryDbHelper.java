package com.yueyue.todolist.modules.main.db;

import com.yueyue.todolist.component.PLog;
import com.yueyue.todolist.modules.main.domain.DiaryEntity;

import org.litepal.crud.DataSupport;
import org.litepal.exceptions.DataSupportException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * author : yueyue on 2018/3/8 09:30
 * desc   :
 */
@Deprecated
public class DiaryDbHelper {
    private static final String TAG = DiaryDbHelper.class.getSimpleName();


    public static DiaryDbHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    private static final class SingletonHolder {
        private static final DiaryDbHelper sInstance = new DiaryDbHelper();
    }

    /**
     * 分页查询
     *
     * @param offset     偏移量
     * @param conditions
     */
    public List<DiaryEntity> pageSearch(int offset, String... conditions)
            throws DataSupportException {
        return DataSupport.where(conditions)
                .limit(10).offset(offset).find(DiaryEntity.class);
    }

    public List<DiaryEntity> findAll() throws DataSupportException {
        return DataSupport.findAll(DiaryEntity.class);
    }


    public boolean addPlantask(DiaryEntity task)
            throws DataSupportException {
        if (task == null) {
            PLog.e(TAG, "addOnePlantask: task is null");
            return false;
        }
        return task.saveOrUpdate("taskId=?", task.getTaskId() + "");
    }

    public int updatePlantask(DiaryEntity task)
            throws InterruptedException, ExecutionException, TimeoutException, DataSupportException {
        if (task == null) {
            PLog.e(TAG, "updateOnePlantaskState: task is null");
            return 0;
        }

        return  task.updateAll("taskId=?", task.getTaskId() + "");
    }

    public int removePlantask(DiaryEntity task)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (task == null) {
            PLog.e(TAG, "removePlantask, task is null");
            return 0;
        }
        return DataSupport.deleteAll(DiaryEntity.class, "taskId=?", task.getTaskId() + "");
    }


}
