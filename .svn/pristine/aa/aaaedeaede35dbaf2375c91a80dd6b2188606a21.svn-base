package com.wzitech.gamegold.common.context;

import org.springframework.core.NamedThreadLocal;

/**
 * 当前线程IP信息
 */
public class CurrentIpContext {
    private static final ThreadLocal<String> currentThreadId = new NamedThreadLocal<String>("jgm-currentip");

    public static String getIp() {
        return currentThreadId.get();
    }

    public static void setIp(String ip) {
        currentThreadId.set(ip);
    }
}
