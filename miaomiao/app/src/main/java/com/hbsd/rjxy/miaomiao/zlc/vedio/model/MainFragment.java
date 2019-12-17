package com.hbsd.rjxy.miaomiao.zlc.vedio.model;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.EventInfo;
import com.hbsd.rjxy.miaomiao.entity.Multi_info;

import com.hbsd.rjxy.miaomiao.entity.Subscription_record;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.utils.ScrollCalculatorHelper;
import com.hbsd.rjxy.miaomiao.zlc.vedio.presenter.IVideoPreseter;
import com.hbsd.rjxy.miaomiao.zlc.vedio.presenter.MeAdapter;
import com.hbsd.rjxy.miaomiao.zlc.vedio.presenter.MeGSYVideoPlayer;
import com.hbsd.rjxy.miaomiao.zlc.vedio.view.IMainFragmentView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.hbsd.rjxy.miaomiao.utils.Constant.INIT_SUBSCRIBE_VIDEO_LIST;
import static com.hbsd.rjxy.miaomiao.utils.Constant.INIT_VIDEO_URL;
import static com.hbsd.rjxy.miaomiao.utils.Constant.LOGIN_SP_NAME;
import static com.hbsd.rjxy.miaomiao.utils.Constant.RECOMMEND_PAGE_DEFAULT;
import static com.hbsd.rjxy.miaomiao.utils.Constant.SUBSCRIBE_PAGE_DEFAULT;
import static com.hbsd.rjxy.miaomiao.utils.Constant.URL_GET_SUBSCRIPTION_LIST;


public class MainFragment extends Fragment implements IMainFragmentView , IVideoPreseter , View.OnClickListener {


    @BindView(R.id.tv_subscribed)
    TextView tv_subscribed;
    @BindView(R.id.tv_recommend)
    TextView tv_recommend;
    @BindView(R.id.rl_video)
    SmartRefreshLayout rlVideo;

    private RecyclerView recyclerView;
    private MeAdapter adapter;
    private List<Multi_info> videoList;         //这里只显示视频，所以是videoList
    private List<Subscription_record> subscriptionRecords;
    ScrollCalculatorHelper scrollCalculatorHelper ;
    private int playTop;
    private int playBottom;
    //这个是否第一次播放的初始化放到了initData方法中
    boolean firstOpenVideo = true;
    private View view;
    private int contentType = 1;//TODO ：1推荐，0订阅
    private String uid;
    private boolean nomoreVideo = false;

    Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view != null){

        }else{
            view = initFragment(inflater,container);

            ButterKnife.bind(this,view);
            tv_recommend.setOnClickListener(this::onClick);
            tv_subscribed.setOnClickListener(this::onClick);

            //获得recyclerView
            recyclerView = view.findViewById(R.id.rv_main);

            initPlayPosition(getContext());
            initRefreshLayout();



            //自定义播放帮助类
            scrollCalculatorHelper = new ScrollCalculatorHelper(R.id.videoPlayer, playTop, playBottom);
            //初始化数据
            videoList = initData(videoList);
            /*
                TODO    重构，使用okhttp
                    (1)判断是否是登录的
             */

            SharedPreferences sp = getContext().getSharedPreferences(LOGIN_SP_NAME, Context.MODE_PRIVATE);
            uid = sp.getString("uid","1");
            if("1".equals(uid)){
                //没登录，不去请求订阅列表
                //现在写的是登录的情况
                askforSubscriptionList();
//                askforRecommend();
            }else {
                //没登录这样
            askforRecommend();
            }






        }
        return view;
    }

    private void initRefreshLayout() {
        rlVideo.setHeaderHeight(190);
        rlVideo.setHeaderMaxDragRate(2.0f);
        rlVideo.setHeaderTriggerRate(0.7f);
        rlVideo.setEnableLoadMore(false);
        rlVideo.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                SharedPreferences sp = getContext().getSharedPreferences(LOGIN_SP_NAME, Context.MODE_PRIVATE);
                String uid = sp.getString("uid","1");
                askforRefreshVideoList(uid);

            }
        });


    }

    private void askforSubscriptionList() {
        Map<String,String> map = new HashMap<>();
        map.put("uid","1");
        OkHttpUtils.getInstance().postForm(URL_GET_SUBSCRIPTION_LIST, map, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                Log.e("网络连接失败","连接失败");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                subscriptionRecords =
                        gson.fromJson(response.body().string(),new TypeToken<List<Subscription_record>>(){}.getType());
                Log.e("askforSubscriptionList",""+subscriptionRecords.toString());
//                Log.e("askforSubscriptionList",""+response.body().string());

                //不知道什么问题
                if(subscriptionRecords.get(0).getSrid() == 0){
                    subscriptionRecords = null;
                }

                askforRecommend();
            }
        });


    }

    private void askforRefreshVideoList(@Nullable String uid){
        if(!nomoreVideo){
            int page;
            String url;
            if(contentType == 1){
                RECOMMEND_PAGE_DEFAULT = 1;
                page = RECOMMEND_PAGE_DEFAULT;
                url = INIT_VIDEO_URL;
            }else{
                SUBSCRIBE_PAGE_DEFAULT = 1;
                page = SUBSCRIBE_PAGE_DEFAULT;
                url = INIT_SUBSCRIBE_VIDEO_LIST;
            }

            JSONObject jo = new JSONObject();
            try {
                jo.put("page",page);
                if(contentType == 0){
                    jo.put("uid",uid);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            OkHttpUtils.getInstance().postJson(url, jo.toString(), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    //这个要替换当前的这些视频
                    List<Multi_info> videoList = gson.fromJson(response.body().string(),new TypeToken<List<Multi_info>>(){}.getType());
                    Log.e("refresh",""+videoList.toString());
                    EventInfo<String,String,Multi_info> videoEvent = new EventInfo<>();
                    videoEvent.setContentList(videoList);
                    videoEvent.setContentString("refreshVideoList");
                    EventBus.getDefault().post(videoEvent);
                }
            });
        }else{
            //已经没有更多的没看过的视频了
        }
    }



    /*
        TODO   :    根据contenType改变请求内容，这里是预加载的加载请求url有变化
     */
    private void askforRecommend() {
        JSONObject jo = new JSONObject();
        if(contentType == 1 && RECOMMEND_PAGE_DEFAULT != 1){
            RECOMMEND_PAGE_DEFAULT += 1;
        }
        if(contentType == 0){
            SUBSCRIBE_PAGE_DEFAULT += 1;
        }

        try {
            if(contentType == 1){
                jo.put("page",RECOMMEND_PAGE_DEFAULT);
            }else{
                jo.put("page",SUBSCRIBE_PAGE_DEFAULT);
                SharedPreferences sp = getContext().getSharedPreferences(LOGIN_SP_NAME, Context.MODE_PRIVATE);
                String uid = sp.getString("uid","1");
                jo.put("uid",uid);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url;
        if(contentType == 1){
            url = INIT_VIDEO_URL;
        }else{
            url = INIT_SUBSCRIBE_VIDEO_LIST ;
        }
        OkHttpUtils.getInstance().postJson(url, jo.toString(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                List<Multi_info> videoList = gson.fromJson(response.body().string(),new TypeToken<List<Multi_info>>(){}.getType());
                EventInfo<String,String,Multi_info> videoEvent = new EventInfo<>();
                if(contentType == 1 && RECOMMEND_PAGE_DEFAULT == 1){
                    RECOMMEND_PAGE_DEFAULT += 1;
                }
                if(!videoList.isEmpty()){
                    //如果拿到了视频数据，则放到eventinfo的list中去
                    videoEvent.setContentList(videoList);
                    videoEvent.setContentString("videoInit");
                    EventBus.getDefault().post(videoEvent);
                    Log.e("videoList",""+videoList.toString());
                }else{
                    //没有拿到设置eventinfo为无效
                    Log.e("videolist","null");
                    videoEvent.setAvailable(false);
                    Map<String,String> map = new HashMap<>();
                    map.put("status","complete");
                    videoEvent.setContentMap(map);
                    videoEvent.setContentString("videoInit");
                    EventBus.getDefault().post(videoEvent);
                }
            }
        });

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
    public RecyclerView initRecyclerView() {
        //使用自定义的LinearLayoutManager子类
        MeLayoutManager linearLayoutManager = new MeLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //缺一不可
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        //设置适配器
        recyclerView.setAdapter(adapter);

        //监听recyclerView的滑动，实现自动播放
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
                //如果下一个视频的封面已经被隐藏了，那么改变封面为显示状态
                if(recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE ){
                    scrollCalculatorHelper.isNowPlaying(view);
                }
            }
        });
        return recyclerView;
    }

    @Override
    public MeAdapter initAdapter() {
        adapter = new MeAdapter(R.layout.rv_mian_detail_layout,videoList,getContext(),subscriptionRecords);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                askforRecommend();
                Log.e("预加载了","...");
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


    //订阅事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getVideoList(EventInfo<String,String,Multi_info> videoEvent){
        if(videoEvent.getContentString().equals("videoInit")){
            if(videoEvent.isAvailable()){
                /**
                 *    如果是有效的
                 *      如果当前视频列表已经存在数据
                 */
                if(this.videoList.size() != 0){
                    for(Multi_info multi_info : videoEvent.getContentList()){
                        this.videoList.add(multi_info);
                    }
                }else{
                    this.videoList = videoEvent.getContentList();
                }
                //如果的适配器还未初始化，初始化适配器和recyclerView
                if(adapter == null){
                    //初始化Adapter
                    adapter = initAdapter();
                    recyclerView = initRecyclerView();
                }
                //初始化RecyclerView

                //通知adapter内容改变
                adapter.loadMoreComplete();
            }else{
                //如果返回失败，那么根据map判断状态
                if("complete".equals(videoEvent.getContentMap().get("status"))){
                    if(contentType == 1){
                        RECOMMEND_PAGE_DEFAULT -= 1;
                        Toast.makeText(getContext(),"看完了",Toast.LENGTH_SHORT).show();
//                        nomoreVideo = true;
                    }else{
                        SUBSCRIBE_PAGE_DEFAULT -= 1;
                        Toast.makeText(getContext(),"看完了",Toast.LENGTH_SHORT).show();
                    }

                }


            }
        }else if("refreshVideoList".equals(videoEvent.getContentString())){
            //如果返回是空
            if(videoEvent.getContentList().isEmpty()){
                rlVideo.finishRefresh(500);
                Toast.makeText(getContext(),"没有更多视频了",Toast.LENGTH_SHORT).show();
                return;
            }
            GSYVideoManager.releaseAllVideos();
            this.videoList.clear();
            for(Multi_info multi_info : videoEvent.getContentList()){
                this.videoList.add(multi_info);
            }
            adapter.notifyItemChanged(0);
            firstOpenVideo = true;
            rlVideo.finishRefresh(1000);
            recyclerView.swapAdapter(adapter,true);
            recyclerView.setAdapter(adapter);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            layoutManager.onItemsChanged(recyclerView);
            MeGSYVideoPlayer meGSYVideoPlayer = recyclerView.getLayoutManager().getChildAt(0).findViewById(R.id.videoPlayer);
            meGSYVideoPlayer.startAfterPrepared();



        }else if("subscribeInit".equals(videoEvent.getContentString())){
            if(videoEvent.getContentList().isEmpty()){
                return;
            }
            GSYVideoManager.releaseAllVideos();
            this.videoList.clear();
            for(Multi_info multi_info : videoEvent.getContentList()){
                this.videoList.add(multi_info);
            }
            adapter.notifyItemChanged(0);
            firstOpenVideo = true;
            recyclerView.swapAdapter(adapter,true);
            recyclerView.setAdapter(adapter);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            layoutManager.onItemsChanged(recyclerView);
            MeGSYVideoPlayer meGSYVideoPlayer = recyclerView.getLayoutManager().getChildAt(0).findViewById(R.id.videoPlayer);
            meGSYVideoPlayer.startAfterPrepared();
        }

        Log.e("loadMoreComplete","loadMoreComplete");
        //没有移除粘性事件
    }




    @Override
    public List<Multi_info> initData(List<Multi_info> videoList) {
        //测试
        if(videoList == null){
            videoList = new ArrayList<>();
        }


        if(firstOpenVideo != false){
            firstOpenVideo = true;
        }else{

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
                if(contentType != 0){
                    //当前不是订阅内容，判断是否登录
                    SharedPreferences sp = getContext().getSharedPreferences(LOGIN_SP_NAME, Context.MODE_PRIVATE);
                    String uid = sp.getString("uid","default");
                    if("default".equals(uid)){
                        startActivity(new Intent(getContext(),PleaseLoginActivity.class));
                    }else{
                        //如果没有登录
                        //以后下面的内容放到else分支里，1改成unregist
                        setTextViewColor(tv_subscribed,tv_recommend);
                        //请求订阅的视频内容...
                        RECOMMEND_PAGE_DEFAULT -= 1;
                        adapter.loadMoreComplete();
                        contentType = 0;
                        askforSubscribedVideoList(uid);
                        //修改当前contentType为 0

                    }

                }else{
                    //已经是这个页面了
                }





                break;

            case R.id.tv_recommend:
                /**点击推荐的事件
                 *
                 * 1.切换推荐为选中状态
                 * 2.请求服务器重新获得视频数据
                 *
                 */

                if(contentType != 1){
                    setTextViewColor(tv_recommend,tv_subscribed);
                    contentType = 1;
                    //请求推荐视频的数据
//                    if(SUBSCRIBE_PAGE_DEFAULT != 1){
//                        SUBSCRIBE_PAGE_DEFAULT -= 1;
//                    }
                    adapter.loadMoreComplete();
                    askforRefreshVideoList(null);
                }else{
                    //表示已经是推荐内容了
                }



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
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }


    /*
        TODO:下拉刷新和预加载都应该判断当前的contentType=0/1，根据这个来请求更多的视频
     */
    private void askforSubscribedVideoList(String uid){
        JSONObject jo = new JSONObject();
        try {
            jo.put("page",SUBSCRIBE_PAGE_DEFAULT);
            jo.put("uid",uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.getInstance().postJson(INIT_SUBSCRIBE_VIDEO_LIST, jo.toString(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //这个要替换当前的这些视频
                List<Multi_info> videoList = gson.fromJson(response.body().string(),new TypeToken<List<Multi_info>>(){}.getType());
                EventInfo<String,String,Multi_info> videoEvent = new EventInfo<>();
                videoEvent.setContentList(videoList);
                videoEvent.setContentString("subscribeInit");
                EventBus.getDefault().post(videoEvent);
            }
        });
    }




}
