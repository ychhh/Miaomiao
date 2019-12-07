package com.hbsd.rjxy.miaomiao.zlc.video.service;


import com.hbsd.rjxy.miaomiao.entity.Multi_info;
import com.hbsd.rjxy.miaomiao.zlc.utils.QiniuUtils;
import com.hbsd.rjxy.miaomiao.zlc.video.dao.VideoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hbsd.rjxy.miaomiao.zlc.constant.VideoConstant.PAGING_STEP;

@Service
public class VideoService {

    @Autowired
    VideoDao videoDao;

    public List<Multi_info> findAll(){
        return videoDao.findAll();
    }


    /**
     * 点击小鱼干
     * @param
     * @return
     */
    public int addHotByFish(int miid){
        return videoDao.feedFish(miid);
    }

    /**
     * 分页查询
     * @param page  当前页
     *
     *              PAGING_STEP是每次分页查询的数量
     * @return
     */
    public List<Multi_info> findVideoPaging(int page){
        return videoDao.findVideoPaging((page-1)*PAGING_STEP,PAGING_STEP);
    }


    /**
     *
     * 获取token
     * @return
     */
    public String getToken(){
        return new QiniuUtils().getUpToken();
    }





}
