package com.yxnne.mysides.util.chat;

/**
 * MAP ,Audio ,Picture 相关的通用工具类，face不在这里面
 * Created by Administrator on 2016/10/31.
 */
public class ChatCommenUtil {
    // 定义五种数据类型
    public final static int TYPE_TEXT = 1;
    //public final static int TYPE_FACE = 2;
    public final static int TYPE_IMAGE = 3;
    public final static int TYPE_AUDIO = 4;
    public final static int TYPE_MAP = 5;
    // 定义五种数据类型的tag
    final static String TAG_TEXT = "[!--TEXT]";
    //public final static String TAG_FACE = "<!--FACE>";
    final static String TAG_AUDIO = "[!--AUDIO]";
    final static String TAG_MAP = "[!--MAP]";
    final static String TAG_IMAGE = "[!--IMAGE]";
    final static String TAG_END = "[__end_]";

    /**
     * 判断收到的body的类型
     * @param body msg
     * @return type_text
     */
    public static int getType(String body) {

        if (body.startsWith(TAG_IMAGE)) {
            return TYPE_IMAGE;
        } else if (body.startsWith(TAG_AUDIO)) {
            return TYPE_AUDIO;
        } else if (body.startsWith(TAG_MAP)) {
            return TYPE_MAP;
        }
        return TYPE_TEXT;
    }

}
