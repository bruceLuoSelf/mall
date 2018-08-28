package com.wzitech.gamegold.facade.backend.action.repository;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2017/05/12    wubiao     ZW_C_JB_00008 商城增加通货
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class RepositoryAction extends AbstractAction {

    private RepositoryInfo repository;

    private List<Long> repositoryIds;

    private List<RepositoryInfo> repositoryList;

    private Boolean isNeed;

    /**
     * 卖家是否在线
     */
    private String sellerIsOnline;
    /**
     * 商品类型
     */
    private String goodsTypeName;


    @Autowired
    IRepositoryManager repositoryManager;

    /**
     * 查询库存列表
     *
     * @return
     */
    public String queryRepository() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        /*int userType = CurrentUserContext.getUserType();
        if(UserType.SystemManager.getCode()!=userType){
			if(UserType.MakeOrder.getCode()==userType||UserType.RecruitBusiness.getCode()==userType){
				UserInfoEO user = (UserInfoEO)CurrentUserContext.getUser();
				paramMap.put("servicerId", user.getMainAccountId());
			}else{
				paramMap.put("servicerId", CurrentUserContext.getUid());
			}
		}*/

        if (repository != null) {
            if (isNeed != null && isNeed) {
                paramMap.remove("servicerId");
            }
            if (StringUtils.isNotBlank(repository.getGameName()) && StringUtils.isBlank(repository.getRegion())) {
                // 按游戏属性2中的游戏名称和游戏服务器进行模糊查询
                paramMap.put("gameName", repository.getGameName());
                paramMap.put("server", repository.getServer());
            } else {
                // 按游戏属性中的游戏名称、游戏区、游戏服务器进行精准查询
                paramMap.put("backGameName", repository.getGameName());
                paramMap.put("backRegion", repository.getRegion());
                paramMap.put("backServer", repository.getServer());
            }

            paramMap.put("gameRace", repository.getGameRace());
            paramMap.put("sellableCount", repository.getGoldCount());
            paramMap.put("orderUnitPrice", repository.getUnitPrice());
            paramMap.put("loginAccount", repository.getLoginAccount());
            paramMap.put("goodsTypeId", repository.getGoodsTypeId());
            paramMap.put("gameAccount", repository.getGameAccount());
            paramMap.put("backSellerGameRole", repository.getSellerGameRole());
            paramMap.put("isNeed", isNeed);
            paramMap.put("stockCount", repository.getStockCount());
            paramMap.put("goodsTypeName", repository.getGoodsTypeName());//ZW_C_JB_00008 add
            // 卖家是否在线
            if (StringUtils.isNotBlank(sellerIsOnline)) {
                paramMap.put("isOnline", Boolean.parseBoolean(sellerIsOnline));
            }
        }
        GenericPage<RepositoryInfo> genericPage = repositoryManager.queryStockRepository(paramMap, this.limit, this.start, "UNIT_PRICE", true);
        repositoryList = genericPage.getData();

        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 查询库存列表
     *
     * @return
     */
    public String queryOrderRepository() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        /*int userType = CurrentUserContext.getUserType();
        if(UserType.SystemManager.getCode()!=userType){
			if(UserType.MakeOrder.getCode()==userType||UserType.RecruitBusiness.getCode()==userType){
				UserInfoEO user = (UserInfoEO)CurrentUserContext.getUser();
				paramMap.put("servicerId", user.getMainAccountId());
			}else{
				paramMap.put("servicerId", CurrentUserContext.getUid());
			}
		}*/

//        if (StringUtils.isBlank(goodsTypeName)) {
//            logger.info("查询库存列表，商品类型不能为空，repository:" + repository);
//            return this.returnError(ResponseCodes.EmptyGoodsType.getCode());
//        }
        if (repository != null) {
            if (isNeed != null && isNeed) {
                paramMap.remove("servicerId");
            }
            if (StringUtils.isNotBlank(repository.getGameName()) && StringUtils.isBlank(repository.getRegion())) {
                // 按游戏属性2中的游戏名称和游戏服务器进行模糊查询
                paramMap.put("gameName", repository.getGameName());
                paramMap.put("server", repository.getServer());
            } else {
                // 按游戏属性中的游戏名称、游戏区、游戏服务器进行精准查询
                paramMap.put("backGameName", repository.getGameName());
                paramMap.put("backRegion", repository.getRegion());
                paramMap.put("backServer", repository.getServer());
            }

            paramMap.put("gameRace", repository.getGameRace());
            paramMap.put("sellableCount", repository.getGoldCount());
            paramMap.put("orderUnitPrice", repository.getUnitPrice());
            paramMap.put("loginAccount", repository.getLoginAccount());
            paramMap.put("goodsTypeId", repository.getGoodsTypeId());
            paramMap.put("gameAccount", repository.getGameAccount());
            paramMap.put("backSellerGameRole", repository.getSellerGameRole());
            paramMap.put("isNeed", isNeed);
            paramMap.put("goodsTypeName",goodsTypeName);//ZW_C_JB_00008 add
            // 卖家是否在线
            if (StringUtils.isNotBlank(sellerIsOnline)) {
                paramMap.put("isOnline", Boolean.parseBoolean(sellerIsOnline));
            }
        }
        logger.info("查询库存列表:" + paramMap);
        GenericPage<RepositoryInfo> genericPage = repositoryManager.queryRepository(paramMap, this.limit, this.start, "UNIT_PRICE", true);
        repositoryList = genericPage.getData();

        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 新增库存信息
     *
     * @return
     */
    public String addRepository() {
        try {
            repository.setServicerId(CurrentUserContext.getUid());
            repositoryManager.addRepositorySingle(repository);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 修改库存
     *
     * @return
     */
    public String modifyRepository() {
        try {
            //修改库存不能修改库存对应的客服
            //repository.setServicerId(CurrentUserContext.getUid());
            repositoryManager.modifyRepository(repository);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 删除库存
     *
     * @return
     */
    public String deleteRepository() {
        try {
            repositoryManager.deleteRepositorys(repositoryIds);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public RepositoryInfo getRepository() {
        return repository;
    }

    public void setRepository(RepositoryInfo repository) {
        this.repository = repository;
    }

    public List<RepositoryInfo> getRepositoryList() {
        return repositoryList;
    }

    public void setRepositoryIds(List<Long> repositoryIds) {
        this.repositoryIds = repositoryIds;
    }

    public void setIsNeed(Boolean isNeed) {
        this.isNeed = isNeed;
    }

    public void setSellerIsOnline(String sellerIsOnline) {
        this.sellerIsOnline = sellerIsOnline;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }
}
