package com.yueyue.todolist.common.utils.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.yueyue.todolist.base.BaseEntity;

import org.litepal.annotation.Column;

/**
 * author : yueyue on 2018/3/3 10:17
 * desc   :
 */

public class PlanTask extends BaseEntity implements Parcelable {
    //private public 以及 public final 类型,LitePal都会存储

    //设置unique = true时候,taskId已经存在数据库的话,再次存储该taskId(调用save()方法)
    @Column(unique = true)
    private long taskId = System.currentTimeMillis();
    public int priority;
    public String title = "";
    public String describe = "";
    public long time;
    @Column(defaultValue = "0")
    public int state; //0: normal; 1: finished

    public PlanTask() {
    }

    private PlanTask(Parcel in) {
        taskId = in.readLong();
        priority = in.readInt();
        title = in.readString();
        describe = in.readString();
        time = in.readLong();
        state = in.readInt();
    }

    public static final Creator<PlanTask> CREATOR = new Creator<PlanTask>() {
        @Override
        public PlanTask createFromParcel(Parcel in) {
            return new PlanTask(in);
        }

        @Override
        public PlanTask[] newArray(int size) {
            return new PlanTask[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(taskId);
        dest.writeInt(priority);
        dest.writeString(title);
        dest.writeString(describe);
        dest.writeLong(time);
        dest.writeInt(state);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlanTask planTask = (PlanTask) o;

        if (taskId != planTask.taskId) return false;
        if (priority != planTask.priority) return false;
        if (time != planTask.time) return false;
        if (state != planTask.state) return false;
        if (!title.equals(planTask.title)) return false;
        return describe.equals(planTask.describe);
    }

    @Override
    public int hashCode() {
        int result = (int) (taskId ^ (taskId >>> 32));
        result = 31 * result + priority;
        result = 31 * result + title.hashCode();
        result = 31 * result + describe.hashCode();
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + state;
        return result;
    }

    public long getTaskId() {
        return taskId;
    }


}
