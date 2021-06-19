package com.huiyuenet.faceCheck;

/**
 * 人脸检测信息结构体，包含人脸矩形框、人脸关键点等信息
 */
public class THFI_FacePos {
    public THFI_FacePos() {
        rcFace = new RECT();
        ptLeftEye = new POINT();
        ptRightEye = new POINT();
        ptMouth = new POINT();
        ptNose = new POINT();
        fAngle = new FaceAngle();
        pFacialData = new byte[512];
    }

    public RECT rcFace;     //coordinate of face
    public POINT ptLeftEye; //coordinate of left eye
    public POINT ptRightEye;    //coordinate of right eye
    public POINT ptMouth;   //coordinate of mouth
    public POINT ptNose;    //coordinate of nose
    public FaceAngle fAngle;    //value of face angle
    public int nQuality;    //quality of face(from 0 to 100)
    public byte[] pFacialData;  //facial data

    public void copyFrom(THFI_FacePos facePos){
        this.rcFace.copyFrom(facePos.rcFace);

        this.ptLeftEye.copyFrom(facePos.ptLeftEye);
        this.ptRightEye.copyFrom(facePos.ptRightEye);
        this.ptMouth.copyFrom(facePos.ptMouth);
        this.ptNose.copyFrom(facePos.ptNose);

        this.fAngle.copyFrom(facePos.fAngle);

        this.nQuality = facePos.nQuality;

        System.arraycopy(facePos.pFacialData, 0, this.pFacialData, 0, this.pFacialData.length);
    }
}
