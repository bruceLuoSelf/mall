package com.wzitech.gamegold.facade.backend.business.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.xxl.job.core.log.XxlJobLogger;



/**
 * Created by 340082 on 2018/5/30.
 */
public class XxlJobLogBackAppender extends AppenderBase<ILoggingEvent> {
    @Override
    protected void append(ILoggingEvent event) {
        XxlJobLogger.log(event.getFormattedMessage());
    }
}
