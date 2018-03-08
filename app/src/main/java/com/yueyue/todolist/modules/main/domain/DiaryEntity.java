package com.yueyue.todolist.modules.main.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.yueyue.todolist.base.BaseEntity;

import org.litepal.annotation.Column;

/**
 * author : yueyue on 2018/3/3 10:17
 * desc   :
 */

public class DiaryEntity extends BaseEntity implements Parcelable {
    //private public 以及 public final 类型,LitePal都会存储

    //设置unique = true时候,taskId已经存在数据库的话,再次存储该taskId(调用save()方法)
    @Column(unique = true)
    private long taskId = System.currentTimeMillis();
    public int priority;
    public String title = "";
    public String content = "";
    //这个为了获取年月日时分
    public long time;
    //这个为了根据年月日查询数据库
    public String calendar;
    @Column(defaultValue = "0")
    public int state; //0: normal; 1: finished

    public DiaryEntity() {
    }

    private DiaryEntity(Parcel in) {
        taskId = in.readLong();
        priority = in.readInt();
        title = in.readString();
        content = in.readString();
        time = in.readLong();
        state = in.readInt();
    }

    public static final Creator<DiaryEntity> CREATOR = new Creator<DiaryEntity>() {
        @Override
        public DiaryEntity createFromParcel(Parcel in) {
            return new DiaryEntity(in);
        }

        @Override
        public DiaryEntity[] newArray(int size) {
            return new DiaryEntity[size];
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
        dest.writeString(content);
        dest.writeLong(time);
        dest.writeInt(state);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiaryEntity diaryEntity = (DiaryEntity) o;

        if (taskId != diaryEntity.taskId) return false;
        if (priority != diaryEntity.priority) return false;
        if (time != diaryEntity.time) return false;
        if (state != diaryEntity.state) return false;
        if (!title.equals(diaryEntity.title)) return false;
        return content.equals(diaryEntity.content);
    }

    @Override
    public int hashCode() {
        int result = (int) (taskId ^ (taskId >>> 32));
        result = 31 * result + priority;
        result = 31 * result + title.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + state;
        return result;
    }

    public long getTaskId() {
        return taskId;
    }


}
