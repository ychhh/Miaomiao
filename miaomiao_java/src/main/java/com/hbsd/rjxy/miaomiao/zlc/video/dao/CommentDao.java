package com.hbsd.rjxy.miaomiao.zlc.video.dao;

import com.hbsd.rjxy.miaomiao.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentDao extends JpaRepository<Comment,Integer> {


    /**
     * 根据视频的miid查询所有评论
     * @param miid
     * @return
     */
    @Query(value = "SELECT * FROM comment WHERE miid=? AND costatus=0",nativeQuery = true)
    List<Comment> findCommentsByMiid(int miid);


    /**
     * 根据视频的miid分页查询评论
     *
     */
    @Query(value = "SELECT * FROM comment WHERE miid=? AND costatus=0 ORDER BY publish_time desc , uid asc limit ?,?",nativeQuery = true)
    List<Comment> findCommentsByMiidAndPage(int miid,int start,int step);


    /**
     * 添加评论
     */
    @Modifying
    @Transactional(readOnly = false)
    @Query(value = "INSERT INTO comment(miid,colike,costatus,uid,cocontent,publish_time) VALUES(?,?,?,?,?,?)",nativeQuery = true)
    int addComment(int miid,int colike,int costatus,int uid,String cocontent,String publishTime);


    @Transactional(readOnly = false)
    @Query(value = "SELECT coid FROM comment WHERE uid=? AND publish_time=?",nativeQuery = true)
    int findId(int uid,String publishTime);


    /**
     * 点赞评论
     */
    @Modifying
    @Transactional(readOnly = false)
    @Query(value = "UPDATE comment SET colike=colike+1 WHERE coid=?",nativeQuery = true)
    int likeComment(int coid);

    /**
     * 取消点赞评论
     */
    @Modifying
    @Transactional(readOnly = false)
    @Query(value = "UPDATE comment SET colike=colike-1 WHERE coid=?",nativeQuery = true)
    int dislikeComment(int coid);


    /**
     *
     * 删除评论
     */
    @Modifying
    @Transactional(readOnly = false)
    @Query(value = "UPDATE comment SET costatus=1 WHERE coid=?",nativeQuery = true)
    int deleteCommentByCoid(int coid);


    /**
     *
     *
     */




}
