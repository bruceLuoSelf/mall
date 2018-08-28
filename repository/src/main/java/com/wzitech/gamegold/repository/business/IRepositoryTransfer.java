package com.wzitech.gamegold.repository.business;

import java.util.Map;

/**
 * 合服后服务器库存互通接口
 *
 * @author yemq
 */
public interface IRepositoryTransfer {
    /**
     * 对查询参数进行处理，返回新的查询参数
     *
     * @param queryMap
     * @return Map<String, Object>
     */
    Map<String, Object> process(Map<String, Object> queryMap);

    /**
     * 获取互通的服务器
     *
     * @param region 区
     * @param server 服
     * @return Map<String, Object>,map等于null或servers等于null表示未找到互通的服务器
     * <li>serverCount:存放合服后还有几个服</li>
     * <li>servers:返回所有互通的服务器</li>
     */
    Map<String, Object> getServers(String region, String server);

    /**
     * 获取合服互通的服务器
     *
     * @param server 服
     * @return Map<String, Object>,map等于null或servers等于null表示未找到互通的服务器
     * <li>serverCount:存放合服后还有几个服</li>
     * <li>servers:返回所有互通的服务器</li>
     */
    Map<String, Object> getServers(String server);

    /**
     * 获取库存互通配置文件
     * @return
     */
    String getRepositoryTransferFile();
}
