package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.shorder.business.IGameAccountManager;
import com.wzitech.gamegold.shorder.entity.GameAccount;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * 收货角色管理
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class ShGameAccountAction extends AbstractAction{

    @Autowired
    IGameAccountManager gameAccountManager;
    private GameAccount shGameAccount;
    private Date startTime;
    private Date endTime;
    private List<GameAccount> gameAccountList;

    /**
     * 查询收货角色列表
     * @return
     */
    public String queryShGameAccount(){
        Map<String,Object> map=new HashMap<String,Object>();
        if(shGameAccount!=null){
            if(StringUtils.isNotBlank(shGameAccount.getBuyerAccount())){
                map.put("buyerAccount",shGameAccount.getBuyerAccount().trim());
            }
            if(StringUtils.isNotBlank(shGameAccount.getBuyerUid())){
                map.put("buyerUid",shGameAccount.getBuyerUid().trim());
            };
            if(StringUtils.isNotBlank(shGameAccount.getGameName())){
                map.put("gameName",shGameAccount.getGameName().trim());
            }
            if(StringUtils.isNotBlank(shGameAccount.getRegion())){
                map.put("region",shGameAccount.getRegion().trim());
            }
            if(StringUtils.isNotBlank(shGameAccount.getServer())){
                map.put("server", shGameAccount.getServer().trim());
            }
            if(StringUtils.isNotBlank(shGameAccount.getGameRace())){
                map.put("gameRace",shGameAccount.getGameRace().trim());
            }
            if(StringUtils.isNotBlank(shGameAccount.getGameAccount())){
                map.put("gameAccount",shGameAccount.getGameAccount().trim());
            }
            if(StringUtils.isNotBlank(shGameAccount.getRoleName())){
                map.put("roleName",shGameAccount.getRoleName().trim());
            }
            if(shGameAccount.getStatus()!=null && shGameAccount.getStatus()!=0){
                map.put("status",shGameAccount.getStatus());
            }
        }
        map.put("createStartTime",startTime);
        map.put("createEndTime", WebServerUtil.oneDateLastTime(endTime));
        List<SortField> sortList = new ArrayList<SortField>();
        SortField field1 = new SortField("game_name", SortField.ASC);
        SortField field2 = new SortField("region", SortField.ASC);
        SortField field3 = new SortField("server", SortField.ASC);
        SortField field4 = new SortField("game_race", SortField.ASC);
        SortField field5 = new SortField("level", SortField.ASC);
        sortList.add(field1);
        sortList.add(field2);
        sortList.add(field3);
        sortList.add(field4);
        sortList.add(field5);
        GenericPage<GameAccount> page=gameAccountManager.queryListInPage(map,this.start,this.limit,sortList);
        gameAccountList=page.getData();

        if(gameAccountList!=null){
            for(GameAccount gameAccount:gameAccountList){
                gameAccount.setGamePwd(null);
                gameAccount.setSecondPwd(null);
            }
        }

        totalCount=page.getTotalCount();
        return this.returnSuccess();
    }

    public GameAccount getShGameAccount() {
        return shGameAccount;
    }

    public void setShGameAccount(GameAccount shGameAccount) {
        this.shGameAccount = shGameAccount;
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

    public List<GameAccount> getGameAccountList() {
        return gameAccountList;
    }

    public void setGameAccountList(List<GameAccount> gameAccountList) {
        this.gameAccountList = gameAccountList;
    }
}
