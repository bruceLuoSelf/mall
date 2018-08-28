package com.wzitech.gamegold.repository.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.game.IGameInfoManager;
import com.wzitech.gamegold.common.game.entity.GameDetailInfo;
import com.wzitech.gamegold.repository.business.ISellerGameManager;
import com.wzitech.gamegold.repository.dao.ISellerGameDBDAO;
import com.wzitech.gamegold.repository.entity.SellerGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

/**
 * 卖家入驻选择的游戏管理实现
 *
 * @author yemq
 */
@Component
public class SellerGameManagerImpl extends AbstractBusinessObject implements ISellerGameManager {

    @Autowired
    private ISellerGameDBDAO sellerGameDBDAO;

    @Autowired
    private IGameInfoManager gameInfoManager;

    @Transactional
    @Override
    public void addAll(List<SellerGame> games, Long sellerId) throws SystemException {
        if (games == null || games.size() == 0)
            throw new SystemException(ResponseCodes.EmptySellerGame.getCode(), ResponseCodes.EmptySellerGame.getMessage());
        if (sellerId == null)
            throw new SystemException(ResponseCodes.EmptySellerId.getCode(), ResponseCodes.EmptySellerId.getMessage());

        // 先清空卖家的游戏记录
        deleteBySellerId(sellerId);

        for (SellerGame game : games) {
            game.setSellerId(sellerId);
            sellerGameDBDAO.insert(game);
        }
    }

    @Transactional
    @Override
    public void deleteBySellerId(Long sellerId) throws SystemException {
        if (sellerId == null)
            throw new SystemException(ResponseCodes.EmptySellerId.getCode(), ResponseCodes.EmptySellerId.getMessage());
        sellerGameDBDAO.deleteBySellerId(sellerId);
    }

    /**
     * 初始化游戏名称
     *
     * @param games
     * @throws IOException
     * @throws JAXBException
     */
    @Transactional(readOnly = true)
    public void initGameName(List<SellerGame> games) throws IOException, JAXBException {
        if (games == null || games.size() == 0)
            return;
        for (SellerGame game : games) {
            GameDetailInfo gameDetailInfo = gameInfoManager.getGameById(game.getGameId());
            game.setGameName(gameDetailInfo.getName());
        }
    }
}
