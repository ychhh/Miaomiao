package com.hbsd.rjxy.miaomiao.zlc.video.service;


import com.hbsd.rjxy.miaomiao.entity.RecordLikes;
import com.hbsd.rjxy.miaomiao.zlc.video.dao.RecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {

    @Autowired
    RecordDao recordDao;

    public List<RecordLikes> findRecords(int miid,int uid){
        return recordDao.findRecordsByMiidAndUid(miid, uid);
    }


    public int addRecord(int coid,int uid,int miid){
        List<RecordLikes> recordLikes = recordDao.findRecordLikesByCoidAndUid(coid, uid);
        if(recordLikes.size() == 0){

            //没有一条点赞记录
            return recordDao.addRecord(coid, uid,miid);
        }else{
            //存在点赞记录，修改rltype的值为1

            return recordDao.recoverRecord(coid, uid, miid);
        }
    }


    public int removeRecord(int coid,int uid){
        return recordDao.removeRecord(coid, uid);
    }

}
