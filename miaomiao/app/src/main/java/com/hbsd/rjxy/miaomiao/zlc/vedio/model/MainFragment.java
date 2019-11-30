package com.hbsd.rjxy.miaomiao.zlc.vedio.model;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.Multi_infor;

import com.hbsd.rjxy.miaomiao.utils.ScrollCalculatorHelper;
import com.hbsd.rjxy.miaomiao.zlc.vedio.presenter.IVideoPreseter;
import com.hbsd.rjxy.miaomiao.zlc.vedio.presenter.MeAdapter;
import com.hbsd.rjxy.miaomiao.zlc.vedio.view.IMainFragmentView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainFragment extends Fragment implements IMainFragmentView , IVideoPreseter , View.OnClickListener {


    @BindView(R.id.tv_subscribed)
    TextView tv_subscribed;
    @BindView(R.id.tv_recommend)
    TextView tv_recommend;

    private RecyclerView recyclerView;
    private MeAdapter adapter;
    private List<Multi_infor> videoList;         //这里只显示视频，所以是videoList
    ScrollCalculatorHelper scrollCalculatorHelper ;
    private int playTop;
    private int playBottom;
    //这个是否第一次播放的初始化放到了initData方法中
    boolean firstOpenVideo = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initFragment(inflater,container);

        ButterKnife.bind(this,view);
        tv_recommend.setOnClickListener(this::onClick);
        tv_subscribed.setOnClickListener(this::onClick);
        //注册订阅者     ！！重复注册
//        EventBus.getDefault().register(this);

        //获得recyclerView
        recyclerView = view.findViewById(R.id.rv_main);

        initPlayPosition(getContext());



        //自定义播放帮助类
        scrollCalculatorHelper = new ScrollCalculatorHelper(R.id.videoPlayer, playTop, playBottom);


        //初始化数据
        videoList = initData(videoList);
        //初始化Adapter
        adapter = initAdapter(adapter);
        //初始化RecyclerView
        recyclerView = initRecyclerView(recyclerView);

        return view;
    }


    /**
     * 初始化fragment的视图
     * @param inflater
     * @param container
     * @return
     */
    @Override
    public View initFragment(LayoutInflater inflater,ViewGroup container) {
        View newView = inflater.inflate(
                R.layout.main_fragment,
                container,
                false
        );
        return newView;
    }

    @Override
    public void initPlayPosition(Context context) {
        //限定范围为屏幕一半的上下偏移180
        playTop = CommonUtil.getScreenHeight(context) / 2 - CommonUtil.dip2px(context, 300);
        playBottom = CommonUtil.getScreenHeight(context) / 2 + CommonUtil.dip2px(context, 300);


    }

    @Override
    public RecyclerView initRecyclerView(RecyclerView recyclerView) {
        //使用自定义的LinearLayoutManager子类
        MeLayoutManager linearLayoutManager = new MeLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //缺一不可
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        //
        recyclerView.setAdapter(adapter);



        //监听recyclerView的滑动
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                scrollCalculatorHelper.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //这是滑动自动播放的代码
                scrollCalculatorHelper.onScroll(recyclerView, firstVisibleItem, lastVisibleItem, lastVisibleItem - firstVisibleItem);
                //第一次进入程序自动播放
                if(firstOpenVideo){
                    scrollCalculatorHelper.onScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_IDLE);
                    firstOpenVideo = false;
                }
            }
        });


        /**
         * 视频移出去后改变封面图的visible状态
         */
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                if(recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE){
                    scrollCalculatorHelper.isNowPlaying(view);
                }
            }
        });
        return recyclerView;
    }

    @Override
    public MeAdapter initAdapter(MeAdapter adapter) {
        adapter = new MeAdapter(R.layout.rv_mian_detail_layout,videoList,getContext());
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //这里是预加载请求
                videoList = initData(videoList);
            }
        },recyclerView);

        //倒数第一个的时候预加载
        adapter.setPreLoadNumber(1);
        adapter.setEnableLoadMore(true);
        //载入动画
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        /**-----------------------点击事件------------------------**/
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //position是序号 0开始
                Toast.makeText(getContext(),"onItemClick:"+position,Toast.LENGTH_SHORT).show();
            }
        });

        //长摁点击事件

        //item中子控件的点击事件

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //判断子控件的id
                //if(view.getId() == targetId){ do something}

            }
        });
        return adapter;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getVideoList(List<Multi_infor> videoList){
        this.videoList = videoList;
        //通知adapter内容改变
        adapter.loadMoreComplete();
    }




    @Override
    public List<Multi_infor> initData(List<Multi_infor> videoList) {
        //测试
        if(videoList == null){
            videoList = new ArrayList<>();
        }
        Multi_infor muti_infor1 = new Multi_infor();
        muti_infor1.setMiPath("http://q1kb2gx86.bkt.clouddn.com/c519a750bbc3d317f9315cdef7db1c72.mp4");
        muti_infor1.setMiCover("http://q1kb2gx86.bkt.clouddn.com/cover1.png");
        videoList.add(muti_infor1);

        Multi_infor muti_infor2 = new Multi_infor();
        muti_infor2.setMiPath("http://q1kb2gx86.bkt.clouddn.com/50d10301117b759c793b4f07ccfbdeca.mp4");
        muti_infor2.setMiCover("http://q1kb2gx86.bkt.clouddn.com/20191127203009.jpg");
        videoList.add(muti_infor2);

        Multi_infor muti_infor3 = new Multi_infor();
        muti_infor3.setMiPath("http://q1kb2gx86.bkt.clouddn.com/765454469a1c2c869749ee68d6a0f8ca.mp4");
        muti_infor3.setMiCover("http://q1kb2gx86.bkt.clouddn.com/20191127203048.jpg");
        videoList.add(muti_infor3);

        Multi_infor muti_infor4 = new Multi_infor();
        muti_infor4.setMiPath("http://q1kb2gx86.bkt.clouddn.com/b48a416cd3047220952f3c0ed320a085.mp4");
        muti_infor4.setMiCover("http://q1kb2gx86.bkt.clouddn.com/20191127203116.jpg");
        videoList.add(muti_infor4);




        if(firstOpenVideo != false){
            firstOpenVideo = true;
        }else{
            adapter.loadMoreComplete();
        }
        return videoList;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_subscribed:
                /**点击订阅的事件
                 *
                 * 1.切换订阅为选中状态
                 * 2.请求服务器重新获得视频数据
                 *
                 */
                setTextViewColor(tv_subscribed,tv_recommend);

                //如果没有登录
                startActivity(new Intent(getContext(),PleaseLoginActivity.class));




                break;

            case R.id.tv_recommend:
                /**点击推荐的事件
                 *
                 * 1.切换推荐为选中状态
                 * 2.请求服务器重新获得视频数据
                 *
                 */
                setTextViewColor(tv_recommend,tv_subscribed);


                break;


        }

    }

    @Override
    public void setTextViewColor(TextView selectedView,TextView unselectedView) {
        selectedView.setTextColor(getResources().getColor(R.color.mainuptextselected));
        unselectedView.setTextColor(getResources().getColor(R.color.mainuptextnormal));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        //切换fragment的时候停止播放，释放所有播放视频
        GSYVideoManager.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        //每次回来也要自动播放
        firstOpenVideo = true;
        GSYVideoManager.onResume();
        super.onResume();
    }
}
