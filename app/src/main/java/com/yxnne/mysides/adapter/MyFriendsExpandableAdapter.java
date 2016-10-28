package com.yxnne.mysides.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxnne.mysides.R;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;

import java.util.ArrayList;
import java.util.Collection;

/**
 * MyFriendsExpandableAdapter for MyfriendActivity
 * Created by Administrator on 2016/10/28.
 */

public class MyFriendsExpandableAdapter extends BaseExpandableListAdapter {
    Context context;
    // RosterGroup代表的是好友分组
    ArrayList<RosterGroup> listGroup;

    public MyFriendsExpandableAdapter(Context context,ArrayList<RosterGroup> listGroup){
        this.context = context;
        if (listGroup == null) {
            this.listGroup = new ArrayList<RosterGroup>();
        } else {
            this.listGroup = listGroup;
        }
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }
    //这个是每个组下面的子列个数
    @Override
    public int getChildrenCount(int groupPosition) {
        RosterGroup rosterGroup = listGroup.get(groupPosition);
        int childrenCount = rosterGroup.getEntryCount();
        return childrenCount;
    }


    @Override
    public Object getGroup(int groupPosition) {
       return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        RosterGroup rosterGroup = listGroup.get(groupPosition);
        // 得到分组下的所有好友
        // RosterEntry代表是一个好友
        ArrayList<RosterEntry> listFriend = new ArrayList(rosterGroup.getEntries());
        RosterEntry rosterEntry = listFriend.get(childPosition);
        return rosterEntry;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if (convertView == null) {
            groupViewHolder = new GroupViewHolder();
            convertView = View.inflate(context, R.layout.explv_myfriend_group, null);
            groupViewHolder.ivIcon = (ImageView) convertView
                    .findViewById(R.id.iv_my_friend_group_icon);
            groupViewHolder.tvGroupName = (TextView) convertView
                    .findViewById(R.id.tv_my_friend_group_groupName);

            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        // 设置数据
        RosterGroup rosterGroup = (RosterGroup) getGroup(groupPosition);
        String groupName = rosterGroup.getName();
        groupViewHolder.tvGroupName.setText(groupName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = View
                    .inflate(context, R.layout.explv_myfriend_frinend, null);
            childViewHolder.ivIcon = (ImageView) convertView
                    .findViewById(R.id.iv_my_friend_friend_icon);
            childViewHolder.tvFriendName = (TextView) convertView
                    .findViewById(R.id.tv_my_friend_friend_friendName);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        RosterEntry rosterEntry = (RosterEntry) getChild(groupPosition,
                childPosition);
        String friendName = rosterEntry.getName();
        childViewHolder.tvFriendName.setText(friendName);
        return convertView;
    }

    class GroupViewHolder {
        ImageView ivIcon;
        TextView tvGroupName;
    }

    class ChildViewHolder {
        ImageView ivIcon;
        TextView tvFriendName;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
