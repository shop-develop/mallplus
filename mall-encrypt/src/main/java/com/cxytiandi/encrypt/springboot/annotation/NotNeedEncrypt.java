package com.cxytiandi.encrypt.springboot.annotation;

import java.lang.annotation.*;

/**
 指定某个方法不需要加密
 * 
 * @author zscat
 *
 * @about 2019-04-30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotNeedEncrypt {
    String value() default "";
}
