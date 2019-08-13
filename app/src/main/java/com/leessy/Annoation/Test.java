package com.leessy.Annoation;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

//        spi();

        testSchedulers();
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

}
