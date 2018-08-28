package com.wzitech.gamegold.facade.serializer;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 模    块：JaxbDateSerializer
 * 包    名：com.wzitech.gamegold.facade.serializer
 * 项目名称：dada
 * 作    者：Shawn
 * 创建时间：2/27/14 10:48 PM
 * 描    述：
 * 更新纪录：1. Shawn 创建于 2/27/14 10:48 PM
 */
public class JaxbDateSerializer extends XmlAdapter<String, Date> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String marshal(Date date) throws Exception {
        return dateFormat.format(date);
    }

    @Override
    public Date unmarshal(String date) throws Exception {
        return dateFormat.parse(date);
    }
}
