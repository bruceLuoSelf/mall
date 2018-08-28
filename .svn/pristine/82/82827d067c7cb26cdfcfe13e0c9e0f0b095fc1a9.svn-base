package com.wzitech.gamegold.facade.frontend.service.game;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.game.dto.QueryWarningRequest;

import javax.ws.rs.QueryParam;

/**
 * 查询商品类型接口
 *  Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/12  lvshuyan           ZW_C_JB_00008 商城增加通货
 */
public interface IGameCategoryConfigService {

	/**
	 * 查询所有启用的商品类型
	 * @return
	 */
	public IServiceResponse queryAllGameCategory();


	/**
	 * 根据游戏名、商品类型查询友情提示
	 * @return
	 */
	public IServiceResponse queryWarning(@QueryParam("")QueryWarningRequest queryWarningRequest);
}
