package com.yueyue.todolist.modules.edit.domain;

/**
 * author : yueyue on 2018/3/14 12:35
 * desc   :
 */

public class ImageEntity {
    public int start;
    public int end;
    public String imageName;
    public String imageFlag;

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