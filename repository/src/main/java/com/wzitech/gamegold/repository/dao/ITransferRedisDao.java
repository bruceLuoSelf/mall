package com.wzitech.gamegold.repository.dao;

import com.wzitech.gamegold.repository.entity.TransferFile;

import java.util.Map;


/**
* Update History
* Date        Name                Reason for change
* ----------  ----------------    ----------------------
* 2017/07/20  wangmin           ZW_J_JB_00073 金币商城增加区服互通功能
*/
public interface ITransferRedisDao {

  //删除
  void remove();

  //添加
  void save(TransferFile transferFile);

  //查询
  String getJsonString(String gameName);

  //查询全部
  Map<String,String> getJsonString();
}
