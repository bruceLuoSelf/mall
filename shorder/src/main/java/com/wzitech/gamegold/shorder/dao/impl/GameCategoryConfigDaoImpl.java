package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IGameCategoryConfigDao;
import com.wzitech.gamegold.shorder.entity.GameCategoryConfig;
import org.springframework.stereotype.Repository;

/**
 * 游戏类目配置
 */
@Repository
public class GameCategoryConfigDaoImpl extends AbstractMyBatisDAO<GameCategoryConfig, Long> implements IGameCategoryConfigDao {
}