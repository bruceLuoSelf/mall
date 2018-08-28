package com.wzitech.gamegold.repository.business.impl;

import com.wzitech.gamegold.repository.dao.ITransFileDBDAO;
import com.wzitech.gamegold.repository.dao.ITransferRedisDao;
import com.wzitech.gamegold.repository.entity.TransferFile;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 剑灵合服库存互通实现
 *
 * @author yemq
 */
@Component("dnfRepositoryTransferManager")
public class DnfRepositoryTransferManagerImpl extends AbstractRepositoryTransfer {

    @Autowired
    private ITransFileDBDAO transFileDBDAO;

    @Autowired
    private ITransferRedisDao transferRedisDao;

    /**
     * 获取库存互通配置文件
     *
     * @return
     */
    @Override
    public String getRepositoryTransferFile() {
        String transferFile = transferRedisDao.getJsonString("地下城与勇士");
        if (StringUtils.isBlank(transferFile)) {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("gameName", "地下城与勇士");
            List<TransferFile> list = transFileDBDAO.selectByMap(queryMap);
            if (list.size() == 1) {
                transferFile = list.get(0).getJsonString();
                transferRedisDao.save(list.get(0));
            }
        }
        return transferFile;
    }

}
