package com.wzitech.gamegold.facade.backend.action.goodsmgmt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.repository.business.ITransFileManager;
import com.wzitech.gamegold.repository.dao.ITransFileDBDAO;
import com.wzitech.gamegold.repository.entity.TransferFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

/**
 * Created by wangmin
 * Date:2017/7/19
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class TransferFileAcction extends AbstractAction {
    @Autowired
    ITransFileManager transFileManager;
    @Autowired
    ITransFileDBDAO transFileDBDAO;

    private Long id;

    private String gameName;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getJsonString() {

        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    private String jsonString;

    List<TransferFile> transferFileList;

    public String updateTransferFile() {
        try {
            if (id == null ){
                throw new SystemException("该配置id为空，不能修改（可查看是否选中）");
            }
            if (StringUtils.isBlank(gameName)){
                throw new SystemException("该配置游戏名为空，不能修改（可查看是否选中）");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(jsonString);
            transFileManager.modifyTransFile(id, jsonString, gameName);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e.getMessage());
        } catch (JsonProcessingException e) {
            return this.returnError("格式有误,请重新输入");
        } catch (IOException e) {
           return this.returnError(e.getMessage());
        }
    }

    /*public String selectTransferFile(){
           try {
               Map<String, Object> queryMap = new HashMap<String, Object>();
              GenericPage<TransferFile> genericPage = transFileManager.selectTransFileInfo(queryMap,this.limit,this.start, "id", false);
               transferFileList=genericPage.getData();
               totalCount=genericPage.getTotalCount();
               return returnSuccess();
           }catch (SystemException e){
               return this.returnError(e);
           }
       }*/
    public String selectTransferFile() {
        try {
            transferFileList = transFileManager.selectAllInfo(gameName, id);
            return returnSuccess();
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

    public List<TransferFile> getTransferFileList() {
        return transferFileList;
    }

    public void setTransferFileList(List<TransferFile> transferFileList) {
        this.transferFileList = transferFileList;
    }
}
