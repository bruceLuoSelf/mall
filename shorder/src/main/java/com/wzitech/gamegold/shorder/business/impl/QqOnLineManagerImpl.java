package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.business.IQqOnLineManager;
import com.wzitech.gamegold.shorder.dao.IQqOnLineDAO;
import com.wzitech.gamegold.shorder.dao.IQqOnLineRedisDao;
import com.wzitech.gamegold.shorder.entity.QqOnLineEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Administrator on 2017/1/10.
 */
@Component
public class QqOnLineManagerImpl extends AbstractBusinessObject implements IQqOnLineManager {
    @Autowired
    IQqOnLineRedisDao qqOnLineRedisDao;
    @Autowired
    IQqOnLineDAO qqOnLineDAO;

    /**
     * 随机获取在线客服QQ
     * @return
     */
    @Override
    public QqOnLineEO getRandomQq() {
        QqOnLineEO qqOnLineEO = qqOnLineRedisDao.getRandomQq();
        if(qqOnLineEO==null){
            Map queryMap = new HashMap();
            queryMap.put("online", true);
            List<QqOnLineEO> qqList = qqOnLineDAO.selectByMap(queryMap);

            if(qqList!=null&&qqList.size()>0){
                for(QqOnLineEO qqOnLine:qqList){
                    qqOnLineRedisDao.insertQqOnLine(qqOnLine);
                }
                qqOnLineEO = qqOnLineRedisDao.getRandomQq();
            }
        }
        return qqOnLineEO;
    }
    /**
     * 分页查询
     * @param map
     * @param pageSize
     * @param start
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<QqOnLineEO> query(Map<String, Object> map, int pageSize, int start, String orderBy, Boolean isAsc) {
        return qqOnLineDAO.selectByMap(map,pageSize,start,orderBy,isAsc);
    }

    /**
     * 增加
     * @param qqOnLine
     * @return
     */
    @Override
    public QqOnLineEO addQqOnLineEO(QqOnLineEO qqOnLine) {
        qqOnLine.setOnline(true);
        qqOnLine.setCreateTime(new Date());
        qqOnLine.setLastUpdateTime(new Date());
        System.out.println(qqOnLine);
        qqOnLineDAO.insert(qqOnLine);
        qqOnLineRedisDao.insertQqOnLine(qqOnLine);
        return qqOnLine;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void deleteQqOnLineEO(Long id) {
        QqOnLineEO qqOnLineEO=qqOnLineDAO.selectById(id);
        if(qqOnLineEO!=null){
            qqOnLineRedisDao.deleteQqOnLine(id);
        }
        qqOnLineDAO.deleteById(id);
    }

    /**
     * 修改
     * @param qqOnLine
     */
    @Override
    public void updateQqOnLineEO(QqOnLineEO qqOnLine) {
        if(qqOnLine!=null){
            QqOnLineEO oldTrade=qqOnLineDAO.selectById(qqOnLine.getId());
            if(oldTrade!=null){
                qqOnLineRedisDao.deleteQqOnLine(qqOnLine.getId());
                qqOnLine.setLastUpdateTime(new Date());
                qqOnLineDAO.update(qqOnLine);
                QqOnLineEO newQqOnLine=qqOnLineDAO.selectById(qqOnLine.getId());
                qqOnLineRedisDao.insertQqOnLine(newQqOnLine);
            }
        }
    }

    /**
     * 上线
     * @param id
     */
    @Override
    public void enabled(Long id) {
        QqOnLineEO qqOnLineEO=qqOnLineDAO.selectById(id);
        if(qqOnLineEO !=null){
            qqOnLineEO.setOnline(true);
            qqOnLineEO.setLastUpdateTime(new Date());
            qqOnLineDAO.update(qqOnLineEO);
            qqOnLineRedisDao.insertQqOnLine(qqOnLineEO);
        }
    }

    /**
     * 下线
     * @param id
     */
    @Override
    public void disabled(Long id) {
        QqOnLineEO qqOnLineEO=qqOnLineDAO.selectById(id);
        if(qqOnLineEO !=null){
            qqOnLineEO.setOnline(false);
            qqOnLineEO.setLastUpdateTime(new Date());
            qqOnLineDAO.update(qqOnLineEO);
            qqOnLineRedisDao.deleteQqOnLine(id);
        }
    }
}
