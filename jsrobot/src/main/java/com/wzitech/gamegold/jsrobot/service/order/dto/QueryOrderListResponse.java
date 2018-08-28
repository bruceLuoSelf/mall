package com.wzitech.gamegold.jsrobot.service.order.dto;

import com.wzitech.gamegold.jsrobot.dto.Response;

import java.util.Date;
import java.util.List;

/**
 * 查询订单列表响应信息
 * @author yemq
 */
public class QueryOrderListResponse extends Response {

    private Integer page;
    private Integer pageSize;
    /**
     * 总记录数
     */
    private Long totalCount;
    /**
     * 总页数
     */
    private Long totalPage;
    private List<Order> orders;

    public QueryOrderListResponse() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public static class Order {
        /**
         * 主订单号
         */
        private String orderId;
        /**
         * 子订单号
         */
        private Long subOrderId;
        /**
         * 游戏名
         */
        private String gameName;
        /**
         * 游戏ID
         */
        private String gameId;
        /**
         * 游戏区
         */
        private String region;
        /**
         * 游戏区ID
         */
        private String regionId;
        /**
         * 游戏服
         */
        private String server;
        /**
         * 游戏服ID
         */
        private String serverId;
        /**
         * 游戏阵营
         */
        private String gameRace;
        /**
         * 游戏阵营ID
         */
        private String raceId;
        /**
         * 创建时间
         */
        private String createTime;
        /**
         * 发货区
         */
        private String regionShip;

        /**
         * 发货服
         */
        private String serverShip;

        public String getRegionShip() {
            return regionShip;
        }

        public void setRegionShip(String regionShip) {
            this.regionShip = regionShip;
        }

        public String getServerShip() {
            return serverShip;
        }

        public void setServerShip(String serverShip) {
            this.serverShip = serverShip;
        }

        public Order() {
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Long getSubOrderId() {
            return subOrderId;
        }

        public void setSubOrderId(Long subOrderId) {
            this.subOrderId = subOrderId;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getRegionId() {
            return regionId;
        }

        public void setRegionId(String regionId) {
            this.regionId = regionId;
        }

        public String getServer() {
            return server;
        }

        public void setServer(String server) {
            this.server = server;
        }

        public String getServerId() {
            return serverId;
        }

        public void setServerId(String serverId) {
            this.serverId = serverId;
        }

        public String getGameRace() {
            return gameRace;
        }

        public void setGameRace(String gameRace) {
            this.gameRace = gameRace;
        }

        public String getRaceId() {
            return raceId;
        }

        public void setRaceId(String raceId) {
            this.raceId = raceId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }

}
