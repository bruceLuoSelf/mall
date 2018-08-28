package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.shorder.dao.IShGameConfigRedisDao;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/6.
 *
 *  *  Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/11  lvshuyan           ZW_C_JB_00008 商城增加通货
 */
@Repository
public class ShGameConfigRedisDaoImpl extends AbstractRedisDAO<ShGameConfig> implements IShGameConfigRedisDao {
    private static String SHGAMECONFIG_KEY="gamegold:shorder:shGameConfigNew:";

    @Autowired
    @Qualifier("userRedisTemplate")

    public void setTemplate(StringRedisTemplate template){
        super.template=template;
    }

    private static final JsonMapper jsonMapper= JsonMapper.nonEmptyMapper();

    @Override
    public ShGameConfig getByConfigKey(String gameName, String goodsTypeName) {
        BoundHashOperations<String, String, String> userOps = template.boundHashOps(SHGAMECONFIG_KEY+gameName);
        String json = userOps.get(goodsTypeName); //通货 by lvshuyan
        return jsonMapper.fromJson(json, ShGameConfig.class);
    }

    @Override
    public void save(ShGameConfig shGameConfig) {
        if(shGameConfig !=null  && shGameConfig.getGoodsTypeName() != null && !"全部类型".equals(shGameConfig.getGoodsTypeName())){
            BoundHashOperations<String, String, String> bhOps = template.boundHashOps(SHGAMECONFIG_KEY+shGameConfig.getGameName());
            String json=jsonMapper.toJson(shGameConfig);
            logger.info("更新收获游戏配置redis:"+shGameConfig.getGameName()+":"+shGameConfig.getGoodsTypeName()+json);
            bhOps.put(shGameConfig.getGoodsTypeName(),json); //通货 by lvshuyan
        }
    }

    @Override
    public int delete(ShGameConfig shGameConfig) {
        if(shGameConfig!=null){
            BoundHashOperations<String, String, String> bhOps = template.boundHashOps(SHGAMECONFIG_KEY+shGameConfig.getGameName());
            if(bhOps!=null){
                bhOps.delete(shGameConfig.getGoodsTypeName()); //通货 by lvshuyan
            }
        }
        return 0;
    }

    @Override
    public List<ShGameConfig> selectByName(String name) {
        BoundHashOperations<String, String, String> bhOps = template.boundHashOps(SHGAMECONFIG_KEY+name);
        List<ShGameConfig> list=new ArrayList<ShGameConfig>();
        if(bhOps!=null && bhOps.size()>0){
            for(String jsonStr:bhOps.values()){
                ShGameConfig shGameConfig=jsonMapper.fromJson(jsonStr,ShGameConfig.class);
                list.add(shGameConfig);
            }
        }
        return list;
    }

    /**
     * 根据游戏名称和开关获取配置
     * @param name
     * @param isEnabled
     * @param enableMall
     * @return
     */
    @Override
    public List<ShGameConfig> selectByNameAndSwitch(String name, Boolean isEnabled, Boolean enableMall) {
        List<ShGameConfig> configList = selectByName(name);
        List<ShGameConfig> resultList = new ArrayList();
        for(ShGameConfig config : configList){
            if(isEnabled!=null && config.getIsEnabled()!=isEnabled){
                continue;
            }
            if(enableMall!=null && config.getEnableMall()!=enableMall){
                continue;
            }

            resultList.add(config);
        }
        return resultList;
    }

}
