package com.leessy.Annoation;

import java.lang.annotation.*;

/**
 * @author Created by 刘承. on 2019/8/1
 * <p>
 * --深圳市尚美欣辰科技有限公司.
 */
//自定义注解
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TTT {
}
