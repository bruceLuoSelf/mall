package com.wzitech.gamegold.repository.business;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.repository.entity.SellerGame;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

/**
 * 卖家入驻选择的游戏管理接口
 *
 * @author yemq
 */
public interface ISellerGameManager {
    /**
     * 添加卖家选择的所有游戏
     *
     * @param games    卖家选择的游戏集合
     * @param sellerId 卖家ID
     * @throws SystemException
     */
    void addAll(List<SellerGame> games, Long sellerId) throws SystemException;

    /**
     * 根据卖家ID删除卖家关联的所有游戏
     *
     * @param sellerId
     * @throws SystemException
     */
    void deleteBySellerId(Long sellerId) throws SystemException;

    /**
     * 初始化游戏名称
     *
     * @param games
     * @throws java.io.IOException
     * @throws javax.xml.bind.JAXBException
     */
    public void initGameName(List<SellerGame> games) throws IOException, JAXBException;
}
