package com.yxnne.mysides.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.util.log.LogGenerator;
import java.io.ByteArrayOutputStream;

/**
 * to use image loader
 * Created by Administrator on 2016/10/29.
 */

public class ImageLoaderUtil {
    private static ImageLoaderConfiguration imageLoaderConfiguration = null;

    /**
     * 准备ImageLoaderConfiguration
     * @param context 上下文
     * @return imageLoaderConfiguration
     */
    private static ImageLoaderConfiguration getConfig(Context context) {
        if (imageLoaderConfiguration == null) {
            imageLoaderConfiguration =
                    new ImageLoaderConfiguration.Builder(context)
                            .discCacheSize(1024 * 1024 * 50)// 设置硬盘缓存的最大大小
                            .discCacheFileCount(50) // 设置硬盘缓存的文件的最多个数
                            .build();
        }
        return imageLoaderConfiguration;
    }

    /**
     *从网络下载图片
     * @param context 上下文
     * @param imageUrl 图片的URL
     * @param imageView 用于显示的imageView
     */
    public static void displayNetworkImage(Context context, String imageUrl,
                                           ImageView imageView) {
        ImageLoader imageLoader=ImageLoader.getInstance();
        imageLoader.init(getConfig(context));
        imageLoader.displayImage(imageUrl, imageView, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                LogGenerator.getInstance().printprintLog("imageLoader.displayImage","onLoadingStarted");

            }

            @Override
            public void onLoadingFailed(String imageUri,
                                        View view,FailReason failReason) {
                LogGenerator.getInstance().printprintLog("imageLoader.displayImage","onLoadingFailed");
            }

            @Override
            public void onLoadingComplete(String imageUri,
                                          View view, Bitmap loadedImage) {
                LogGenerator.getInstance().printprintLog("imageLoader.displayImage","onLoadingComplete");
                //下载好了 需要将它保存
                try {
                    // 保存到sdcard
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    loadedImage.compress(Bitmap.CompressFormat.PNG, 100,byteArrayOutputStream);

                    byte[] imageData = byteArrayOutputStream
                            .toByteArray();
                    String imageName = imageUri.substring(imageUri.lastIndexOf("/") + 1);
                    CommenTools.writeToSdcard(YApplication.instance,Const.IMAGE_PATH, imageName, imageData);
                } catch (Exception e) {
                    LogGenerator.getInstance().printError(e);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                LogGenerator.getInstance().printprintLog("imageLoader.displayImage","onLoadingCancelled");
            }
        });
    }

    /**
     * 从sdcard中显示图片，Imageloader
     *如果imageUrl是以http://开始的，从网上下载图片；如果imageUrl是以file:开始的,从sdcard上读图片
     * @param context 上下文
     * @param imageUrl 图片url
     * @param imageView 用于显示图片的View
     */
    public static void displaySdcardImage(Context context, String imageUrl,
                        	ImageView imageView) {
        LogGenerator.getInstance().printprintLog("imageLoader","read from sdcard");
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(getConfig(context));
        //告诉imageloader，从sdcard上读图片
        imageUrl="file://"+imageUrl;
        imageLoader.displayImage(imageUrl, imageView);

    }


}
