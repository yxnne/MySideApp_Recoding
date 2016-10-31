package com.yxnne.mysides.util.log;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yxnne.mysides.R;
import com.yxnne.mysides.adapter.FaceGvAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 和表情相关的工具类
 * Created by Administrator on 2016/10/31.
 */

public class FaceUtil {
    /**
     * 初始化表情列表staticFacesList
     * @param context 上下文
     * @return list
     */
    public static  List<String> initStaticFaces(Context context) {
        List<String> facesList = null;
        try {
            facesList = new ArrayList<String>();
            //assetsmanager 的list方法返回 目录下所有文件名
            String[] faces = context.getAssets().list("p");
            //将Assets中的表情名称转为字符串一一添加进staticFacesList
            for (int i = 0; i < faces.length; i++) {
                facesList.add(faces[i]);
            }
            //去掉删除图片
            facesList.remove("del.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return facesList;
    }
    /**
     * 根据表情数量以及GridView设置的行数和列数计算Pager数量
     * @return
     */
    public  static int getPagerCount(int listsize,int columns,int rows ) {
        return listsize % (columns * rows - 1) == 0 ? listsize / (columns * rows - 1): listsize / (columns * rows - 1) + 1;
    }


    /**
     * 得到viewpager里面放的gridView
     * @param context 上下文
     * @param position Viewpager的位置 需要一系列计算
     * @param staticFacesList 表情名
     * @param columns 列
     * @param rows 行
     * @param editText 相关联的edittext
     * @return View gridview，放在viewpager里面
     */
    public static View viewPagerItem(final Context context,
                                     int position,
                                     List<String> staticFacesList,
                                     int columns, int rows,
                                     final EditText editText) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.face_gridview_viewpager, null);//表情布局
        GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);

         // 每一页末尾都有一个删除图标，
         // 所以每一页的实际表情columns *　rows　- 1;
         // 空出最后一个位置给删除图标

        List<String> subList = new ArrayList<String>();
        subList.addAll(staticFacesList
                .subList(position * (columns * rows - 1),
                        (columns * rows - 1) * (position + 1) > staticFacesList
                                .size() ? staticFacesList.size() : (columns
                                * rows - 1)
                                * (position + 1)));

         //末尾添加删除图标
        subList.add("_del.png");
        FaceGvAdapter mGvAdapter = new FaceGvAdapter(subList, context);
        gridview.setAdapter(mGvAdapter);
        gridview.setNumColumns(columns);
        // 单击表情执行的操作
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                try {
                    String png = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
                    if (!png.contains("_del")) {// 如果不是删除图标
                        FaceUtil.insert(editText,FaceUtil.getFace(context,png));
                    } else {
                        FaceUtil.delete(editText);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return gridview;
    }

    /**
     * 向输入框里添加表情
     * */
    private static void insert(EditText input,CharSequence text) {
        int iCursorStart = Selection.getSelectionStart((input.getText()));
        int iCursorEnd = Selection.getSelectionEnd((input.getText()));
        if (iCursorStart != iCursorEnd) {
            ((Editable) input.getText()).replace(iCursorStart, iCursorEnd, "");
        }
        int iCursor = Selection.getSelectionEnd((input.getText()));
        ((Editable) input.getText()).insert(iCursor, text);
    }
    /**
     * 删除图标执行事件
     * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，
     * 所以必需一次性删除掉tempText，才能将图片删除
     * */
    private static void delete(EditText input) {
        if (input.getText().length() != 0) {
            int iCursorEnd = Selection.getSelectionEnd(input.getText());
            int iCursorStart = Selection.getSelectionStart(input.getText());
            if (iCursorEnd > 0) {
                if (iCursorEnd == iCursorStart) {
                    if (isDeletePng(input,iCursorEnd)) {
                        String st = "[p/_000.png]";
                        ((Editable) input.getText()).delete(
                                iCursorEnd - st.length(), iCursorEnd);
                    } else {
                        ((Editable) input.getText()).delete(iCursorEnd - 1,
                                iCursorEnd);
                    }
                } else {
                    ((Editable) input.getText()).delete(iCursorStart,
                            iCursorEnd);
                }
            }
        }
    }
    /**
     * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：
     * 则讲删除整个tempText
     * **/
    private static boolean  isDeletePng(EditText input,int cursor) {
        String st = "[p/_000.png]";
        String content = input.getText().toString().substring(0, cursor);
        if (content.length() >= st.length()) {
            String checkStr = content.substring(content.length() - st.length(),content.length());
            String regex = "\\[[^\\]]+\\]";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(checkStr);
            return m.matches();
        }
        return false;
    }

    /**
     *
     * @param mContext 上下文
     * @param png 图片
     * @return
     * SpannableStringBuilder
     * 首先SpannableString、SpannableStringBuilder基本上与String差不多，
     * 也是用来存储字符串，但它们俩的特殊就在于有一个SetSpan（）函数，
     * 能给这些存储的String添加各种格式或者称样式（Span），
     * 将原来的String以不同的样式显示出来，
     * 比如在原来String上加下划线、加背景色、改变字体颜色、用图片把指定的文字给替换掉，等等。
     * 所以，总而言之，SpannableString、SpannableStringBuilder与String一样，
     * 首先也是传字符串，但SpannableString、SpannableStringBuilder可以对这些字符串添加额外的样式信息，
     * 但String则不行。
     */
    private static SpannableStringBuilder getFace(Context mContext, String png) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        try {
            /**
             * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
             * 所以这里对这个tempText值做特殊处理
             * 格式：#[face/png/f_static_000.png]#，以方便判斷當前圖片是哪一個
             * */
            String tempText = "[" + png + "]";
            sb.append(tempText);
            sb.setSpan(
                    new ImageSpan(mContext, BitmapFactory
                            .decodeStream(mContext.getAssets().open(png))), sb.length()
                            - tempText.length(), sb.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }
}
