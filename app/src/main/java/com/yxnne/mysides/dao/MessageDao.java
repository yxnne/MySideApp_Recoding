package com.yxnne.mysides.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yxnne.mysides.util.chat.ChatCommenUtil;
import com.yxnne.mysides.util.log.LogGenerator;

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;

/**
 * about message
 * Created by Administrator on 2016/11/2.
 */

public class MessageDAO {
    public static final String INSERT_IMAGE_TYPE = "i_m_a_g_e";
    public static final String INSERT_AUDIO_TYPE = "a_u_d_i_o";
    public static final String INSERT_MAP_TYPE = "m_a_p";

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
            String msg = judgeMsg(message);
            values.put("messageBody", msg);

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

    /**
     * 判断类型 不同类型插入数据库的方式不同
     * @param message msg
     * @return str to insert
     */
    private String judgeMsg(Message message) {

        String strMsg = message.getBody();
        int type = ChatCommenUtil.getType(strMsg);
        switch (type){
            case ChatCommenUtil.TYPE_IMAGE:
                strMsg = INSERT_IMAGE_TYPE;
                break;
            case ChatCommenUtil.TYPE_MAP:
                strMsg = INSERT_MAP_TYPE;
                break;
            case ChatCommenUtil.TYPE_AUDIO:
                strMsg = INSERT_AUDIO_TYPE;
                break;
        }
        return strMsg;
    }

    public ArrayList<Message> query(String friendUser) {
        ArrayList<Message> list = null;
        DBHelper dbHelper = null;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            dbHelper = new DBHelper(context);
            sqLiteDatabase = dbHelper.getWritableDatabase();
            String[] columns = { "messageFrom", "messageTo", "messageType",
                    "messageBody" };
            String selection = "messageTo=? or messageFrom like ?";
            String[] selectionArgs = { friendUser, friendUser+"%" };
            cursor = sqLiteDatabase.query("message", columns, selection,
                    selectionArgs, null, null, "_id asc");
            list = new ArrayList<Message>();
            while (cursor.moveToNext()) {
                String from = cursor.getString(cursor
                        .getColumnIndex("messageFrom"));
                String to = cursor
                        .getString(cursor.getColumnIndex("messageTo"));
                String type = cursor.getString(cursor
                        .getColumnIndex("messageType"));
                String body = cursor.getString(cursor
                        .getColumnIndex("messageBody"));

                Message message = new Message();
                message.setFrom(from);
                message.setTo(to);
                message.setBody(body);
                if ("chat".equals(type)) {
                    message.setType(Message.Type.chat);
                }
                list.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
            if (dbHelper != null) {
                dbHelper.close();
            }
        }
        return list;
    }
}
