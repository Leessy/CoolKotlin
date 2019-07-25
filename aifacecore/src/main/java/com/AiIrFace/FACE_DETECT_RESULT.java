package com.AiIrFace;

// FACE_DETECT_RESULT类, 存放检测到的人脸参数
public class FACE_DETECT_RESULT {
    public int nFaceLeft, nFaceTop, nFaceRight, nFaceBottom; // 人脸坐标
    public int nLeftEyeX, nLeftEyeY, nRightEyeX, nRightEyeY; // 左右眼坐标
    public int nMouthX, nMouthY, nNoseX, nNoseY; // 嘴巴鼻子坐标
    public int nAngleYaw, nAnglePitch, nAngleRoll; // 人脸偏转角度（左右，上下，水平方向）
    public int nQuality; // 人脸质量
    public int nBrightAvg; // 人脸区域平均亮度
    public byte[] FaceData; // 脸部详细参数，可据此判断睁闭眼张嘴微笑等姿状，特征提取需要此数据确认脸部轮廓，所以提特征时需要将人脸检测的结果原样输入

    public FACE_DETECT_RESULT() {
        FaceData = new byte[512];
    } // 定义对象时自动分配空间
}
