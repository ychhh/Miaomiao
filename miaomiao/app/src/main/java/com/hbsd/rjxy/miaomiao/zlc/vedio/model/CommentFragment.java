package com.hbsd.rjxy.miaomiao.zlc.vedio.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.Comment;
import com.hbsd.rjxy.miaomiao.entity.EventInfo;
import com.hbsd.rjxy.miaomiao.entity.SortClass;
import com.hbsd.rjxy.miaomiao.utils.HideKeyBoard;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.zlc.vedio.presenter.CommentAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.hbsd.rjxy.miaomiao.utils.Constant.LOGIN_SP_NAME;
import static com.hbsd.rjxy.miaomiao.utils.Constant.PUBLISH_URL_COMMENT;
import static com.hbsd.rjxy.miaomiao.utils.Constant.URL_FINDCOMMENTPAGING;
import static com.hbsd.rjxy.miaomiao.utils.Constant.URL_GET_TIME;


/*
    TODO
        还是写成viewPager把，第一页是这只猫的信息，第二页是本视频的评论
        <-----评论fragment   机制---->
        前三楼是点赞数量的前三名
        之后是按时间（评论时间排序  新----->旧）
        <------布局----->
        左边头像，中上--->用户uname，中下----->内容，最下---->et，最右边是发送
        <-------评论et-------->
        ****et悬浮****
        点击把et浮起来，在输入法上面（查！技术攻关）
        *****表情评论*****，查！技术攻关）
        <------初始化------->
        查询是否存在本视频的评论，如果没有，修改布局中的tv为当前视频还没有人评论哦，快来当沙发
        <------下拉刷新，上拉加载------>
        上拉刷新获取第一页的评论
        下拉加载改变当前的page属性+1
        请求


 */



/*
    TODO    明天写下拉刷新和预加载
 */

public class CommentFragment extends Fragment {

    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    @BindView(R.id.et_comment_context)
    EditText etComment;
    @BindView(R.id.iv_publish_comment)
    ImageView ivPublish;
    @BindView(R.id.rl_comment)
    SmartRefreshLayout rlComment;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;


    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private int miid;
    private int currentPage = 1;    //当前页
    private String currentTime;

    private boolean isPublishing = false;

    Gson gson = new Gson();

    public CommentFragment(int miid) {
        this.miid = miid;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ivLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    /*
            TODO    发布评论：
         */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_fragment,null);

        ButterKnife.bind(this,view);


        initDate();



        initPublishButton();
        initCommentEditText();
        initRefreshLayout();
        initImageView();


        return view;


    }

    //获取服务器当前时间
    private void initDate() {
        OkHttpUtils.getInstance().get(URL_GET_TIME, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                currentTime = response.body().string();
                initData();
            }
        });

    }

    private void initImageView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.loading)
                    .apply(options)
                    .into(ivLoading);
        }
    }

    private void initRefreshLayout() {
        rlComment.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                /*
                   TODO     下拉刷新，将currentPage恢复为1,获取全部评论并且排序
                 */
                currentPage = 1;
//                OkHttpUtils.getInstance().postJson("", "", new Callback() {
//                    @Override
//                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//
//                    }
//                });



            }
        });


    }

    private void initData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("miid",miid);
            jsonObject.put("page",currentPage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(isPublishing){
            Toast.makeText(getContext(),"正在等待服务器返回数据，请稍后再刷新",Toast.LENGTH_SHORT).show();
        }else {
            OkHttpUtils.getInstance().postJson(URL_FINDCOMMENTPAGING, jsonObject.toString(), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    List<Comment> comments = gson.fromJson(response.body().string(), new TypeToken<List<Comment>>(){}.getType());
                    Collections.sort(comments,new SortClass());

                    EventInfo<String,String,Comment> eventInfo = new EventInfo<>();
                    eventInfo.setContentList(comments);
                    eventInfo.setContentString("initCommentData");
                    EventBus.getDefault().post(eventInfo);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveComments(EventInfo eventInfo){
        if(eventInfo.getContentString().equals("initCommentData")){

            commentList = eventInfo.getContentList();
            ivLoading.setVisibility(View.INVISIBLE);
            initAdapter();
            initRecyclerView();
        }else if("publishCommentFailed".equals(eventInfo.getContentString())){
            commentList.remove(0);
            commentAdapter.notifyDataSetChanged();
            //解锁
            isPublishing = false;
        }else if("publishCommentSuccess".equals(eventInfo.getContentString())){
            //我要修改时间和coid
            commentList.get(0).setCoid(Integer.parseInt(eventInfo.getContentMap().get("coid").toString()));
            commentList.get(0).setPublishTime(eventInfo.getContentMap().get("publishTime").toString());
            commentAdapter.notifyDataSetChanged();
            //解锁
            isPublishing = false;
        }

    }

    private void initCommentEditText() {
        etComment.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etComment.getText().toString().length()>50){
                    s.clear();
                    Toast.makeText(getContext(),"最多输入50个汉字哦~",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rvComment.setLayoutManager(layoutManager);
        rvComment.setAdapter(commentAdapter);

    }

    private void initPublishButton(){
        ivPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPublishing){
                    Log.e("miid",""+miid);

                /*
                    TODO    判断当前是否登录，没登录跳转登录页面，登陆了
                 */
                    SharedPreferences sp = getContext().getSharedPreferences(LOGIN_SP_NAME, Context.MODE_PRIVATE);
                    String uid = sp.getString("uid","1");
                    if("1".equals(uid)){
                        Log.e("没登录","跳转");
                        //这个一会放到下面
                        if(etComment.getText().toString().length() == 0){
                            Toast.makeText(getContext(),"输入不能为空",Toast.LENGTH_SHORT).show();
                        }else{
                            //发表评论
                            Comment comment = new Comment();
                            comment.setMiid(miid);
                            comment.setUid(Integer.parseInt(uid));
                            comment.setCocontent(etComment.getText().toString());
                            comment.setColike(0);
                            comment.setCostatus(0);

                            //时间应该放在服务器处理
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:MM:ss");
                            comment.setPublishTime(simpleDateFormat.format(System.currentTimeMillis()));
                            List<View> views = new ArrayList<>();
                            views.add(v);
                            HideKeyBoard.hideSoftKeyboard(getContext(),views);
                            Log.e("发送成功",""+comment.toString());
                            commentList.add(0,comment);
                            etComment.setText("");
                            etComment.clearFocus();
                            commentAdapter.notifyDataSetChanged();

                            isPublishing = true;
                            //发送
                            OkHttpUtils.getInstance().postJson(PUBLISH_URL_COMMENT, gson.toJson(comment), new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Log.e("fail",""+e);
                                    EventInfo eventInfo = new EventInfo();
                                    eventInfo.setContentString("publishCommentFailed");
                                    EventBus.getDefault().post(eventInfo);
                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    Comment temp = gson.fromJson(response.body().string(),Comment.class);
                                    EventInfo eventInfo = new EventInfo();
                                    eventInfo.setContentString("publishCommentSuccess");
                                    Map<String,String> map = new HashMap<>();
                                    map.put("coid",temp.getCoid()+"");
                                    map.put("publishTime",temp.getPublishTime());
                                    eventInfo.setContentMap(map);
                                    EventBus.getDefault().post(eventInfo);
                                }
                            });
                        }
                    }else{
                        //如果已经登录     可以发表评论

                    }
                }else{
                    Toast.makeText(getContext(),"请等待上一条评论发送成功",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initAdapter() {

         commentAdapter = new CommentAdapter(R.layout.rv_comment_detail_layout,commentList,getContext(),currentTime);

    }




    public void hideSoftKeyboard(Context context, List<View> viewList) {
        if (viewList == null) return;

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        for (View v : viewList) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
