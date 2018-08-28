package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.shorder.dao.IQqOnLineRedisDao;
import com.wzitech.gamegold.shorder.entity.QqOnLineEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/1/11.
 */
@Repository
public class QqOnLineRedisDaoImpl extends AbstractRedisDAO<QqOnLineEO> implements IQqOnLineRedisDao {

    private static String QqOnLineKEY="gamegold:shorder:QQ:";

    @Autowired
    @Qualifier("userRedisTemplate")

    public void setTemplate(StringRedisTemplate template){
        super.template=template;
    }

    private static final JsonMapper jsonMapper=JsonMapper.nonEmptyMapper();

    /**
     * 插入数据
     */
    @Override
    public int insertQqOnLine(QqOnLineEO qqOnLineEO){
        String qqOnLineJson=jsonMapper.toJson(qqOnLineEO);
        zSetOps.add(QqOnLineKEY,qqOnLineJson,qqOnLineEO.getId().doubleValue());
        return 0;
    }

    /**
     * 更新数据
     */
    @Override
    public int updateQqOnLine(QqOnLineEO newEO,QqOnLineEO oldEO){
        if (oldEO!=null){
            zSetOps.removeRangeByScore(QqOnLineKEY,oldEO.getId().doubleValue(),oldEO.getId().doubleValue());
        }
        String qqOnLineJson=jsonMapper.toJson(newEO);
        zSetOps.add(QqOnLineKEY,qqOnLineJson,newEO.getId().doubleValue());
        return 0;
    }

    /**
     *删除数据
     */
    @Override
    public int deleteQqOnLine(Long id){
        zSetOps.removeRangeByScore(QqOnLineKEY,id.doubleValue(),id.doubleValue());
        return 0;
    }

    /**
     * 查找数据
     */
    @Override
    public List<QqOnLineEO> queryQqOnLine(){

        Set<String> values= zSetOps.range(QqOnLineKEY,0,-1);
        List<QqOnLineEO> qqOnLineList=new ArrayList<QqOnLineEO>();
        for(String qqOnLineJson:values){
            QqOnLineEO eo = jsonMapper.fromJson(qqOnLineJson,QqOnLineEO.class);
            qqOnLineList.add(eo);
        }
        return  qqOnLineList;
    }

    /**
     * 查找数据
     */
    @Override
    public QqOnLineEO getRandomQq(){
        Long size = zSetOps.size(QqOnLineKEY);
        if(size.longValue()>0){
            int randomIndex = (int) (Math.random() * size);
            Set<String> values= zSetOps.range(QqOnLineKEY,randomIndex,randomIndex);
            QqOnLineEO eo = jsonMapper.fromJson(values.iterator().next(),QqOnLineEO.class);
            return eo;
        }
        return null;
    }

    /**
     * 同步数据
     */
    @Override
    public int buildDate(List<QqOnLineEO> qqOnLineEOList){
        template.delete(QqOnLineKEY);
        for(QqOnLineEO eo:qqOnLineEOList){
            String qqOnLineJson=jsonMapper.toJson(eo);
            zSetOps.add(QqOnLineKEY,qqOnLineJson,eo.getId().doubleValue());
        }
        return 0;
    }

    @Override
    public int delete(){
        template.delete(QqOnLineKEY);
        return 0;
    }
}


