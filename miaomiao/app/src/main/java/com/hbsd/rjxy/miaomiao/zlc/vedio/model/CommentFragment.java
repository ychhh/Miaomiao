package com.hbsd.rjxy.miaomiao.zlc.vedio.model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.hbsd.rjxy.miaomiao.R;


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


public class CommentFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_fragment,null);




        return view;


    }
}
