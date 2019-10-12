package com.leessy.Annoation;

import com.leessy.CRC;
import com.leessy.CRC16Util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;

/**
 * @author Created by 刘承. on 2019/8/1
 * <p>
 * --深圳市尚美欣辰科技有限公司.
 */
public class Test {
    @MyTest(name = "face", index = 0)
//    byte[] bytes = new byte[11];
    private byte[] bytenames = {1, 3, 13};
//    private byte[] bytes = null;

    /**
     * 将int转换成byte数组，低位在前，高位在后
     * 改变高低位顺序只需调换数组序号
     */
    private static byte[] intToBytes(int value) {
        byte[] src = new byte[2];
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    public static byte[] shortToByteArray(short value) {
//        byte[] targets = new byte[2];
//        for (int i = 0; i < 2; i++) {
//            int offset = (targets.length - 1 - i) * 8;
//            targets[i] = (byte) ((s >>> offset) & 0xff);
//        }

        byte[] src = new byte[2];
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    private static byte[] bytescrc = {
            0x01, 0x03, 0x14, 0x00, 0x03, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            (byte) 0xe7, 0x23
    };

    public static boolean isHttpUrl(String urls) {
        boolean isurl = false;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式
        Pattern pat = Pattern.compile(regex.trim());//比对
        Matcher mat = pat.matcher(urls.trim());
        isurl = mat.matches();//判断是否匹配
        return isurl;
    }

    //01 03 14 00 03 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
    public static void main(String[] args) {

        Long ll = 1858558321L;
        System.out.println("---***** " + (43111 / 10000));

        byte[] b = CRC16Util.getCrc16(bytescrc, bytescrc.length - 2);
//        byte[] b = intToBytes(CRC.crc16_modbus(bytescrc, 0, (bytescrc.length - 2)));
        System.out.println("---" + Arrays.toString(b));
        System.out.println("---" + byteToHex(b));

        int b2 = CRC16Util.getCrc16Int(bytescrc, bytescrc.length - 2);
        System.out.println("---2 " + b2);


        short s = CRC.crc16_modbus(bytescrc, 0, (bytescrc.length - 2));
        System.out.println("---3 " + byteToHex(shortToByteArray(s)));


//        AnnoationUtils.getInfo(MyStudent.class);


//        获取Bean类所有公共成员变量
        Test test = new Test();
        Field[] fields = test.getClass().getDeclaredFields();

        //遍历Bean类所有公共成员变量
        for (Field field : fields) {
            //判断成员变量的注解
            if (field.isAnnotationPresent(MyTest.class)) {
                //获取成员变量的注解
                MyTest myTarget = field.getAnnotation(MyTest.class);
                System.out.println(myTarget.name());
                System.out.println(myTarget);
                System.out.println(field.getName());
                try {
                    byte[] bb = (byte[]) field.get(test);
                    System.out.println(Arrays.toString(bb));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }

//        spi();

//        testSchedulers();
    }

    static void testSchedulers() {
        Observable.range(0, 500000000)
                .concatMap(i -> {
                    long delay = Math.round(Math.random() * 2);

                    return Observable.timer(delay, TimeUnit.SECONDS).map(n -> i);
                })
                .blockingSubscribe(System.out::print);


//**********************************************************************************************//
//        Observable<Integer> o1 = Observable.range(5, 5);
//        Observable<Integer> o2 = Observable.range(1, 4).map(new Function<Integer, Integer>() {
//            @Override
//            public Integer apply(Integer integer) throws Exception {
//                if (integer == 3) {
//                    throw new Exception("测试发送异常");
//                }
//                return integer;
//            }
//        });
//
//        Observable.merge(o2, o1)
//                .observeOn(Schedulers.computation(), true)
//                .concatMapDelayError(new Function<Integer, ObservableSource<?>>() {
//                    @Override
//                    public ObservableSource<?> apply(Integer integer) throws Exception {
//                        return Observable.just(integer);
//                    }
//                })
//
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        System.out.println("-----" + o);
////                        Thread.sleep(000);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        System.out.println("-----" + throwable);
//                    }
//                });


        //**********************************************************************************************//
//        Observable.range(1, 100)
////        Observable.intervalRange(1, 1000, 1, 1, TimeUnit.SECONDS)
//                .map(new Function<Integer, Object>() {
//                    @Override
//                    public Object apply(Integer integer) throws Exception {
//                        if (integer == 5) {
//                            throw new Exception("测试发送异常");
//                        }
//                        return integer + "";
//                    }
//                })
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(Schedulers.computation(), true)
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        System.out.println("-----" + o);
//                        Thread.sleep(6000);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        System.out.println("-----" + throwable);
//                    }
//                });

        try {
            Thread.sleep(1000 * 30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void spi() {
        String s = "A8:15:4D:5F:BD:36|00:08:22:29:11:4F|02|04|1|-94";
        String[] strings = s.split("");
        System.out.println(strings.length);
        for (String string : strings) {
            System.out.println(string);
        }
    }

    /**
     * byte数组转hex
     *
     * @param bytes
     * @return
     */
    public static String byteToHex(byte[] bytes) {
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < bytes.length; n++) {
            strHex = Integer.toHexString(bytes[n] & 0xFF);
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
        }
        return sb.toString().trim();
    }
}
