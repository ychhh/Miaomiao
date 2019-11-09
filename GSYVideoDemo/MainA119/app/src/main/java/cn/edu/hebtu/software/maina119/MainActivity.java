package cn.edu.hebtu.software.maina119;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.transition.Explode;
import com.chad.library.adapter.base.BaseQuickAdapter;

import com.shuyu.gsyvideoplayer.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.hebtu.software.maina119.util.ScrollCalculatorHelper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_main)
    RecyclerView recyclerView;

    private MeAdapter adapter;
    private List<MeVideo> videoList;

    ScrollCalculatorHelper scrollCalculatorHelper ;
    boolean mFull = false;
    //这个是否第一次播放的初始化放到了initData方法中
    boolean firstOpenVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //透明状态栏          
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        // 设置一个exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定视图控件
        ButterKnife.bind(this);


        //限定范围为屏幕一半的上下偏移180
        int playTop = CommonUtil.getScreenHeight(this) / 2 - CommonUtil.dip2px(this, 300);
        int playBottom = CommonUtil.getScreenHeight(this) / 2 + CommonUtil.dip2px(this, 300);



        //自定播放帮助类
        scrollCalculatorHelper = new ScrollCalculatorHelper(R.id.videoPlayer, playTop, playBottom);

        //初始化数据
        initData();
        //初始化Adapter
        initAdapter();
        //初始化RecyclerView
        initRecyclerView();

    }


    private void initRecyclerView() {
        //
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        //使用自定义的LinearLayoutManager子类
        MeLayoutManager linearLayoutManager = new MeLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //缺一不可
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        //
        recyclerView.setAdapter(adapter);
        //监听
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
//                if (!mFull) {


                scrollCalculatorHelper.onScroll(recyclerView, firstVisibleItem, lastVisibleItem, lastVisibleItem - firstVisibleItem);
                //第一次进入程序自动播放
                if(firstOpenVideo){
                    scrollCalculatorHelper.onScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_IDLE);
                    firstOpenVideo = false;
                }
            }
        });




    }

    private void initAdapter() {
        adapter = new MeAdapter(R.layout.rv_mian_detail_layout,videoList,MainActivity.this);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                MeVideo meVideo = new MeVideo();
                adapter.addData(meVideo);
                adapter.loadMoreComplete();
            }
        },recyclerView);

        //倒数第一个的时候预加载
        adapter.setPreLoadNumber(1);
        //载入动画
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        /**-----------------------点击事件------------------------**/
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //position是序号 0开始
                Toast.makeText(MainActivity.this,"onItemClick:"+position,Toast.LENGTH_SHORT).show();
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
    }


    private void initData() {
        firstOpenVideo = true;

        videoList = new ArrayList<>();
        MeVideo video1 = new MeVideo();
        MeVideo video2 = new MeVideo();
        videoList.add(video1);
        videoList.add(video2);
    }


    //旋转全屏设置
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了也不全屏
        if (newConfig.orientation != ActivityInfo.SCREEN_ORIENTATION_USER) {
            mFull = false;
        } else {
            mFull = false;
        }
    }

}
