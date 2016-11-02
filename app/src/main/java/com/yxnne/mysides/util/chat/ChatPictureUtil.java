package com.yxnne.mysides.util.chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.util.CommenTools;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.log.LogGenerator;

import org.jivesoftware.smack.packet.Message;

import java.util.UUID;

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

    /**
     * 收到图片时,保存到sdcard,
     * body修改了，当中存放的是图片path
     * @param message 消息
     */
    public static void saveImageToSdcard(Message message) {
        int start = TAG_NETWORK_IMAGE.length();
        String body = message.getBody();
        int end = body.length() - TAG_END.length();
        // 图的数据
        body = body.substring(start, end);

        // 保存到sdcard
        byte[] imageData = Base64.decode(body, Base64.DEFAULT);
        //生成随机名字
        String fileName = UUID.randomUUID().toString();
        fileName = fileName + ".png";
        CommenTools.writeToSdcard(YApplication.instance, Const.IMAGE_PATH, fileName,
                imageData);

        // 修改body
        body = TAG_SDCARD_IMAGE + Const.IMAGE_PATH + "/" + fileName + TAG_END;
        message.setBody(body);
    }

    /**
     * 根据地址 取得图片
     * @param body 消息体，实际是地址
     * @return 位图
     */
    public static Bitmap getSdcardImage(String body) {
        int start = TAG_SDCARD_IMAGE.length();
        int end = body.length() - TAG_END.length();
        // body是path
        body = body.substring(start, end);
        byte[] fileData = CommenTools.readSdcard(body);
        Bitmap bitmap = BitmapFactory.decodeByteArray(fileData, 0,
                fileData.length);

        return bitmap;
    }

}
