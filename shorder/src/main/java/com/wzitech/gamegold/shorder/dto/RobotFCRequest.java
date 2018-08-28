package com.wzitech.gamegold.shorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

import java.util.Date;

/**
 * Created by 340032 on 2018/6/20.
 */
public class RobotFCRequest extends AbstractServiceRequest {

    /**
     * 5173登入账号
     */
    private String loginAccount;

    /**
     * 库存类型(1：销售；2：收货；3：分仓)
     */
    private Integer stockType;
    /**
     * 背包数量
     */
    private Long packageCount;
    /**
     * 金库数量
     */
    private Long repertoryCount;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 区名
     */
    private String regionName;
    /**
     * 服名
     */
    private String serverName;
    /**
     * 阵营名
     */
    private String raceName;
    /**
     * 游戏账号
     */
    private String gameAccount;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 关联(销售、收货、分仓)主订单号
     */
    private String orderId;
    /**
     * 关联(销售、收货、分仓)子订单号
     */
    private Long subOrderId;

    /**
     * 签名
     */
    private String sign;

    /**
     * 缺口数量
     * @return
     */
    private Long count;

    /**
     *收入支出类型
     */
    private Integer incomeType;
    /**
     * 日志内容
     */
    private String log;

    /**
     * 收货方5173账号   ---出货
     */
    private String buyerAccount;


    /**
     * 异常原因
     * @return
     */
    private Integer robotReason;

    /**
     * 自动化异常
     * @return
     */
    private String robotOtherReason;

    /**
     * 金币来源数量
     * @return
     */
    private Long useCepertoryCount;
    /**
     * 每页笔数
     */
    private Integer pageSize;

    /**
     * 分成子订单
     * @return
     *
     */
    private Long id;

    /**
     * 实际分仓数量
     * @return
     */
    private Long realCount;

    public Long getRealCount() {
        return realCount;
    }

    public void setRealCount(Long realCount) {
        this.realCount = realCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getUseCepertoryCount() {
        return useCepertoryCount;
    }

    public void setUseCepertoryCount(Long useCepertoryCount) {
        this.useCepertoryCount = useCepertoryCount;
    }

    public String getRobotOtherReason() {
        return robotOtherReason;
    }

    public void setRobotOtherReason(String robotOtherReason) {
        this.robotOtherReason = robotOtherReason;
    }

    public Integer getRobotReason() {
        return robotReason;
    }

    public void setRobotReason(Integer robotReason) {
        this.robotReason = robotReason;
    }

    public Integer getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(Integer incomeType) {
        this.incomeType = incomeType;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public Integer getStockType() {
        return stockType;
    }

    public void setStockType(Integer stockType) {
        this.stockType = stockType;
    }

    public Long getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(Long packageCount) {
        this.packageCount = packageCount;
    }

    public Long getRepertoryCount() {
        return repertoryCount;
    }

    public void setRepertoryCount(Long repertoryCount) {
        this.repertoryCount = repertoryCount;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public String getGameAccount() {
        return gameAccount;
    }

    public void setGameAccount(String gameAccount) {
        this.gameAccount = gameAccount;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
}
