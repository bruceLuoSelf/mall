package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.IQqOnLineManager;
import com.wzitech.gamegold.shorder.entity.QqOnLineEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;

/**
 * 客服QQ在线
 * Created by Administrator on 2017/1/11.
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class QqOnLineAction  extends AbstractAction {
    private QqOnLineEO qqOnLineEO;
    private Long id;
    private String qqNumber;
    private List<QqOnLineEO> qqOnLineEOList;
    @Autowired
    IQqOnLineManager qqOnLineManager;
    /**
     * 查询所有QQ
     */
    public String queryQqOnLine()  {
        try {
            HashMap<String, Object> paramMap = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(qqNumber)) {
                paramMap.put("qqNumber", qqNumber);
            }
            GenericPage<QqOnLineEO> genericPage = qqOnLineManager.query(paramMap, this.limit, this.start, "id", false);
            qqOnLineEOList = genericPage.getData();
            totalCount = genericPage.getTotalCount();
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    };
    /**
     * 新增
     */
    public String addQqOnLine(){
        try {
            qqOnLineManager.addQqOnLineEO(qqOnLineEO);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }
    /**
     * 删除
     *
     * @return
     */
    public String deleteQqOnLine() {
        try {
            qqOnLineManager.deleteQqOnLineEO(id);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }
    /**
     * 修改配置
     *
     * @return
     */
    public String updateQqOnLine() {
        try {
            qqOnLineManager.updateQqOnLineEO(qqOnLineEO);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }
    /**
     * 启用
     * @return
     */
    public String enabledQqOnLine() {
        try {
            qqOnLineManager.enabled(id);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 禁用
     * @return
     */
    public String disabledQqOnLine() {
        try {
            qqOnLineManager.disabled(id);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public QqOnLineEO getQqOnLineEO() {
        return qqOnLineEO;
    }

    public void setQqOnLineEO(QqOnLineEO qqOnLineEO) {
        this.qqOnLineEO = qqOnLineEO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQqNumber() {
        return qqNumber;
    }

    public void setQqNumber(String qqNumber) {
        this.qqNumber = qqNumber;
    }

    public List<QqOnLineEO> getQqOnLineEOList() {
        return qqOnLineEOList;
    }

    public void setQqOnLineEOList(List<QqOnLineEO> qqOnLineEOList) {
        this.qqOnLineEOList = qqOnLineEOList;
    }
}
