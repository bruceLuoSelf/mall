package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.TradeLogoEnum;
import com.wzitech.gamegold.common.utils.DESHelper;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GTR订单
 * Created by 汪俊杰 on 2016/12/14.
 */
@Component
public class GtrOrderManager extends AbstractBaseService implements IGtrOrderManager {
    /**
     * 加密KEY
     */
    @Value("${shrobot.secret_key}")
    private String secretKey = "";

    @Autowired
    private IDeliverySubOrderManager deliverySubOrderManager;

    @Autowired
    private ISplitRepositoryRequestManager splitRepositoryRequestManager;

    @Autowired
    private IDeliveryOrderManager deliveryOrderManager;

    @Autowired
    ISplitRepositorySubRequestManager splitRepositorySubRequestManager;

    /**
     * 分页查找GTR收货订单
     *
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public List<GtrShOrder> queryShOrderListInPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean isAsc) {
        GenericPage<DeliverySubOrder> genericPage = deliverySubOrderManager.queryListInPage(map, start, pageSize, orderBy, isAsc);
        List<DeliverySubOrder> subOrderList = genericPage.getData();
        List<GtrShOrder> list = new ArrayList<GtrShOrder>();
        if (subOrderList != null && subOrderList.size() > 0) {
            for (DeliverySubOrder subOrder : subOrderList) {
                GtrShOrder gtrShOrder = new GtrShOrder();
                gtrShOrder.setId(subOrder.getId());
                gtrShOrder.setOrderId(subOrder.getOrderId());
                gtrShOrder.setGameName(subOrder.getGameName());
                gtrShOrder.setRegion(subOrder.getRegion());
                gtrShOrder.setServer(subOrder.getServer());
                gtrShOrder.setGameRace(subOrder.getGameRace());

                String gameAccount = StringUtils.isBlank(subOrder.getGameAccount()) ? "" : subOrder.getGameAccount();
                String gamePwd = StringUtils.isBlank(subOrder.getGamePwd()) ? "" : subOrder.getGamePwd();
                String gamePwd2 = StringUtils.isBlank(subOrder.getSecondPwd()) ? "" : subOrder.getSecondPwd();
                String accountDataDes = getEncryptAccountInfo(gameAccount, gamePwd, gamePwd2);
                gtrShOrder.setGameAccount(accountDataDes);
                gtrShOrder.setRoleName(subOrder.getGameRole());
                gtrShOrder.setCount(subOrder.getCount());
                gtrShOrder.setAddress(subOrder.getAddress());
                gtrShOrder.setWords(subOrder.getWords());
                gtrShOrder.setStatus(subOrder.getStatus());
                gtrShOrder.setCreateTime(subOrder.getCreateTime());
                gtrShOrder.setSellerRoleName(subOrder.getSellerRoleName());
                gtrShOrder.setBuyerAccount(subOrder.getBuyerAccount());
                gtrShOrder.setBuyerTel(subOrder.getBuyerTel());
                list.add(gtrShOrder);
            }
        }
        return list;
    }

    /**
     * 分页查找GTR分仓订单
     *
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public List<GtrShOrder> querySplitOrderListInPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean isAsc) {
        GenericPage<SplitRepositorySubRequest> genericPage = splitRepositorySubRequestManager.queryListInPage(map, pageSize,start, orderBy, isAsc);
        List<SplitRepositorySubRequest> splitOrderList = genericPage.getData();
        List<GtrShOrder> list = new ArrayList<GtrShOrder>();
        if (splitOrderList != null && splitOrderList.size() > 0) {
            for (SplitRepositorySubRequest splitSubOrder : splitOrderList) {
                GtrShOrder gtrShOrder = new GtrShOrder();
                SplitRepositoryRequest splitRepositoryRequest = splitRepositoryRequestManager.queryByOrderId(splitSubOrder.getOrderId());
                gtrShOrder.setOrderId(splitSubOrder.getOrderId());
                gtrShOrder.setGameName(splitRepositoryRequest.getGameName());
                gtrShOrder.setRegion(splitRepositoryRequest.getRegion());
                gtrShOrder.setServer(splitRepositoryRequest.getServer());
                gtrShOrder.setGameRace(splitRepositoryRequest.getGameRace());
                gtrShOrder.setShRoleName(splitRepositoryRequest.getGameRole());
                gtrShOrder.setRoleName(splitSubOrder.getGameRole());
                gtrShOrder.setStatus(splitRepositoryRequest.getStatus());
                gtrShOrder.setTotalCount(splitRepositoryRequest.getCount());
                gtrShOrder.setBuyerAccount(splitRepositoryRequest.getBuyerAccount());
                //分仓子订单数据
                gtrShOrder.setCount(splitSubOrder.getCount());
                gtrShOrder.setId(splitSubOrder.getId());
                gtrShOrder.setCreateTime(splitSubOrder.getCreateTime());

                String gameAccount = StringUtils.isBlank(splitRepositoryRequest.getGameAccount()) ? "" : splitRepositoryRequest.getGameAccount();
                String gamePwd = StringUtils.isBlank(splitRepositoryRequest.getPwd()) ? "" : splitRepositoryRequest.getPwd();
                String gamePwd2 = StringUtils.isBlank(splitRepositoryRequest.getSecondPwd()) ? "" : splitRepositoryRequest.getSecondPwd();
                String accountDataDes = getEncryptAccountInfo(gameAccount, gamePwd, gamePwd2);
                gtrShOrder.setGameAccount(accountDataDes);
                list.add(gtrShOrder);
            }
        }
        return list;
    }

    @Override
    public List<RoboutOrder> queryShOrderListPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean isAsc) {
        GenericPage<DeliverySubOrder> genericPage = deliverySubOrderManager.queryListInPage(map, start, pageSize, orderBy, isAsc);
        List<DeliverySubOrder> subOrderList = genericPage.getData();
        List<RoboutOrder> list = new ArrayList<RoboutOrder>();
        if (subOrderList != null && subOrderList.size() > 0) {
            for (DeliverySubOrder subOrder : subOrderList) {
                RoboutOrder roboutOrder =new RoboutOrder();
                roboutOrder.setId(subOrder.getId());
                roboutOrder.setOrderId(subOrder.getOrderId());
                roboutOrder.setCreateTime(subOrder.getCreateTime());
                //如果是拍卖的订单(通过子订单 1.查出出货商等级 2.添加收货数量  3.与后4位随机数)
                DeliveryOrder deliveryOrder = deliveryOrderManager.selectByOrderId(subOrder.getOrderId());
                if (deliveryOrder==null){
                    throw new SystemException(ResponseCodes.NoSubOrder.getCode(),
                            ResponseCodes.NoSubOrder.getMessage());
                }
                roboutOrder.setSellerRoleLevel(deliveryOrder.getSellerRoleLevel());
                roboutOrder.setCount(subOrder.getCount());
                roboutOrder.setAfterFour(subOrder.getAfterFour());
                roboutOrder.setTradeLogo(subOrder.getTradeLogo());
                String gameAccount = StringUtils.isBlank(subOrder.getGameAccount()) ? "" : subOrder.getGameAccount();
                String gamePwd = StringUtils.isBlank(subOrder.getGamePwd()) ? "" : subOrder.getGamePwd();
                String gamePwd2 = StringUtils.isBlank(subOrder.getSecondPwd()) ? "" : subOrder.getSecondPwd();
                String accountDataDes = getEncryptAccountInfo(gameAccount, gamePwd, gamePwd2);
                roboutOrder.setGameAccount(accountDataDes);
                roboutOrder.setGameName(subOrder.getGameName());
                roboutOrder.setRegion(subOrder.getRegion());
                roboutOrder.setGameRole(subOrder.getGameRole());
                roboutOrder.setServer(subOrder.getServer());
                roboutOrder.setGameRace(subOrder.getGameRace());
                roboutOrder.setBuyerAccount(subOrder.getBuyerAccount());
                roboutOrder.setStatus(subOrder.getStatus());
                list.add(roboutOrder);
            }
        }
        return list;
    }

    /**
     * 获取加密的账号信息
     *
     * @param gameAccount
     * @param gamePwd
     * @param secondPwd
     * @return
     */
    private String getEncryptAccountInfo(String gameAccount, String gamePwd, String secondPwd) {
        //账户信息（游戏账号+游戏密码+二级密码DES加密）
        StringBuilder s = new StringBuilder("{");
        s.append("\"gameAccount\":\"").append(urlEncoding(gameAccount,"UTF-8")).append("\",");
        s.append("\"gamePwd\":\"").append(urlEncoding(gamePwd,"UTF-8")).append("\",");
        s.append("\"secondPwd\":\"").append(urlEncoding(secondPwd,"UTF-8")).append("\"");
        s.append("}");
        String account = null;
        try {
            account = DESHelper.encryptDesEcb(s.toString(), secretKey);
        } catch (Exception e) {
            logger.error("DES加密账号信息出错了", e);
        }
        return account;
    }

    public static String urlEncoding(String content, String enc) {
        if (StringUtils.isBlank(content)) return "";
        try {
            return URLEncoder.encode(content, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }
}
