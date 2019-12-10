package com.hbsd.rjxy.miaomiao.zlc.video.dao;

import com.hbsd.rjxy.miaomiao.entity.Comment;
import com.hbsd.rjxy.miaomiao.entity.RecordLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RecordDao extends JpaRepository<RecordLikes,Integer> {


    /**
     * 根据视频的miid,和uid查询所有这个用户对这条视频下面评论的点赞记录
     * @param miid
     * @return
     */
    @Query(value = "SELECT * FROM recordlikes WHERE miid=? AND uid=? AND rltype=1",nativeQuery = true)
    List<RecordLikes> findRecordsByMiidAndUid(int miid,int uid);


    /**
     * 根据coid和uid查询这个用户点赞过这个评论没有
     * @param coid
     * @param uid
     * @return
     */
    @Query(value = "SELECT * FROM recordlikes WHERE coid=? and uid=?",nativeQuery = true)
    List<RecordLikes> findRecordLikesByCoidAndUid(int coid,int uid);


    /**
     * 点赞
     * @param coid
     * @param uid
     * @return
     */
    @Modifying
    @Transactional(readOnly = false)
    @Query(value = "INSERT INTO recordlikes(coid,uid,rltype,miid) VALUES(?,?,1,?)",nativeQuery = true)
    int addRecord(int coid,int uid,int miid);

    /**
     * 点赞
     * @param coid
     * @param uid
     * @return
     */
    @Modifying
    @Transactional(readOnly = false)
    @Query(value = "UPDATE recordlikes set rltype=1 WHERE coid=? and uid=? and miid=?",nativeQuery = true)
    int recoverRecord(int coid,int uid,int miid);




    /**
     * 取消点赞
     *
     */
    @Modifying
    @Transactional(readOnly = false)
    @Query(value = "UPDATE recordlikes set rltype=0 WHERE coid=? AND uid=?",nativeQuery = true)
    int removeRecord(int coid,int uid);









}
