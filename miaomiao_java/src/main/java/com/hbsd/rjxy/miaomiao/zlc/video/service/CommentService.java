package com.hbsd.rjxy.miaomiao.zlc.video.service;


import com.hbsd.rjxy.miaomiao.entity.Comment;
import com.hbsd.rjxy.miaomiao.entity.Multi_info;
import com.hbsd.rjxy.miaomiao.zlc.video.dao.CommentDao;
import com.hbsd.rjxy.miaomiao.zlc.video.dao.VideoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hbsd.rjxy.miaomiao.zlc.constant.VideoConstant.COMMENT_PAGING_STEP;

@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;
    @Autowired
    VideoDao videoDao;


    public List<Comment> findCommentsByMiid(Multi_info multi_info){
        return commentDao.findCommentsByMiid(multi_info.getMiid());
    }


    /**
     * 根据视频的miid分页查询评论
     * Multi_info 视频对象
     * page 当前页数
     *
     */
    public List<Comment> findCommentsByMiidAndPage(int miid,int page){
        return commentDao.findCommentsByMiidAndPage(miid,(page-1)*COMMENT_PAGING_STEP,COMMENT_PAGING_STEP);
    }



    public List<Comment> findCommentsByMiidAndPageForLogin(int miid,int page){
        return commentDao.findCommentsByMiidAndPage(miid,(page-1)*COMMENT_PAGING_STEP,COMMENT_PAGING_STEP);
    }


    /**
     * 添加评论，修改视频的评论数量(事务控制)
     * Multi_info 视频对象
     * Comment 评论对象
     *
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public int addComment(Comment comment){
        videoDao.addVideoCommentAccount(comment.getMiid());
        commentDao.addComment(comment.getMiid(),comment.getColike(),comment.getCostatus(),comment.getUid(),comment.getCocontent(),comment.getPublishTime());
        return commentDao.findId(comment.getUid(),comment.getPublishTime());
    }


    /**
     *
     * 删除评论
     * Comment 评论对象
     *
     */
    public int deleteCommentByCoid(Comment comment){
        return commentDao.deleteCommentByCoid(comment.getCoid());
    }


    /**
     * 点赞评论
     * @param coid
     * @return
     */
    public int likeComment(int coid){
        return commentDao.likeComment(coid);
    }


    /**
     * 取消点赞
     * @param coid
     * @return
     */
    public int dislikeComment(int coid){
        return commentDao.dislikeComment(coid);
    }








}
