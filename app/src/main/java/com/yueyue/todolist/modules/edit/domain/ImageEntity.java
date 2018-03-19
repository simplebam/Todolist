package com.yueyue.todolist.modules.edit.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author : yueyue on 2018/3/14 12:35
 * desc   :
 */

public class ImageEntity implements Parcelable{
    public int start;
    public int end;
    public String imageName;
    public String imageFlag;

    public ImageEntity() {
    }

    public ImageEntity(int start, int end, String imageName, String imageFlag) {
        this.start = start;
        this.end = end;
        this.imageName = imageName;
        this.imageFlag = imageFlag;
    }

    protected ImageEntity(Parcel in) {
        start = in.readInt();
        end = in.readInt();
        imageName = in.readString();
        imageFlag = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(start);
        dest.writeInt(end);
        dest.writeString(imageName);
        dest.writeString(imageFlag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageEntity> CREATOR = new Creator<ImageEntity>() {
        @Override
        public ImageEntity createFromParcel(Parcel in) {
            return new ImageEntity(in);
        }

        @Override
        public ImageEntity[] newArray(int size) {
            return new ImageEntity[size];
        }
    };

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getImageFlag() {
        return imageFlag;
    }

    public void setImageFlag(String imageFlag) {
        this.imageFlag = imageFlag;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}