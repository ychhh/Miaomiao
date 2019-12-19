package com.hbsd.rjxy.miaomiao.zlc.video.service;


import com.hbsd.rjxy.miaomiao.entity.Multi_info;
import com.hbsd.rjxy.miaomiao.ych.subscription.dao.SubDao;
import com.hbsd.rjxy.miaomiao.zlc.utils.QiniuUtils;
import com.hbsd.rjxy.miaomiao.zlc.video.dao.VideoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.hbsd.rjxy.miaomiao.zlc.constant.VideoConstant.PAGING_STEP;

@Service
public class VideoService {

    @Autowired
    VideoDao videoDao;
    @Autowired
    SubDao subDao;

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
        System.out.println("page"+page);
        return videoDao.findVideoPaging((page-1)*PAGING_STEP,PAGING_STEP);
    }


    /**
     * 分页查询，根据订阅
     */
    public List<Multi_info> findVideoPagingByUid(int page,int uid){
        if(subDao.findAllByUid(uid).size() == 0){
            System.out.println("未查到订阅记录");
            List<Multi_info> list = new ArrayList<>();
            return list;
        }
        System.out.println(videoDao.findVideoPagingByUid(uid,(page-1)*PAGING_STEP,PAGING_STEP));
        return videoDao.findVideoPagingByUid(uid,(page-1)*PAGING_STEP,PAGING_STEP);
    }


    /**
     *
     * 获取token
     * @return
     */
    public String getToken(){
        return new QiniuUtils().getUpToken();
    }


    /*
        发布
    
     */
    public int publishMulti(Multi_info multi_info){
        return videoDao.publishMulti(multi_info.getType(),multi_info.getCid(),multi_info.getUid(),multi_info.getMpath(),multi_info.getMupload_time(),multi_info.getMcontent(),multi_info.getMformat(),multi_info.getMcover());
    }



}
