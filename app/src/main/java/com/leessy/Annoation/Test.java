package com.leessy.Annoation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Created by 刘承. on 2019/8/1
 * <p>
 * --深圳市尚美欣辰科技有限公司.
 */
public class Test {
    @MyTest(name = "face", index = 0)
//    byte[] bytes = new byte[11];
//    private byte[] bytes = {1, 3, 13};
    private byte[] bytes = null;

    public static void main(String[] args) {
//        AnnoationUtils.getInfo(MyStudent.class);


        spi();

        //获取Bean类所有公共成员变量
//        Test test = new Test();
//        Field[] fields = test.getClass().getDeclaredFields();
//
//        //遍历Bean类所有公共成员变量
//        for (Field field : fields) {
//            //判断成员变量的注解
//            if (field.isAnnotationPresent(MyTest.class)) {
//                //获取成员变量的注解
//                MyTest myTarget = field.getAnnotation(MyTest.class);
//                System.out.println(myTarget.name());
//                System.out.println(myTarget);
//                System.out.println(field.getName());
//                try {
//                    byte[] bb = (byte[]) field.get(test);
//                    System.out.println(Arrays.toString(bb));
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
    }

    static void spi() {
        String s = "A8:15:4D:5F:BD:36|00:08:22:29:11:4F|02|04|1|-94";
        String[] strings = s.split("");
        System.out.println(strings.length);
        for (String string : strings) {
            System.out.println(string);
        }
    }

}
