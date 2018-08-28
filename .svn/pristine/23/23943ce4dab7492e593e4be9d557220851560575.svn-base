package com.wzitech.gamegold.jsrobot.interceptor;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import javax.xml.bind.Marshaller;

/**
 * 模    块：FormattedJAXBInterceptor
 * 包    名：com.wzitech.gamegold.facade.interceptor
 * 项目名称：dada
 * 作    者：Shawn
 * 创建时间：2/26/14 6:22 PM
 * 描    述：
 * 更新纪录：1. Shawn 创建于 2/26/14 6:22 PM
 */
public class FormattedJAXBInterceptor extends AbstractPhaseInterceptor<Message> {
    public FormattedJAXBInterceptor() {
        super(Phase.PRE_STREAM);
    }

    @Override
    public void handleMessage(Message message) {
        message.put(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    }

    @Override
    public void handleFault(Message message) {
        message.put(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    }
}
