package com.yueyue.todolist.modules.main.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.blankj.utilcode.util.TimeUtils;
import com.yueyue.todolist.base.BaseEntity;

import org.litepal.annotation.Column;

import java.util.UUID;

/**
 * author : yueyue on 2018/3/13 16:38
 * desc   :
 */

public class NoteEntity extends BaseEntity implements Parcelable {
    public int id;
    //设置unique = true时候,taskId已经存在数据库的话,再次存储该taskId(调用save()方法)不会更新
    @Column(unique = true)
    public String noteId = UUID.randomUUID().toString();
    public long createdTime = TimeUtils.getNowMills();
    public long modifiedTime;
    public String noteContent = "";
    @Column(defaultValue = "0")
    public int isPrivacy;    //    1是私密便签  0不是
    @Column(defaultValue = "0")
    public int inRecycleBin; //    1是废纸篓中便签，0不是



    public NoteEntity() {
    }

    protected NoteEntity(Parcel in) {
        id = in.readInt();
        noteId = in.readString();
        createdTime = in.readLong();
        modifiedTime = in.readLong();
        noteContent = in.readString();
        isPrivacy = in.readInt();
        inRecycleBin = in.readInt();
    }

    public static final Creator<NoteEntity> CREATOR = new Creator<NoteEntity>() {
        @Override
        public NoteEntity createFromParcel(Parcel in) {
            return new NoteEntity(in);
        }

        @Override
        public NoteEntity[] newArray(int size) {
            return new NoteEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(noteId);
        dest.writeLong(createdTime);
        dest.writeLong(modifiedTime);
        dest.writeString(noteContent);
        dest.writeInt(isPrivacy);
        dest.writeInt(inRecycleBin);
    }

    @Override
    public String toString() {
        return NoteEntity.class.getSimpleName() + "{" +
                "id=" + id +
                ", noteId='" + noteId + '\'' +
                ", createdTime=" + createdTime +
                ", modifiedTime=" + modifiedTime +
                ", noteContent='" + noteContent + '\'' +
                ", isPrivacy=" + isPrivacy +
                ", inRecycleBin=" + inRecycleBin +
                '}';
    }
}
