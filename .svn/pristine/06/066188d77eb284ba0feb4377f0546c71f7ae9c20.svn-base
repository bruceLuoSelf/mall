package com.wzitech.gamegold.rc8.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 模    块：DateSerializer
 * 包    名：com.wzitech.gamegold.facade.serializer
 * 项目名称：dada
 * 作    者：Shawn
 * 创建时间：2/26/14 4:49 PM
 * 描    述：
 * 更新纪录：1. Shawn 创建于 2/26/14 4:49 PM
 */
public class DateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formattedDate = formatter.format(date);
        jsonGenerator.writeString(formattedDate);
    }
}
