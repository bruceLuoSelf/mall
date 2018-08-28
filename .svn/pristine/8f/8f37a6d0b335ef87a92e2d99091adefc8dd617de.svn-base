package com.wzitech.gamegold.facade.backend.action.repository;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.repository.business.ISellerSettingManager;
import com.wzitech.gamegold.repository.entity.SellerSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
@ExceptionToJSON
public class SellerSettingAction extends AbstractAction {
    private List<SellerSetting> sellerSettingList;

    private SellerSetting sellerSetting;

    private Long id;

    private List<Long> ids;

    private Integer sort;

    @Autowired
    ISellerSettingManager sellerSettingManager;

    /**
     * 查询店铺配置列表
     * @return
     */
    public String querySellerSetting() {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        if(sellerSetting!=null) {
            paramMap.put("loginAccount", sellerSetting.getLoginAccount());
            paramMap.put("gameName", sellerSetting.getGameName());
            paramMap.put("region", sellerSetting.getRegion());
            /** ZW_C_JB_00008_20170513_START	**/
            if (!"全部".equals(sellerSetting.getGoodsTypeName())){
            paramMap.put("goodsTypeName",sellerSetting.getGoodsTypeName());
            }
            /** ZW_C_JB_00008_20170513_END	**/
        }
        GenericPage<SellerSetting> genericPage = sellerSettingManager.querySellerSetting(paramMap, this.limit, this.start, "GAME_NAME",true);
        sellerSettingList = genericPage.getData();
        totalCount = genericPage.getTotalCount();

        return this.returnSuccess();
    }

    /**
     * 新增店铺配置
     * @return
     * @throws IOException
     */
    public String addSellerSetting() throws IOException {
        try {
            Boolean flag=sellerSettingManager.addSellerSetting(sellerSetting);

            String message=flag?"新增成功":"该卖家在该游戏下已存在配置";
            return this.returnSuccess(message);
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 修改店铺配置
     * @return
     * @throws IOException
     */
    public String modifySellerSetting() throws IOException {
        try {
            sellerSetting.setId(id);
            Boolean flag= sellerSettingManager.modifySellerSetting(sellerSetting);
            String message=flag?"修改成功":"该卖家在该游戏下已存在配置";
            return this.returnSuccess(message);
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 删除店家配置
     * @return
     */
    public String deleteSellerSetting() {
        try {
            sellerSettingManager.deleteSellerSetting(ids);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 启用和禁用店家配置
     * @return
     */
    public String settingEnable() {
        try {
            sellerSettingManager.settingEnable(ids, sellerSetting.getIsDeleted());
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 设置卖家店铺排序
     * @return
     */
    public String updateShopSort() {
        if (sort == null)
            sort = 0;
        try {
            sellerSettingManager.updateSort(id, sort);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public List<SellerSetting> getSellerSettingList() {
        return sellerSettingList;
    }

    public void setSellerSettingList(List<SellerSetting> sellerSettingList) {
        this.sellerSettingList = sellerSettingList;
    }

    public void setSellerSetting(SellerSetting sellerSetting) {
        this.sellerSetting = sellerSetting;
    }

    public SellerSetting getSellerSetting() {
        return sellerSetting;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
