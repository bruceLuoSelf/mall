package com.wzitech.gamegold.facade.backend.action.repository;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.facade.backend.business.ISellerShManager;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
@ExceptionToJSON
public class SellerAction extends AbstractAction {

    public void setId(Long id) {
        this.id = id;
    }

    private SellerInfo seller;

    private Integer audit;

    private Long id;

    private String auditServiceAccount;

    private Date createStartTime;

    private Date createEndTime;

    private List<SellerInfo> sellerList;

    private boolean isShield;

    private boolean isHelper;

    private boolean isOnline;

    private String loginAccount;

    private String uid;

    private boolean isOpenSh;

    private boolean isPriceRob;

    private String phoneNumber;

    private Long updateId;

    private String updateLoginAccount;

    private String updateShopName;

    private String updateName;

    private String updatePhoneNumber;

    private String updateQQ;

    private String updateGames;

    private String updateNotes;


    @Autowired
    ISellerManager sellerManager;

    @Autowired
    IUserInfoManager userInfoManager;

    @Autowired
    ISellerShManager sellerShManager;

    /**
     * 查询卖家列表
     *
     * @return
     */
    public String querySeller() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        int userType = CurrentUserContext.getUserType();
        if (UserType.SystemManager.getCode() != userType && UserType.NomalManager.getCode() != userType) {
            if (UserType.MakeOrder.getCode() == userType || UserType.RecruitBusiness.getCode() == userType) {
                UserInfoEO user = (UserInfoEO) CurrentUserContext.getUser();
                paramMap.put("servicerId", user.getMainAccountId());
            } else {
                paramMap.put("servicerId", CurrentUserContext.getUid());
            }
        } else {
            if (StringUtils.isNotEmpty(auditServiceAccount)) {
                UserInfoEO servicer = userInfoManager.findUserByAccount(auditServiceAccount);
                if (servicer != null) {
                    paramMap.put("servicerId", servicer.getId());
                } else {
                    paramMap.put("servicerId", 0L);
                }
            }
        }
        if (seller != null) {
            paramMap.put("name", seller.getName());
            paramMap.put("checkState", seller.getCheckState());
            paramMap.put("loginAccount", seller.getLoginAccount());
            paramMap.put("phoneNumber", seller.getPhoneNumber());
        }
        paramMap.put("createStartTime", createStartTime);
        paramMap.put("createEndTime", WebServerUtil.oneDateLastTime(createEndTime));
        GenericPage<SellerInfo> genericPage = sellerManager.querySeller(paramMap, this.limit, this.start, null, true);
        sellerList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 开通收货的卖家批量生成环信账号
     */
    public String massProductionAccount() throws InterruptedException {
        try {
            sellerManager.MassProductionAccount();
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 删除卖家
     *
     * @return
     */
    public String auditSeller() {
        try {
            seller.setCheckState(audit);
            sellerManager.auditSeller(seller);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String givePower() {
        try {
            sellerManager.givePower(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String cancelPower() {
        try {
            sellerManager.cancelPower(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String openNewFund(){
        try {
            sellerManager.openNewFund(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String closeNewFund(){
        try {
            sellerManager.closeNewFund(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }


    public String openCross(){
        try {
            sellerManager.openCross(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String closeCross(){
        try {
            sellerManager.closeCross(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }


    public String shieldSeller() {
        try {
            sellerManager.shieldSeller(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String cancelShield() {
        try {
            sellerManager.cancelShield(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String giveManualStatus() {
        try {
            sellerManager.giveManualStatus(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String cancelManualStatus() {
        try {
            sellerManager.cancelManualStatus(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String saveShieldSeller() {
        try {
            sellerManager.saveShieldSeller(id, isShield, isHelper);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String eidtSeller() {
        try {
//            sellerManager.modifySeller(seller);
            sellerManager.modifySeller(updateId,updateLoginAccount,updateShopName,updateName,updatePhoneNumber,updateQQ,updateGames,updateNotes);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String online() {
        try {
            if (isOnline) {
                sellerManager.online(loginAccount, uid);
            } else {
                sellerManager.offlineBack(loginAccount, uid);
            }

            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String checkSh() {
        try {
            sellerShManager.checkSh(loginAccount, uid, isOpenSh, id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 开启/关闭自动更新价格
     *
     * @return
     */
    public String enablePriceRob() {
        try {
            sellerManager.enablePriceRob(loginAccount, uid, isPriceRob);

            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public SellerInfo getSeller() {
        return seller;
    }

    public void setSeller(SellerInfo seller) {
        this.seller = seller;
    }

    public List<SellerInfo> getSellerList() {
        return sellerList;
    }

    public void setAudit(Integer audit) {
        this.audit = audit;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public void setAuditServiceAccount(String auditServiceAccount) {
        this.auditServiceAccount = auditServiceAccount;
    }

    public boolean isShield() {
        return isShield;
    }

    public void setIsShield(boolean isShield) {
        this.isShield = isShield;
    }

    public boolean isHelper() {
        return isHelper;
    }

    public void setIsHelper(boolean isHelper) {
        this.isHelper = isHelper;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean getIsOpenSh() {
        return isOpenSh;
    }

    public void setIsOpenSh(boolean isOpenSh) {
        this.isOpenSh = isOpenSh;
    }

    public boolean isPriceRob() {
        return isPriceRob;
    }

    public void setIsPriceRob(boolean isPriceRob) {
        this.isPriceRob = isPriceRob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public String getUpdateLoginAccount() {
        return updateLoginAccount;
    }

    public void setUpdateLoginAccount(String updateLoginAccount) {
        this.updateLoginAccount = updateLoginAccount;
    }

    public String getUpdateShopName() {
        return updateShopName;
    }

    public void setUpdateShopName(String updateShopName) {
        this.updateShopName = updateShopName;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getUpdatePhoneNumber() {
        return updatePhoneNumber;
    }

    public void setUpdatePhoneNumber(String updatePhoneNumber) {
        this.updatePhoneNumber = updatePhoneNumber;
    }

    public String getUpdateQQ() {
        return updateQQ;
    }

    public void setUpdateQQ(String updateQQ) {
        this.updateQQ = updateQQ;
    }

    public String getUpdateGames() {
        return updateGames;
    }

    public void setUpdateGames(String updateGames) {
        this.updateGames = updateGames;
    }

    public String getUpdateNotes() {
        return updateNotes;
    }

    public void setUpdateNotes(String updateNotes) {
        this.updateNotes = updateNotes;
    }
}
