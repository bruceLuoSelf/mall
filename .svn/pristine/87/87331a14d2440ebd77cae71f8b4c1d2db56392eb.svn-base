package com.wzitech.gamegold.rc8.service.order.dto;

import com.wzitech.gamegold.rc8.dto.Response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.List;

/**
 * 查询订单列表响应
 *
 * @author yemq
 */
@XmlRootElement(name = "Result")
@XmlType(propOrder = {"totalCount", "totalPage", "orders"})
public class QueryOrderListResponse extends Response {
    /**
     * 总记录条数
     */
    private Long totalCount;
    /**
     * 总页数
     */
    private Long totalPage;
    private List<Order> orders;

    public QueryOrderListResponse() {
    }

    @XmlElementWrapper(name = "Orders")
    @XmlElement(name = "Order")
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    @XmlRootElement(name = "Order")
    @XmlType(propOrder = {
            "title", "orderId", "gameName", "gameRegion", "gameServer", "gameRace", "goldCount", "unitPrice",
            "totalPrice", "buyerAccount", "buyerQq", "buyerGameRole", "serviceAccount", "status", "createTime", "paidTime",
            "sendTime", "endTime","goodsTypeName","goodsTypeId","digitalId","gameGrade"
    })
    public static class Order {
        /**
         * 标题
         */
        private String title;
        /**
         * 订单号
         */
        private String orderId;
        /**
         * 游戏名
         */
        private String gameName;
        /**
         * 游戏区
         */
        private String gameRegion;
        /**
         * 游戏服
         */
        private String gameServer;
        /**
         * 游戏阵营
         */
        private String gameRace;
        /**
         * 配置的游戏币数目
         */
        private Long goldCount;
        /**
         * 单价(1游戏币兑换多少元)
         */
        private BigDecimal unitPrice;
        /**
         * 总费用
         */
        private BigDecimal totalPrice;
        /**
         * 买家5173帐号
         */
        private String buyerAccount;
        /**
         * 买家QQ
         */
        private String buyerQq;
        /**
         * 买家游戏角色名
         */
        private String buyerGameRole;
        /**
         * 所属客服账号
         */
        private String serviceAccount;
        /**
         * 状态
         */
        private Integer status;
        /**
         * 创建时间
         */
        private String createTime;

        /**
         * 付款时间
         */
        private String paidTime;

        /**
         * 发货时间
         */
        private String sendTime;

        /**
         * 结束时间
         */
        private String endTime;

        /**
         * 通货类型ID
         * lvchengsheng 5.16新增 ZW_C_JB_00008 商城增加通货
         */
        private Long goodsTypeId;

        /**
         * 通货类型名称
         * lvchengsheng 5.16新增 ZW_C_JB_00008 商城增加通货
         */
        private String goodsTypeName;

        /**
         *商品数字ID
         */
        private String digitalId;

        /**
         * 游戏等级
         */
        private Integer gameGrade;

        public Integer getGameGrade() {
            return gameGrade;
        }

        public void setGameGrade(Integer gameGrade) {
            this.gameGrade = gameGrade;
        }

        public String getDigitalId() {
            return digitalId;
        }

        public void setDigitalId(String digitalId) {
            this.digitalId = digitalId;
        }

        public Long getGoodsTypeId() {
            return goodsTypeId;
        }

        public void setGoodsTypeId(Long goodsTypeId) {
            this.goodsTypeId = goodsTypeId;
        }

        public String getGoodsTypeName() {
            return goodsTypeName;
        }

        public void setGoodsTypeName(String goodsTypeName) {
            this.goodsTypeName = goodsTypeName;
        }

        public Order() {
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public String getGameRegion() {
            return gameRegion;
        }

        public void setGameRegion(String gameRegion) {
            this.gameRegion = gameRegion;
        }

        public String getGameServer() {
            return gameServer;
        }

        public void setGameServer(String gameServer) {
            this.gameServer = gameServer;
        }

        public String getGameRace() {
            return gameRace;
        }

        public void setGameRace(String gameRace) {
            this.gameRace = gameRace;
        }

        public Long getGoldCount() {
            return goldCount;
        }

        public void setGoldCount(Long goldCount) {
            this.goldCount = goldCount;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getBuyerAccount() {
            return buyerAccount;
        }

        public void setBuyerAccount(String buyerAccount) {
            this.buyerAccount = buyerAccount;
        }

        public String getBuyerQq() {
            return buyerQq;
        }

        public void setBuyerQq(String buyerQq) {
            this.buyerQq = buyerQq;
        }

        public String getServiceAccount() {
            return serviceAccount;
        }

        public void setServiceAccount(String serviceAccount) {
            this.serviceAccount = serviceAccount;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getBuyerGameRole() {
            return buyerGameRole;
        }

        public void setBuyerGameRole(String buyerGameRole) {
            this.buyerGameRole = buyerGameRole;
        }

        public String getPaidTime() {
            return paidTime;
        }

        public void setPaidTime(String paidTime) {
            this.paidTime = paidTime;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }


}
