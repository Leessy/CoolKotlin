package com.leessy.Annoation;

import java.lang.annotation.*;

/**
 * @author Created by 刘承. on 2019/8/1
 * <p>
 * --深圳市尚美欣辰科技有限公司.
 */
//自定义注解
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyTest {
    //String为返回值类型，methodName为方法名，default表示使用注解时未指定参数
//    String methodName() default "no method to set";
    String name();

    int index() default 1;
}
