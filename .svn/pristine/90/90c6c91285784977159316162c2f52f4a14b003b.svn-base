package com.wzitech.gamegold.common.main;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 340082 on 2017/10/24.
 */
@Component
public class MainGerIdUtilEo implements IMainGerIdUtil{

    @Autowired
    private IMainStationManager mainStationManager;

    /**
     * 请求主站查询获取发送 MQ 的id
     * */
    public String getMainId(Boolean isGameGold){
        String jsonParam= "{\"AppName\":\""+ "jbsc\","+
                "\"OrderType\":\"" + (isGameGold ? "GameMoney":"GameGoods") + "\","+
                "\"ClientID\":\"" + "1\"}";
        String result = mainStationManager.GetMainRest(MainStationConstant.URL_REST,MainStationConstant.RESULT_AUTHVERS,MainStationConstant.RESULT_TYPE, MainStationConstant.GET_ID_MAIN,
                jsonParam,MainStationConstant.RESULT_MD5,MainStationConstant.RESULT_FIELDS,MainStationConstant.RESULT_VERSION);
        //转化主站返回来的结果
        JSONObject jsonObject = JSONObject.fromObject(result);
        JSONObject bizResult = jsonObject.getJSONObject("BizResult");
        String realName;
        if (!bizResult.isNullObject()) {
            realName = bizResult.getString("VersionNumber");
            if (realName==null||"null".equals(realName)){
                realName="-1";
            }
        }else {
            realName="-1";
        }
        return realName;
    }
}
