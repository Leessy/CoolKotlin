package com.huiyuenet.faceCheck;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * {@link FaceCheck} 中部分接口的封装类，为了便于应用调用
 */
public class FaceFunction {
    private static String TAG = "FaceFunction";
    public static final String PATH = Environment.getExternalStorageDirectory().toString() + "/FeaturePath/";

    /**
     * 人脸特征点建模存放路径
     */
    public static final String savePath = PATH;

    /**
     * 单目人脸活体检测接口（建议使用该接口代替双目活体检测接口）
     *
     * @param pixelsBGR RGB图像BGR数据
     * @param facePos   人脸图像对应的人脸信息结构体，结构体信息参考 {@link THFI_FacePos} 类
     * @param width     图像宽度
     * @param height    图像高度
     * @return 人脸图像活体分数值
     */
    public synchronized static float faceLiveCheckBGR(byte[] pixelsBGR, THFI_FacePos[] facePos, int width, int height) {
        float[] liveScore = new float[2];

        if (pixelsBGR == null) {
            return -1;
        }

        //根据检测到的人脸，判断角度和人脸坐标准确度
        THFI_FacePos pos = facePos[0];

        if (pos.fAngle.confidence < THFI_Param.CONFIDENCE_THRESHOLD || Math.abs(pos.fAngle.pitch) > THFI_Param.FACE_VALID_ANGLE
                || Math.abs(pos.fAngle.roll) > THFI_Param.FACE_VALID_ANGLE) {
            Log.d(TAG, "人脸定位准确度不足或人脸角度大于" + THFI_Param.FACE_VALID_ANGLE);
            return -2;
        }

        int detectResult = FaceCheck.DetectLive2(pixelsBGR, null, facePos, null, width, height, 50, liveScore);
        if (detectResult < 0) {
            Log.e(TAG, "人脸活体检测失败");
        }

        return liveScore[0];
    }

    /**
     * 单目人脸活体检测接口（建议使用该接口代替双目活体检测接口）
     *
     * @param pixelsIR IR图像BGR数据
     * @param facePos  人脸图像对应的人脸信息结构体，结构体信息参考 {@link THFI_FacePos} 类
     * @param width    图像宽度
     * @param height   图像高度
     * @return 人脸图像活体分数值
     */
    public synchronized static float faceLiveCheckIR(byte[] pixelsIR, THFI_FacePos[] facePos, int width, int height) {
        float[] liveScore = new float[2];

        if (pixelsIR == null) {
            return -1;
        }

        //根据检测到的人脸，判断角度和人脸坐标准确度
        THFI_FacePos pos = facePos[0];

        if (pos.fAngle.confidence < THFI_Param.CONFIDENCE_THRESHOLD || Math.abs(pos.fAngle.pitch) > THFI_Param.FACE_VALID_ANGLE
                || Math.abs(pos.fAngle.roll) > THFI_Param.FACE_VALID_ANGLE) {
            Log.d(TAG, "人脸定位准确度不足或人脸角度大于" + THFI_Param.FACE_VALID_ANGLE);
            return -2;
        }

        int detectResult = FaceCheck.DetectLive2(null, pixelsIR, null, facePos, width, height, 50, liveScore);
        if (detectResult < 0) {
            Log.e(TAG, "人脸活体检测失败");
        }

        return liveScore[0];
    }

    /**
     * @param pixelsBGR  彩色图像BGR数据
     * @param pixelsIR   红外图像BGR数据
     * @param width      图像宽度
     * @param height     图像高度
     * @param threshold  活体检测阈值
     * @param facePosRgb 彩色人脸图像对应的人脸信息结构体，结构体信息参考 {@link THFI_FacePos} 类
     * @param facePosIr  红外人脸图像对应的人脸信息结构体，结构体信息参考 {@link THFI_FacePos} 类
     * @return 人脸图像活体分数值
     * @deprecated 双目人脸活体检测接口
     * （建议使用单目活体检测接口
     * {@link FaceFunction#faceLiveCheckBGR(byte[], THFI_FacePos[], int, int)}、
     * {@link FaceFunction#faceLiveCheckIR(byte[], THFI_FacePos[], int, int)}
     * 代替）
     */
    public synchronized static float[] faceLiveCheck2(byte[] pixelsBGR, byte[] pixelsIR, int width, int height, int threshold, THFI_FacePos[] facePosRgb, THFI_FacePos[] facePosIr) {
        float[] liveScore = new float[2];

        if (pixelsBGR == null && pixelsIR == null) {
            return liveScore;
        }

        //根据检测到的人脸，判断角度和人脸坐标准确度
        THFI_FacePos pos = facePosRgb[0];

        if (pos.fAngle.confidence < THFI_Param.CONFIDENCE_THRESHOLD || Math.abs(pos.fAngle.pitch) > THFI_Param.FACE_VALID_ANGLE
                || Math.abs(pos.fAngle.roll) > THFI_Param.FACE_VALID_ANGLE) {
            Log.d(TAG, "人脸定位准确度不足或人脸角度大于" + THFI_Param.FACE_VALID_ANGLE);
            return liveScore;
        }

        int detectResult = FaceCheck.DetectLive2(pixelsBGR, pixelsIR, facePosRgb, facePosIr, width, height, threshold, liveScore);
        if (detectResult < 0) {
            Log.e(TAG, "人脸活体检测失败");
        }

        return liveScore;
    }


    /**
     * <p> 人脸质量检测（总检测接口，该接口检测所有支持的质量检测项，耗时较长） </p>
     * 如果需要单独检测某一项，如：检测模糊度，则推荐调用 {@link FaceFunction#faceQualityCheck(FaceCheck.CheckOperation, byte[], int, int, THFI_FacePos[], int[])}）
     *
     * @param pixels     图像BGR数据
     * @param width      图像宽度
     * @param height     图像高度
     * @param facePos    人脸图像对应的人脸信息结构体，结构体信息参考 {@link THFI_FacePos} 类
     * @param faceResult 人脸质量检测结果，具体检测内容参考：{@link THFQ_Result}
     * @return 成功返回0，否则返回<0的数
     */
    public static int faceQualityCheck(byte[] pixels, int width, int height, THFI_FacePos[] facePos, THFQ_Result[] faceResult) {

        int ret = -1;

        if (pixels == null) {
            return ret;
        }

        //根据检测到的人脸，判断角度和人脸坐标准确度
        THFI_FacePos pos = facePos[0];

        if (pos.fAngle.confidence < THFI_Param.CONFIDENCE_THRESHOLD || Math.abs(pos.fAngle.pitch) > THFI_Param.FACE_VALID_ANGLE
                || Math.abs(pos.fAngle.roll) > THFI_Param.FACE_VALID_ANGLE) {
            Log.d(TAG, "人脸定位准确度不足或人脸角度大于" + THFI_Param.FACE_VALID_ANGLE);
            return -2;
        }

        ret = FaceCheck.THFQCheck((short) 0, pixels, 24, width, height, facePos, faceResult);

        return ret;
    }

    /**
     * 人脸质量检测（单项检测，根据 {@link FaceCheck.CheckOperation} 配置要检测的项）
     *
     * @param operation  指定人脸检测项，参考：{@link FaceCheck.CheckOperation}
     * @param pixels     图像BGR数据
     * @param width      图像宽度
     * @param height     图像高度
     * @param facePos    人脸图像对应的人脸信息结构体，结构体信息参考 {@link THFI_FacePos} 类
     * @param faceResult 人脸质量检测结果
     * @return 成功返回0，否则返回<0的数
     */
    public static int faceQualityCheck(FaceCheck.CheckOperation operation, byte[] pixels, int width, int height, THFI_FacePos[] facePos, int[] faceResult) {

        int ret = -1;

        if (pixels == null) {
            return ret;
        }

        //根据检测到的人脸，判断角度和人脸坐标准确度
        THFI_FacePos pos = facePos[0];

        if (pos.fAngle.confidence < THFI_Param.CONFIDENCE_THRESHOLD || Math.abs(pos.fAngle.pitch) > THFI_Param.FACE_VALID_ANGLE
                || Math.abs(pos.fAngle.roll) > THFI_Param.FACE_VALID_ANGLE) {
            Log.d(TAG, "人脸定位准确度不足或人脸角度大于" + THFI_Param.FACE_VALID_ANGLE);
            return -2;
        }

        int[] tmp = new int[facePos.length];

        switch (operation) {
            case CHECK_HAT:
                ret = FaceCheck.THFQCheckHat((short) 0, pixels, 24, width, height, facePos, faceResult);
                break;
            case CHECK_OCCLUSION:
                ret = FaceCheck.THFQCheckOcclusion((short) 0, pixels, 24, width, height, facePos, faceResult);
                break;
            case CHECK_BRIGHTNESS:
                ret = FaceCheck.THFQCheckBrightness((short) 0, pixels, 24, width, height, facePos, faceResult);
                break;
            case CHECK_BLUR:
                ret = FaceCheck.THFQCheckBlurGlasses((short) 0, pixels, 24, width, height, facePos, faceResult, tmp);
                break;
            case CHECK_GLASSES:
                ret = FaceCheck.THFQCheckBlurGlasses((short) 0, pixels, 24, width, height, facePos, tmp, faceResult);
                break;
            default:
                break;
        }

        return ret;
    }

    /**
     * 人脸属性检测
     *
     * @param pixels     图像BGR数据
     * @param width      图像宽度
     * @param height     图像高度
     * @param facePos    人脸图像对应的人脸信息结构体，结构体信息参考 {@link THFI_FacePos} 类
     * @param faceResult 人脸质量检测结果，具体检测内容参考：参考：{@link THFP_Result}
     * @return
     */
    public static int facePropertyCheck(byte[] pixels, int width, int height, THFI_FacePos[] facePos, THFP_Result faceResult) {

        if (pixels == null) {
            return -1;
        }

        //根据检测到的人脸，判断角度和人脸坐标准确度
        THFI_FacePos pos = facePos[0];

        if (pos.fAngle.confidence < THFI_Param.CONFIDENCE_THRESHOLD || Math.abs(pos.fAngle.pitch) > THFI_Param.FACE_VALID_ANGLE
                || Math.abs(pos.fAngle.roll) > THFI_Param.FACE_VALID_ANGLE) {
            Log.d(TAG, "人脸定位准确度不足或人脸角度大于" + THFI_Param.FACE_VALID_ANGLE);
            return -2;
        }

        int ret = FaceCheck.THFPExecute((short) 0, pixels, width, height, facePos, faceResult);

        return ret;
    }

    /**
     * 人脸戴口罩检测
     *
     * @param pixels         图像BGR数据
     * @param width          图像宽度
     * @param height         图像高度
     * @param facePos        人脸图像对应的人脸信息结构体，结构体信息参考 {@link THFI_FacePos} 类
     * @param faceMaskResult 人脸戴口罩检测结果，0-1的浮点数，越大越可能佩戴口罩，建议判别阈值为0.5
     * @return 1表示检测到口罩，0表示没有检测到口罩
     */
    public static int faceMaskCheck(byte[] pixels, int width, int height, THFI_FacePos[] facePos, float[] faceMaskResult) {

        if (pixels == null) {
            return -1;
        }

        int ret = FaceCheck.THFMExecute((short) 0, pixels, width, height, facePos, faceMaskResult);

        return ret;
    }

    /**
     * 人脸检测
     *
     * @param pixels  待抽取特征点的图片（请保证图片是正的）
     * @param angle   图片允许偏移的最大角度（建议建模时20度以内，检测时30度以内）
     * @param facePos 图片对应的人脸位置结构体，参考 {@link THFI_FacePos}
     * @return
     */
    public static int faceDetect(byte[] pixels, int width, int height, int angle, THFI_FacePos[] facePos) {

        if (pixels == null) {
            return -1;
        }

        int ret = FaceCheck.CheckFace((short) 0, pixels, 24, width, height, facePos, THFI_Param.MAX_FACE_NUMS, THFI_Param.SAMPLE_SIZE);

        if (ret > 0) {   //检测到人脸，判断角度和人脸坐标准确度
            THFI_FacePos pos = facePos[0];

            if (pos.fAngle.confidence < THFI_Param.CONFIDENCE_THRESHOLD || Math.abs(pos.fAngle.pitch) > angle || Math.abs(pos.fAngle.roll) > angle) {
                Log.i("face_info", "人脸定位准确度不足或人脸角度大于" + angle + ", confidence:" + pos.fAngle.confidence);
            }

        } else {
            //未检测到人脸
        }

        return ret;
    }

    /**
     * 抽取特征点，注意：如果要抽取特征的图像比较小（如需要抽取身份证照片中的人脸图像的特征），最好先把人脸图像对应的Bitmap放大2倍再传到算法，这样可以增加
     * 抽取成功率
     *
     * @param pixelsBGR 待抽取特征点的图片（请保证图片是正的）
     * @param angle     图片允许偏移的最大角度（建议建模时20度以内，检测时30度以内）
     * @param facePos   图片对应的人脸位置，该变量通过人脸检测接口（{@link FaceFunction#faceDetect(byte[], int, int, int, THFI_FacePos[])}）获得
     * @return 抽取成功返回图像对应的特征数组，否则返回null
     */
    public synchronized static byte[] faceFeatures(byte[] pixelsBGR, int width, int height, int angle, THFI_FacePos[] facePos) {

        if (pixelsBGR == null || facePos == null || facePos[0] == null) {
            return null;
        }

        //根据检测到的人脸，判断角度和人脸坐标准确度
        THFI_FacePos pos = facePos[0];

        if (pos.fAngle.confidence < THFI_Param.CONFIDENCE_THRESHOLD || Math.abs(pos.fAngle.pitch) > angle || Math.abs(pos.fAngle.roll) > angle) {
            Log.d(TAG, "人脸定位准确度不足或人脸角度大于" + angle);
            return null;
        }

        int size = FaceCheck.GetFeaturesSize();
        byte[] feature = new byte[size];

        int ret = FaceCheck.GetFeatures(pixelsBGR, width, height, 3, facePos[0], feature);

        if (ret == 1) {
            return feature;
        }

        return null;
    }

    /**
     * 从Bitmap图像提取特征
     *
     * @param bitmap      RGB彩色图像
     * @param isCheckLive 是否开启活体检测
     * @return 抽取成功返回图像对应的特征数组，否则返回null
     */
    public synchronized static byte[] faceFeaturesRGB(Bitmap bitmap, boolean isCheckLive) {
        if (bitmap == null) {
            return null;
        }

        //根据检测到的人脸，判断角度和人脸坐标准确度
        THFI_FacePos[] facePos = new THFI_FacePos[1];
        for (int i = 0; i < facePos.length; i++) {
            facePos[i] = new THFI_FacePos();
        }

        byte[] pixelsBGR = getPixelsBGR(bitmap);
        int faceNum = faceDetect(pixelsBGR, bitmap.getWidth(), bitmap.getHeight(), THFI_Param.FACE_VALID_ANGLE, facePos);
        if (faceNum < 1) {
            Log.i("face_test", "FaceFunction.java FaceFunction faceFeatures faceNum:" + faceNum);
            return null;
        }

        float liveScore = THFI_Param.LIVE_THRESHOLD + 1;
        int result = -1;

        byte[] feature = new byte[FaceCheck.GetFeaturesSize()];
        if (isCheckLive) {
            liveScore = faceLiveCheckBGR(pixelsBGR, facePos, bitmap.getWidth(), bitmap.getHeight());
        }
        if (liveScore > THFI_Param.LIVE_THRESHOLD) {
            result = FaceCheck.GetFeatures(pixelsBGR, bitmap.getWidth(), bitmap.getHeight(), 3, facePos[0], feature);
        }

        if (result == 1) {
            return feature;
        } else {
            return null;
        }
    }

    /**
     * 单个建模，将特征保存到文件中
     *
     * @param feature 需要保存的特征
     * @return
     */
    public static boolean saveFaceFeatures(byte[] feature, String userName) {
        if (!createDir())
            return false;

        if (feature == null) {
            Log.e(TAG, "建模失败 特征点为空");
            return false;
        }

        THFI_Param.FaceName.add(THFI_Param.EnrolledNum, userName);
        FaceCheck.addFeature(THFI_Param.EnrolledNum, feature);
        THFI_Param.EnrolledNum++;

        boolean result = saveBytesToFile(feature, savePath, userName + THFI_Param.SUFFIX);
        if (!result) {
            Log.e(TAG, "特征保存失败");
        }

        return result;
    }

    public static float faceCompare(byte[] feature1, byte[] feature2) {
        if (feature1 == null || feature2 == null) {
            return -1;
        }

        return FaceCheck.FaceCompare(feature1, feature2);
    }


    /**
     * 人脸比对 1比N （该接口的比对针对底层管理的特征，如果需要完成应用层中保存的特征的1:N，调用{@link FaceCheck#ver1NN(int, byte[], byte[], int[], float[])}）
     *
     * @param feature 比对者
     * @return 比对成功返回比对成功的index，否则返回<0
     */
    public static long[] faceComparison1ToNMem(byte[] feature) {
        long[] t = new long[2];
        t[0] = 0L;
        t[1] = 0L;
        if (feature == null) {
            return t;
        }
        int[] matchIndex = new int[1];
        float[] matchScore = new float[1];
//        int ret = FaceCheck.ver1N(THFI_Param.EnrolledNum, feature, matchIndex, matchScore);
        int ret = FaceCheck.ver1NThreshold(THFI_Param.EnrolledNum, feature, matchIndex, matchScore, THFI_Param.COMPARE_THRESHOLD);
        if (ret == 0) {
            t[0] = matchIndex[0];
            t[1] = (long) (matchScore[0] * 100F);
        }
        return t;
    }

    /**
     * 获取bitmap中的BGR格式的像素数据
     *
     * @param bmp
     * @return
     */
    public static byte[] getPixelsBGR(Bitmap bmp) {
        if (bmp == null) {
            return null;
        }

        int width = bmp.getWidth();
        int height = bmp.getHeight();

        byte[] pixelsByteArrayDest = new byte[width * height * 3];
        byte[] pixelsByteArraySrc = new byte[width * height * 4];

        ByteBuffer src = ByteBuffer.wrap(pixelsByteArraySrc);
        bmp.copyPixelsToBuffer(src);  //从bitmap中取像素值到buffer中，像素值类型为ARGB

        FaceCheck.getPixelsBGR(pixelsByteArrayDest, pixelsByteArraySrc, width, height);

        return pixelsByteArrayDest;
    }

    /**
     * 创建模型库存放目录
     *
     * @return
     */
    private static boolean createDir() {
        //创建文件夹
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return true;
    }

    /**
     * 将byte数组保存成文件
     */
    public static boolean saveBytesToFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return true;
    }

}
