package com.leessy.coolkotlin;

import com.leessy.App;
import com.leessy.KotlinExtension.ViewKtKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * @author Created by 刘承. on 2019/7/25
 * <p>
 * --深圳市尚美欣辰科技有限公司.
 */
public class JavaClassTest {
    private static final JavaClassTest ourInstance = new JavaClassTest();

    public static JavaClassTest getInstance() {
        return ourInstance;
    }

    private JavaClassTest() {
    }


    private void TestKTApp() {
        App app = null;
        ViewKtKt.getapp2(app, new Function1<App, Unit>() {
            @Override
            public Unit invoke(App app) {
                return null;
            }
        }, new Function1<String, Unit>() {
            @Override
            public Unit invoke(String s) {


                return null;
            }
        });

    }
}
