package com.yxnne.mysides.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.yxnne.mysides.R;
import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.adapter.FaceEmojiAdapter;
import com.yxnne.mysides.adapter.FaceVpAdapter;
import com.yxnne.mysides.biz.PrivateChatBizAsyncTask;
import com.yxnne.mysides.entity.PrivateChatEntity;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.log.EmojiFaceDataUtil;
import com.yxnne.mysides.util.log.FaceUtil;
import com.yxnne.mysides.util.log.LogGenerator;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class PrivateChatActivity extends AppCompatActivity {
    private String friendUser;
    private TextView tvFriendName,tvBack,tvMore;
    private EditText etMsgBody;
    private Button btnSend,btnFace;
    private ScrollView scrollView;
    private LinearLayout linearLayout,linearLayoutMore,mDotsLayout,linearLayoutFace;
    //private GridView gvFace;
   // private FaceGridViewAdapter faceAdapter;
    ///三个线表示用recyclerView的删掉注释
    private RecyclerView rvFace;
    private FaceEmojiAdapter faceRvAdapter;
    //ViewPager做一个qq表情
    private ViewPager vpQQFace;
    //表情列表
    private List<String> staticFacesList;
    //表情图标每页6列4行
    private int columns = 6;
    private int rows = 4;
    //添加到ViewPager的View 实际是GridView
    private List<View> views = new ArrayList<>();
    private StaggeredGridLayoutManager layoutManager = null;
    private BroadcastReceiver showPrivateMsgReceiver;
    private Handler handler = new Handler();
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_private_chat_viewpager);
            getData();
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            findViews();
            addListener();
            showPrivateMsgReceiver = new ShowPrivateMsgReceiver();
            this.registerReceiver(showPrivateMsgReceiver,
                    new IntentFilter(Const.ACTION_SEND_PRIVATE_CHAT_MSG));
            ///configRecyclerView();
            configViewPager();
        } catch (Exception e) {
            LogGenerator.getInstance().printError(e);
        }
    }

    private void getData() {
        friendUser = this.getIntent().getStringExtra(Const.STATUS_KEY);
    }
    private void findViews() {

        tvFriendName = (TextView) findViewById(R.id.tv_private_chat_friendName);
        tvBack = (TextView) findViewById(R.id.tv_private_chat_back);
        tvMore = (TextView) findViewById(R.id.tv_private_chat_more);
        etMsgBody = (EditText) findViewById(R.id.et_private_body);
        btnSend = (Button) findViewById(R.id.btn_private_chat_send);
        btnFace = (Button) findViewById(R.id.btn_private_chat_face);
        scrollView = (ScrollView) findViewById(R.id.scrollView1);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutChatContent);
        linearLayoutMore = (LinearLayout) findViewById(R.id.linearLayoutMore);
        linearLayoutFace = (LinearLayout) findViewById(R.id.linearLayoutface);
//        gvFace = (GridView) findViewById(R.id.gridView_face);
        ///rvFace = (RecyclerView) findViewById(R.id.rv_face);
        //gv adapter
//        faceAdapter = new FaceGridViewAdapter(this);
//        gvFace.setAdapter(faceAdapter);
        //viewPager
        vpQQFace = (ViewPager) findViewById(R.id.vp_face);
        //indicator
        mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
        //显示名字
        if (friendUser.contains("@")) {
            String friendUsername = friendUser.substring(0,
                    friendUser.indexOf("@"));
            tvFriendName.setText(friendUsername);
        }
    }

    /**
     * 配置RecyclerView 显示表情
     * 废弃了
     */
    private void configRecyclerView() {
        //创建适配器
        faceRvAdapter = new FaceEmojiAdapter(this, EmojiFaceDataUtil.data);
        //创建布局管理器
        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        rvFace.setLayoutManager(layoutManager);

        //创建条目间隔
        RecyclerView.ItemDecoration itemDecoration = new EmojiSpaceItemDecoration();
        rvFace.addItemDecoration(itemDecoration);

        //设置适配器
        rvFace.setAdapter(faceRvAdapter);
    }
    private void configViewPager() {
        staticFacesList = FaceUtil.initStaticFaces(this);
        int pagesize = FaceUtil.getPagerCount(staticFacesList.size(),columns,rows);
        // 获取页数
        for (int i = 0; i <pagesize; i++) {
            views.add(FaceUtil.viewPagerItem(this, i, staticFacesList,columns, rows, etMsgBody));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(16, 16);
            mDotsLayout.addView(dotsItem(i), params);
        }
        FaceVpAdapter mVpAdapter = new FaceVpAdapter(views);
        vpQQFace.setAdapter(mVpAdapter);
        mDotsLayout.getChildAt(0).setSelected(true);
        vpQQFace.addOnPageChangeListener(new PageChange());
    }
    /**
     * 表情页切换时，底部小圆点
     * @param position 位置
     * @return ivdot
     */
    private ImageView dotsItem(int position) {
        View layout = inflater.inflate(R.layout.dot_image, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
    }
    /**
     * 表情页改变时，dots效果也要跟着改变
     * */
    class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
                mDotsLayout.getChildAt(i).setSelected(false);
            }
            mDotsLayout.getChildAt(arg0).setSelected(true);
        }
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        this.unregisterReceiver(showPrivateMsgReceiver);
    }

    private void addListener() {
        //btn Send:
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String body = etMsgBody.getText().toString();
                    etMsgBody.getText().clear();

                    PrivateChatBizAsyncTask privateChatTask = new PrivateChatBizAsyncTask();
                    privateChatTask.execute(friendUser, body);
                } catch (Exception e) {
                    LogGenerator.getInstance().printError(e);
                }

            }
        });
        //tv back to back
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.finish
                finish();
                //2.保存对话 //TODO
            }
        });
        //tv more 控制：更多面板显示
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏软键盘
                hideSoftInputView();
                if (linearLayoutMore.getVisibility() == View.GONE) {
                    if(linearLayoutFace.getVisibility() == View.GONE){
                        linearLayoutMore.setVisibility(View.VISIBLE);
                    }else{
                        linearLayoutFace.setVisibility(View.GONE);
                    }

                } else if (linearLayoutMore.getVisibility() == View.VISIBLE) {
                    linearLayoutMore.setVisibility(View.GONE);
                }
            }
        });
        //btnFace
        btnFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInputView();
                linearLayoutMore.setVisibility(View.GONE);
                if(linearLayoutFace.getVisibility() == View.GONE){
                    linearLayoutFace.setVisibility(View.VISIBLE);
                }else{
                    linearLayoutFace.setVisibility(View.GONE);
                }
            }
        });
        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayoutFace.getVisibility() == View.VISIBLE){
                    linearLayoutFace.setVisibility(View.GONE);
                }
                if(linearLayoutMore.getVisibility() == View.VISIBLE){
                    linearLayoutMore.setVisibility(View.GONE);
                }
            }
        });

    }
    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * taskBiz 发送消息成功以后 发送广播过来 这里收 用于更新界面
     */
    class ShowPrivateMsgReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                LogGenerator.getInstance().printprintLog("ShowPrivateMsgReceiver", "show a msg");
                Vector<Message> vector = PrivateChatEntity.map.get(friendUser);
                for (Message message : vector) {
                    String from = message.getFrom();
                    String body = message.getBody();
                    //LogGenerator.getInstance().printprintLog("ShowPrivateMsgReceiver", from + ":" + body);
                    View view ;
                    // 判断是我说的还是好友说的
                    if (YApplication.currentUser.equals(from)) {//我说的
                        view = View.inflate(context, R.layout.right_chat_item, null);
                    } else {
                        // 好友说的用left.xml来显示
                        view = View.inflate(context, R.layout.left_chat_item, null);
                    }

                    // 把right.xml或left.xml放到linearLayout中
                    linearLayout.addView(view);
                    // 给textview设置显示的聊天内容
                    TextView tvText = (TextView) view
                            .findViewById(R.id.tv_text);
                    tvText.setText(body);

                    handler.post(new Runnable() {//这个执行的时间靠后些
                        @Override
                        public void run() {
                            moveUp();
                        }
                    });
                    // 从vector移出message
                   // moveUp();
                    vector.remove(message);
                }
            } catch (Exception e) {
                LogGenerator.getInstance().printError(e);
            }
        }

        /**
         * 将scrollView向上移动
         */
        private void moveUp() {
            // 向上移
            int scrollViewHeight = scrollView.getHeight();
            int linearLayoutHeight = linearLayout.getHeight();
            LogGenerator.getInstance()
                    .printprintLog("向上移", "scrollViewHeight=" + scrollViewHeight
                    + ",linearLayoutHeight=" + linearLayoutHeight);
            if (linearLayoutHeight > scrollViewHeight) {
                int y = linearLayoutHeight - scrollViewHeight;
                scrollView.scrollTo(0, y);
            }
        }
    }

    /**
     * recycler view use
     */
    class EmojiSpaceItemDecoration extends RecyclerView.ItemDecoration {
        /**
         * 水平方向的间距
         */
        private int hSpace;

        EmojiSpaceItemDecoration() {
            hSpace = 60;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        if (parent.getChildAdapterPosition(view) > 0) {
            outRect.left = hSpace;
            outRect.right = hSpace;
//        }
        }
    }
}
