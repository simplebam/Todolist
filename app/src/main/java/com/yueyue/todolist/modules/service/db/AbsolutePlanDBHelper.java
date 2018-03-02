package com.yueyue.todolist.modules.service.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * author : yueyue on 2018/3/3 10:26
 * desc   :
 */

public class AbsolutePlanDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "todolist.db";
    public static final int DB_VERSION = 1;
    public static final String DB_TABLE_PLANTASK = "plantask";

    public AbsolutePlanDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + DB_TABLE_PLANTASK + "("
                + AbsolutePlanContract.PlanTask._ID + " integer primary key autoincrement, "
                + AbsolutePlanContract.PlanTask.TASK_ID + " text not null , "
                + AbsolutePlanContract.PlanTask.TASK_TITLE + " text default '-1' , "
                + AbsolutePlanContract.PlanTask.TASK_DESCRIBE + " text , "
                + AbsolutePlanContract.PlanTask.TASK_PRIORITY + "  integer default -1 , "
                + AbsolutePlanContract.PlanTask.TASK_STATE + " integer default 0 , "
                + AbsolutePlanContract.PlanTask.TASK_TIME + " integer "
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}