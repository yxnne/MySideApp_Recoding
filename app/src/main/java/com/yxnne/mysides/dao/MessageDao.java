package com.yxnne.mysides.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yxnne.mysides.util.log.LogGenerator;

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;

/**
 * about message
 * Created by Administrator on 2016/11/2.
 */

public class MessageDAO {

    Context context;

    public MessageDAO(Context context) {
        this.context = context;
        DBHelper dbHelper = null;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            //建一个message表
            String sql = "create table if not exists message"
                    + "(_id integer primary key," + "messageFrom text,"
                    + "messageTo text," + "messageType text,"
                    + "messageBody text)";
            dbHelper = new DBHelper(context);
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(sql);
        } catch (Exception e) {
            LogGenerator.getInstance().printError(e);
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
            if (dbHelper != null) {
                dbHelper.close();
            }
        }

    }

    public int insert(Message message) {
        int id = -1;
        DBHelper dbHelper = null;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            ContentValues values = new ContentValues();
            values.put("messageFrom", message.getFrom());
            values.put("messageTo", message.getTo());
            values.put("messageType", message.getType().toString());
            values.put("messageBody", message.getBody());

            dbHelper = new DBHelper(context);
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sqLiteDatabase.insert("message", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
            if (dbHelper != null) {
                dbHelper.close();
            }
        }
        return id;
    }

    public ArrayList<Message> query() {
        ArrayList<Message> list = null;
        return list;
    }
}
