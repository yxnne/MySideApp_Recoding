package com.yxnne.mysides.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * Created by Administrator on 2016/11/2.
 */

public class DBHelper  extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, "chat.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

}
