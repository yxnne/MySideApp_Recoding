package com.yxnne.mysides.adapter;

import android.content.Context;
import com.yxnne.mysides.R;
import java.util.List;
import xiaojinzi.base.android.adapter.recyclerView.CommonRecyclerViewAdapter;
import xiaojinzi.base.android.adapter.recyclerView.CommonRecyclerViewHolder;

/**
 *
 * Created by Administrator on 2016/10/30.
 */

public class FaceEmojiAdapter extends CommonRecyclerViewAdapter<Integer> {
    /**
     * 构造函数
     *
     * @param context 上下文
     * @param data    显示的数据
     */
    public FaceEmojiAdapter(Context context, List<Integer> data) {
        super(context, data);
    }

    @Override
    public void convert(CommonRecyclerViewHolder h, Integer entity, int position) {
        h.setImage(R.id.iv_face, entity);
    }

    @Override
    public int getLayoutViewId(int viewType) {
        return R.layout.recyclerview_face_item;
    }


}
