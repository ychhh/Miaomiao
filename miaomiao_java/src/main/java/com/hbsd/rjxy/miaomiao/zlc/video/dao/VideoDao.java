package com.hbsd.rjxy.miaomiao.zlc.video.dao;

import com.hbsd.rjxy.miaomiao.entity.Multi_info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface VideoDao extends JpaRepository<Multi_info,Integer> {

    /**
     * 查询所有视频信息
     */
    @Query( value = "SELECT * FROM multi_info WHERE type=0",nativeQuery = true)
    List<Multi_info> findAll();

    /**
     * 赠送小鱼干
     * 需要传一个miid
     *
     */
    @Modifying
    @Transactional(readOnly = false)
    @Query( value = "UPDATE multi_info set mhot=mhot+1 where miid=? ",nativeQuery = true)
    int feedFish(int miid);


    /**
     * 分页查询视频
     *
     * @param start 开始的位置
     * @param step 每页显示的数量
     * @return
     */
    @Query(value = "SELECT * FROM multi_info limit ?,?",nativeQuery = true)
    List<Multi_info> findVideoPaging(int start,int step);


    /**
     * 添加视频评论个数
     * @param miid
     * @return
     */
    @Modifying
    @Query(value = "UPDATE multi_info SET mcomment_count=mcomment_count+1 WHERE miid=?",nativeQuery = true)
    int addVideoCommentAccount(int miid);


    @Modifying
    @Transactional(readOnly = false)
    @Query(value = "INSERT INTO multi_info(type,cid,uid,mpath,mupload_time,mcontent,mvisited,mstatus,mcomment_count,mformat,mhot,mcover,mrecommended,mtag)" +
            " VALUES(?,?,?,?,?,?,0,0,0,?,0,?,0.1,1)",nativeQuery =true)
    int publishMulti(int type, int cid, int uid, String mpath, String mupload_time,String mcontent,String mformat,String cover);





}
