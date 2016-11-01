package com.yxnne.mysides.util.chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.yxnne.mysides.util.log.LogGenerator;

/**
 * 和图片相关的工具类
 * Created by Administrator on 2016/10/31.
 */

public class ChatPictureUtil extends  ChatCommenUtil{

    /**
     * 发图片时候调用
     *
     * @param imageData picture数据流
     * @return String
     */
    public static String addImageTag(byte[] imageData) {
        StringBuilder builder = new StringBuilder();
        try {
            // 把byte[]转成字符串
            // 1,用java的方式,把字符串转成byte,有编码问题
            // 如果好友用的是iphone,出问题
            // String body=new String(imageData);
            // 好友收到的是body
            // byte[] data=body.getBytes();
            // 2,需要一种编码方式Base64是通用的，c,java,iphone都支持
            String string = Base64.encodeToString(imageData, Base64.DEFAULT);

            builder.append(TAG_IMAGE);
            builder.append(string);
            builder.append(TAG_END);
        } catch (Exception e) {
            LogGenerator.getInstance().printError(e);
        }

        return builder.toString();
    }

    /**
     * 收到图片时
     * @param body msg
     * @return bitmap
     */
    public static Bitmap getImage(String body) {
        Bitmap bitmap ;
        int start = TAG_IMAGE.length();
        int end = body.length() - TAG_END.length();
        body = body.substring(start, end);

        // 用base64把字符串转成byte[]
        byte[] imageData = Base64.decode(body, Base64.DEFAULT);
        // byte[]转成bitmap
        bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        return bitmap;
    }
}
