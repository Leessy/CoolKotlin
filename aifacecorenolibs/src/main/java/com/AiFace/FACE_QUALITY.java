package com.AiFace;

// FACE_QUALITY 结构, 存放人脸质量，包括口罩遮挡等属性
public class FACE_QUALITY {
	public int nMask; // 人脸遮挡度(值越大表示人脸遮挡程度越高): 判别是否戴口罩的建议阈值为 30
	public int nHat; // 是否戴帽(值越大表示越可能戴着帽子)，建议阈值为 50
	public int nGlasses; // 是否戴眼镜(值越大表示越可能戴着眼镜)，建议阈值为 70
	public int nBrightLevel; // 人脸亮度级别：-1->太暗，0->正常，1->太亮
	public int nBlur; //人脸模糊度(值越大表示图像越模糊)，建议阈值为 70
}
