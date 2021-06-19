package com.huiyuenet.faceCheck;

/**
 * 人脸质量检测配置类
 */
public class THFQ_Param {
    public int brightness_min;  // 0-100，太暗阈值,建议25.越小越可能把过暗的人脸当作正常人脸，此参数会影响THFQ_Result::brightness
    public int brightness_max;  // 0-100，太亮阈值,建议75.越大越可能把的过亮人脸当作正常人脸，此参数会影响THFQ_Result::brightness
}
