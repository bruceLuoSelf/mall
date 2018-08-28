package com.wzitech.gamegold.facade.frontend.service.shorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;

import javax.ws.rs.QueryParam;

/**
 * Created by Administrator on 2017/1/6.
 */
public interface IShGameConfigService {
    IServiceResponse getConfigByGameName(@QueryParam("gameName")  String gameName);
    IServiceResponse getConfigForALL();

    /**
     * 根据游戏名称和开关查询游戏类目配置
     * @param gameName 游戏名称，不能为空
     * @param isEnable 收货开关，可以为空
     * @param  enableMall 商城开关，可以为空
     * @return
     */
    IServiceResponse getAllConfigByGameName(@QueryParam("gameName")String gameName,@QueryParam("isEnable")Boolean isEnable,@QueryParam("enableMall")Boolean enableMall);

    /**
     * 新增前端接口--根据游戏名称和商品类型查询最低购买金额
     *ADD_20170609_通货优化
     * @param gameName
     * @param goodsTypeName
     * @return
     */
    IServiceResponse getGameConfig(@QueryParam("gameName") String gameName,@QueryParam("goodsTypeName") String goodsTypeName);

    /**
     * 获取所有的配置游戏
     * @param goodsTypeName
     * @return
     */
    IServiceResponse getGameList(@QueryParam("goodsTypeName") String goodsTypeName);

    /**
     * 查询收货商的库存上限配置
     * @param gameName
     * @param goodsTypeName
     * @return
     */
    IServiceResponse queryRepositoryLimit(@QueryParam("gameName") String gameName, @QueryParam("goodsTypeName") String goodsTypeName);

    /**
     * 修改收货商库存限制
     * @param isSplit
     * @param repositoryCount
     * @param needCount
     * @return
     */
    IServiceResponse updateRepositoryLimit(Boolean isSplit, Long repositoryCount, Long needCount);
}
