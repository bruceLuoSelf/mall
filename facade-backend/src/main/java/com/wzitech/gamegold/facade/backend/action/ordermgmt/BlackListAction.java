package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.shorder.business.IBlackListManager;
import com.wzitech.gamegold.shorder.entity.BlackListEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 340032 on 2017/12/25.
 */

/**
 * 黑名单管理
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class BlackListAction extends AbstractAction {
    private BlackListEO blackListeo;

    private Long id;

    private List<BlackListEO> blackListList;

    private Date startTime;
    private Date endTime;

    private Long totalCount;



    @Autowired
    IBlackListManager blackListManager;


    /**
     * 查询用户列表
     * @return
     */
    public String queryBlackList() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(blackListeo.getLoginAccount())) {
            queryMap.put("loginAccount", blackListeo.getLoginAccount().trim());
        }
            queryMap.put("startTime", startTime);
            queryMap.put("endTime", WebServerUtil.oneDateLastTime(endTime));
        GenericPage<BlackListEO> genericPage = blackListManager.queryUser(queryMap, this.limit, this.start, "ID", true);
        blackListList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }



    /**
     * 新增后台管理用户
     * @return
     * @throws IOException
     */
    public String addUser1() throws IOException {
        try {
            blackListManager.addUser1(blackListeo);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 启用用户
     * @return
     */
    public String enableUser1() {
        try {
            blackListManager.enableUser1(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public BlackListEO getBlackListeo() {
        return blackListeo;
    }

    public void setBlackListeo(BlackListEO blackListeo) {
        this.blackListeo = blackListeo;
    }

    @Override
    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 禁用用户
     * @return
     */
    public String disableUser1() {
        try {
            blackListManager.disableUser1(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }


    /**
     * 删除用户
     * @return
     */
    public String deleteUser1() {
        try {
            blackListManager.deleteUser1(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BlackListEO> getBlackListList() {
        return blackListList;
    }

    public void setBlackListList(List<BlackListEO> blackListList) {
        this.blackListList = blackListList;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
