package com.wzitech.gamegold.facade.frontend.accessLimit;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * Created by 汪俊杰 on 2018/4/19.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface AccessLimit {
    //时间段，单位为秒
    int time() default 30;

    //该时间段里的访问次数
    int maxCount() default Integer.MAX_VALUE;

    //是否需要登录，默认需要
    boolean isNeedLogin() default true;

    //是否取配置值
    boolean isConfig() default true;
}
