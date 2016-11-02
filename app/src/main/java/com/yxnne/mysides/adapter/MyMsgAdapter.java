package com.yxnne.mysides.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxnne.mysides.R;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/28.
 */

public class MyMsgAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Object> listMsg;

    /**
     *  构造方法
     * @param context 上下文
     * @param listMsg 消息集合
     */
    public MyMsgAdapter(Context context , ArrayList<Object> listMsg){
        this.context = context;
        if (listMsg == null) {
            this.listMsg = new ArrayList<Object>();
        } else {
            this.listMsg = listMsg;
        }
    }
    @Override
    public int getCount() {
        return listMsg.size();
    }

    @Override
    public Object getItem(int position) {
        return listMsg.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * 重新显示listView
     * @param newData 消息集合
     */
    public void updateData(ArrayList<Object> newData) {
        this.listMsg = newData;
        this.notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.my_msg_item, null);
            viewHolder.llAddFriendResult = (LinearLayout) convertView
                    .findViewById(R.id.ll_my_msg_item_addFriendResult);
            viewHolder.llChatContent = (LinearLayout) convertView
                    .findViewById(R.id.ll_my_msg_item_chat);
            viewHolder.tvAddFreindResult = (TextView) convertView
                    .findViewById(R.id.tv_my_msg_item_addFriendResult);
            viewHolder.tvChatContent = (TextView) convertView
                    .findViewById(R.id.tv_my_msg_item_chatContent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 数据类型都是string,显示的布局不一样
        // class Data{int typeId,Object content}

        Object data = listMsg.get(position);
        // 判断类型，不同类型不同处理
        if (data instanceof Presence) {
            // 不同意
            Presence presence = (Presence) data;
            String user = presence.getFrom();
            viewHolder.llAddFriendResult.setVisibility(View.VISIBLE);
            viewHolder.tvAddFreindResult.setText(user + "不同意");
        }
        if (data instanceof String) {
            viewHolder.llAddFriendResult.setVisibility(View.VISIBLE);
            // 好友同意
            viewHolder.tvAddFreindResult.setText(String.valueOf(data));

        }

        if (data instanceof Message) {
            // 聊天内容
            String str = ((Message) data).getFrom()+" say :";
            viewHolder.tvChatContent.setText(str);
        }

        return convertView;
    }


    /**
     *  view Holder
     */
    class ViewHolder {
        LinearLayout llAddFriendResult, llChatContent;
        TextView tvAddFreindResult, tvChatContent;
        Button btnChat;
    }

}
