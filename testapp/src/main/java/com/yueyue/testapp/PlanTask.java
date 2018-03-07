package com.yueyue.testapp;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * author : yueyue on 2018/3/3 10:17
 * desc   :
 */

public class PlanTask extends DataSupport {
    public final long finalRemark = System.currentTimeMillis();//PlanTask独特标志,用于删除跟更新时候用
    @Column(unique = true)
    public String fildStr;
    public String priFildStr;
    private String name;

    public PlanTask() {
    }

    public PlanTask(String fildStr, String priFildStr, int state) {
        this.fildStr = fildStr;
        this.priFildStr = priFildStr;
        this.state = state;
    }

    @Column(defaultValue = "0")
    public int state;


    @Override
    public String toString() {
        return "PlanTask{" +
                "finalRemark=" + finalRemark +
                ", fildStr='" + fildStr + '\'' +
                ", priFildStr='" + priFildStr + '\'' +
                ", name='" + name + '\'' +
                ", state=" + state +
                '}';
    }
}
