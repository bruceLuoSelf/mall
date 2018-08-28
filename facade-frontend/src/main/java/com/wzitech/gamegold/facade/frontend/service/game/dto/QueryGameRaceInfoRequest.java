package com.wzitech.gamegold.facade.frontend.service.game.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 *
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/14  wubiao           ZW_C_JB_00008 商城增加通货
 */
public class QueryGameRaceInfoRequest extends AbstractServiceRequest {

    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
