package com.wzitech.gamegold.facade.backend.action.log;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.extjs.AbstractFileUploadAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.IUserModifiActionLogManager;
import com.wzitech.gamegold.shorder.entity.UserModifiActionLog;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jhlcitadmin on 2017/3/10.
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class UserLogAction extends AbstractAction {

    private static final long serialVersionUID = 7158706093540959280L;

    private UserModifiActionLog userModifilog;

    private List<UserModifiActionLog> userLoginList;

    @Autowired
    IUserModifiActionLogManager userModifiActionLogManager;



    public UserModifiActionLog getUserModifilog() {
        return userModifilog;
    }

    public void setUserModifilog(UserModifiActionLog userModifilog) {
        this.userModifilog = userModifilog;
    }

    public List<UserModifiActionLog> getUserLoginList() {
        return userLoginList;
    }

    public void setUserLoginList(List<UserModifiActionLog> userLoginList) {
        this.userLoginList = userLoginList;
    }

    //查询日志表信息
    public String queryLog(){
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (userModifilog != null) {
            queryMap.put("userAccount", userModifilog.getUserAccount());
        }
       /*修改日志显示的顺序，按时间从最近的时间到最早的时间*/
        GenericPage<UserModifiActionLog> genericPage = userModifiActionLogManager.queryUser(queryMap, this.limit, this.start, "update_time", false);
        //  GenericPage<UserModifiActionLog> genericPage = userModifiActionLogManager.queryUser(queryMap, this.limit, this.start, "id", true);
        userLoginList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }
}

