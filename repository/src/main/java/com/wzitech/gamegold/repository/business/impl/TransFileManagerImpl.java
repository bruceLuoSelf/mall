package com.wzitech.gamegold.repository.business.impl;


import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.repository.business.ITransFileManager;
import com.wzitech.gamegold.repository.dao.ITransFileDBDAO;
import com.wzitech.gamegold.repository.dao.ITransferRedisDao;
import com.wzitech.gamegold.repository.entity.TransferFile;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/20  wangmin           ZW_J_JB_00073 金币商城增加区服互通功能
 */
@Component
public class TransFileManagerImpl implements ITransFileManager {
    @Autowired
    ITransFileDBDAO transFileDBDAO;

    JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    @Autowired
    ITransferRedisDao transFerRedisDao;

    @Override
    @Transactional
    public void modifyTransFile(Long id, String jsonString, String gameName) throws SystemException {
        if (StringUtils.isBlank(gameName)){
            throw new SystemException("游戏名称不能为空");
        }
        String jsonStringNew = transFerRedisDao.getJsonString(gameName);
        if(jsonString.equals(jsonStringNew)){
            throw new SystemException("相同配置已存在,请确认修改后提交");
        }
        TransferFile transferFile = new TransferFile();
        transferFile.setJsonString(jsonString);
        transferFile.setId(id);
        transferFile.setGameName(gameName);
        List<TransferFile> list = new ArrayList<TransferFile>();
        transferFile.setUpdateTime(new Date());
        list.add(transferFile);
        transFileDBDAO.batchUpdate(list);
        transFerRedisDao.save(list.get(0));
    }

    @Override
    @Transactional
    public GenericPage<TransferFile> selectTransFileInfo(Map<String, Object> map, int pageSize, int start, String orderBy, Boolean isAsc) {
        return transFileDBDAO.selectByMap(map, pageSize, start, orderBy, isAsc);
    }

    @Override
    @Transactional
    public List<TransferFile> selectAllInfo(String gameName, Long id) {
        Map<String, String> transferFileMapper = transFerRedisDao.getJsonString();
        if (transferFileMapper != null && transferFileMapper.size() == TransferFile.GAMESIZE) {
            List<TransferFile> list = new ArrayList<TransferFile>();
            for (Map.Entry<String, String> entry:transferFileMapper.entrySet()){
                TransferFile transferFile = jsonMapper.fromJson(entry.getValue(), TransferFile.class);
                list.add(transferFile);
            }
            return list;
        }
        List<TransferFile> selectTransferFile = transFileDBDAO.selectAll();
        if (selectTransferFile.size() != 0){
            transFerRedisDao.remove();
            for ( TransferFile transferFiles : selectTransferFile){
                transFerRedisDao.save(transferFiles);
            }
        }
        return selectTransferFile;
    }
}
