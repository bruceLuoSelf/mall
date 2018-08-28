package com.wzitech.gamegold.common.paymgmt.dto.jsondeserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * *********************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p/>
 * 包	名：  com.wzitech.gamegold.common.paymgmt.dto.jsondeserial
 * 项目名称:   gamegold
 * 作	者：  SunChengfei
 * 创建时间:   2014/5/29
 * 描	述：
 * 更新纪录:   1. SunChengfei 创建于 2014/5/29 14:16
 * <p/>
 * **********************************************************************************
 */
public class ResultJsonDeserializer extends JsonDeserializer<Boolean> {

    @Override
    public Boolean deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        String parserString = parser.getText();
        if(StringUtils.equals(parserString, "true")){
            return true;
        }
        return false;
    }


}
