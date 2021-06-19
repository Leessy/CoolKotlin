package com.huiyuenet.faceCheck;

/**
 * 人脸属性检测结果类
 */
public class THFP_Result {

    public int gender;  // 1-male,0-female
    public int age;     // range[0-100]
    public int sunglasses;  // 1-sunglasses,0-no sunglasses
    public int glasses; // 1-glasses,0-no glasses
    public int mask;    // 1-mask,0-no mask

    @Override
    public String toString() {
        return "THFP_Result{" +
                "gender=" + gender +
                ", age=" + age +
                ", sunglasses=" + sunglasses +
                ", glasses=" + glasses +
                ", mask=" + mask +
                '}';
    }
}
