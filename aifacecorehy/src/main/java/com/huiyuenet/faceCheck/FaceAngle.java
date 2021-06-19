package com.huiyuenet.faceCheck;

public class FaceAngle {
	public int   yaw;//angle of yaw,from -90 to +90,left is negative,right is postive
	public int   pitch;//angle of pitch,from -90 to +90,up is negative,down is postive
	public int   roll;//angle of roll,from -90 to +90,left is negative,right is postive
	public float confidence;//confidence of face pose(from 0 to 1,0.6 is suggested threshold)

    public void copyFrom(FaceAngle faceAngle){
        this.yaw = faceAngle.yaw;
        this.pitch = faceAngle.pitch;
        this.roll = faceAngle.roll;
        this.confidence = faceAngle.confidence;
    }
}
