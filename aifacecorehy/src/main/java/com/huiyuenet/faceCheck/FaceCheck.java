package com.huiyuenet.faceCheck;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * 人脸核心接口类，为了方便调用，该类中部分稍微复杂的接口在 {@link FaceFunction} 中有封装
 */
public class FaceCheck {

    private static Context sContext;

    private static String rootDir = Environment.getExternalStorageDirectory().toString() + "/FaceResource/";
    /**算法模型库（_ko.so）存放路径，_ko.so最好跟普通的.so放到一起，并且将readDir设置为context.getApplicationInfo().nativeLibraryDir*/
    private static String readDir = "";
    /**算法运行过程中使用的临时目录（需要确保当前应用可以正常访问）*/
    private static String writeDir = rootDir + "write/";

    /**
     * 算法初始化接口（应用启动时调用）
     * @param context
     * @return
     */
    public static int init(Context context){
        sContext = context.getApplicationContext();
        createDir(context);

        return initFaceEngine();
    }

    private static int initFaceEngine() {

        int initResult1 = -1;
        int initResult2 = -1;
        int initResult3 = -1;

        initResult1 = FaceCheck.Init(readDir, writeDir, sContext);
        initResult2 = FaceCheck.InitFaceEngine(readDir, writeDir);
        initResult3 = FaceCheck.allocateFeatureMemory(5000);

        Log.i("face_test", "FaceCheck initFaceEngine initResult1:" + initResult1 + ", initResult2:" + initResult2 + ", initResult3:" + initResult3);
        Log.i("face_test", "FaceCheck initFaceEngine sdkversioninfo:" + GetSDKVersionInfo());

        if(initResult1>=0 && initResult2>=0 && initResult3>=0){
            return THFI_Param.SUCCESS;

        }else{
            int code = initResult1;

            if(initResult1 < 0){
                code = initResult1;

            }else if(initResult2 < 0){
                code = initResult2;

            }else if(initResult3 < 0){
                code = initResult3;
            }
            return code;  // 返回不同的返回值，用于提供给应用层做出不同的处理
        }
    }

    /**
     * 创建算法需要的readDir和writeDir
     * @param context
     */
    private static void createDir(Context context) {
        readDir = context.getApplicationInfo().nativeLibraryDir;

        File file = new File(writeDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 初始化引擎；不建议单独调用，而是直接调用封装好的接口 {@link FaceCheck#init(Context)} 来初始化人脸算法，只需要在程序启动时调用一次
     * @param read 存放 libTHDetect_dpbin.so文件的目录
     * @param write 临时读写目录，供算法初始化使用
     *
     * @return 成功返回0，否则返回<0的数
     */
    private static native int Init(String read, String write, Context context);

    /**
     * 初始化人脸引擎；不建议单独调用，而是直接调用封装好的接口 {@link FaceCheck#init(Context)} 来初始化人脸算法，只需要在程序启动时调用一次
     * @param read 核心库文件读取目录
     * @param write 初始化引擎时使用的临时写目录
     * @return
     */
    private static native int InitFaceEngine (String read, String write);

    /**
     * 释放引擎（应用退出时调用）
     */
    public static native void Release();

    /**
     * 释放引擎（应用退出时调用）
     */
    public static native void FaceEngineRelease();

    /**
     * <p>【底层管理特征接口】如果应用层自己管理特征，则该接口不需要调用 </p>
     * <p/>
     *
     *  分配一块空间用于保存已注册特征，已注册特征可以通过addFeature()添加。
     *  这部分空间在native层分配，所以访问起来效率更快一些，通过该方法分配的空间，可以使用addFeature()向这块空间添加特征，
     *  当需要比对的时候，调用ver1N()，传入一个需要比对的特征即可实现1:N
     *
     * @param capacity  最大特征数量，目前允许的最大容量为20,0000个特征
     *
     * @return 成功返回0，否则返回<0的数
     */
    public static native int allocateFeatureMemory(int capacity);

    /**
     * <p>【底层管理特征接口】如果应用层自己管理特征，则该接口不需要调用 </p>
     * <p/>
     *
     *  向通过allocateFeatureMemory()分配的空间中添加一个特征。
     * @param index 添加特征的位置，如果被添加的位置已经有特征，则新特征会替换掉已有特征
     * @param feature 被添加的特征
     *
     * @return 成功返回0，否则返回<0的数
     */
    public static native int addFeature(int index, byte[] feature);

    /**
     * <p>【底层管理特征接口】如果应用层自己管理特征，则该接口不需要调用 </p>
     * <p/>
     *
     * 清空已保存的特征，注：只是将特征清空，并不会释放保存特征的空间
     */
    public static native void clearFeature();

    /**
     * <p>【底层管理特征接口】如果应用层自己管理特征，则该接口不需要调用 </p>
     * <p/>
     *
     * 特征1:N接口，通过输入单个特征，与已经保存的特征进行比对。
     * @param matchNum 与已保存的特征中的前多少个特征进行比对，通常是已保存特征个数
     * @param feature 需要比对的单个特征
     * @param matchIndex 比对成功的index
     * @param matchScore 比对成功的分数
     *
     * @return 成功返回0，否则返回<0的数
     */
    public static native int ver1N(int matchNum, byte[] feature, int[] matchIndex, float[] matchScore);

    /**
     * <p>【底层管理特征接口】如果应用层自己管理特征，则该接口不需要调用 </p>
     * <p/>
     *
     * 特征1:N接口，应用层自己设定比对通过阈值，通过输入单个特征，与已经保存的特征进行比对。
     * @param matchNum 与已保存的特征中的前多少个特征进行比对，通常是已保存特征个数
     * @param feature 需要比对的单个特征
     * @param matchIndex 比对成功的index
     * @param matchScore 比对成功的分数
     * @param threshold 比对通过的阈值（0~1之间，建议0.7）
     *
     * @return 成功返回0，否则返回<0的数
     */
    public static native int ver1NThreshold(int matchNum, byte[] feature, int[] matchIndex, float[] matchScore, float threshold);


    /**
     * 特征1:N接口，直接输入单个特征和多个特征进行1:N功能。注意该接口与 {@link FaceCheck#ver1N(int, byte[], int[], float[])} 的区别：前者为
     * 应用层自己管理特征，参数中需要传入一个特征列表featureList；后者为SDK底层管理特征，只需要传入待比对的单个特征即可；
     * @param featureCount 与featureList中的前多少个特征进行比对，通常是featureList中保存的特征个数
     * @param featureList 需要比对的N个特征列表
     * @param featureSingle 需要比对的单个特征
     * @param matchIndex 比对成功的index
     * @param matchScore 比对成功的分数
     *
     * @return 成功返回0，否则返回<0的数
     */
    public static native int ver1NN(
            int featureCount, byte[] featureList, byte[] featureSingle, int[] matchIndex, float[] matchScore);

    /**
     * 特征1:N接口，应用层自己设定比对通过阈值，直接输入单个特征和多个特征进行1:N功能。注意该接口与 {@link FaceCheck#ver1NThreshold(int, byte[], int[], float[], float)}
     * 的区别：前者为应用层自己管理特征，参数中需要传入一个特征列表featureList；后者为SDK底层管理特征，只需要传入待比对的单个特征即可；
     * @param featureCount 与featureList中的前多少个特征进行比对，通常是featureList中保存的特征个数
     * @param featureList 需要比对的N个特征列表
     * @param featureSingle 需要比对的单个特征
     * @param matchIndex 比对成功的index
     * @param matchScore 比对成功的分数
     * @param threshold 比对通过的阈值（0~1之间，建议0.7）
     *
     * @return 成功返回0，否则返回<0的数
     */
    public static native int ver1NNThreshold(
            int featureCount, byte[] featureList, byte[] featureSingle, int[] matchIndex, float[] matchScore, float threshold);

    /**
     * 活体检测接口，根据输入的图像，得出该图像的活体分数。如果要将当前SDK用于单个摄像头设备，调用活体检测接口时，ir相关的参数（irImageData、facePosIr）都
     * 传入null即可，同时活体分数数组score只有score[0]有效。
     *
     * @param bgrImageData （输入参数）彩色人脸图像对应的BGR数据，【注意】：图像格式不能传错，必须是BGR格式
     * @param irImageData （输入参数）红外人脸图像对应的BGR数据，【注意】：图像格式不能传错，必须是BGR格式
     * @param facePosRgb （输入参数）彩色人脸图像对应的人脸信息结构体，该结构体由人脸检测函数（{@link FaceCheck#CheckFace(short, byte[], int, int, int, THFI_FacePos[], int, int)}）
     *                      得到，结构体信息参考 {@link THFI_FacePos} 类
     * @param facePosIr  （输入参数）红外人脸图像对应的人脸信息结构体，该结构体由人脸检测函数（{@link FaceCheck#CheckFace(short, byte[], int, int, int, THFI_FacePos[], int, int)}）
     *                     得到，结构体信息参考 {@link THFI_FacePos} 类
     * @param width  （输入参数）图像宽度
     * @param height （输入参数）图像高度
     * @param threshold （输入参数）活体检测阈值，传入50即可
     * @param score （输出参数）彩色图像和红外图像的活体分数，score[0]为彩色人脸图像活体分数，score[1]为红外人脸图像活体分数
     *
     * @return 成功返回0，否则返回<0的数
     *
     */
    public static native int DetectLive2(byte[] bgrImageData, byte[] irImageData, THFI_FacePos[] facePosRgb, THFI_FacePos[] facePosIr, int width, int height, int threshold, float[] score);

    /**
     * 将RGB格式的图像数据转换为BGR格式
     * @param dest 转换后的BGR格式的数据
     * @param src 需要被转换的RGB数据
     * @param width 图像宽
     * @param height 图像高
     *
     * @return 成功返回0，否则返回<0的数
     */
    public static native int getPixelsBGR(byte[] dest, byte[] src, int width, int height);

    /**
     * 获取当前SDK版本信息
     *
     * @return 当前SDK版本信息
     */
    public static native String GetSDKVersionInfo();


    /**
     * 人脸检测
     * @param nChannelID 检测通道id
     * @param pImage 图片数组
     * @param bpp 图片位数 24,8
     * @param nWidth 图片宽度
     * @param nHeight 图片高度
     * @param pfps 检测后的人脸坐标点数组
     * @param nMaxFaceNums 最大检测数
     * @param nSampleSize 缩放比例
     *
     * @return
     */
    public static native int CheckFace(short nChannelID, byte[] pImage, int bpp, int nWidth, int nHeight, THFI_FacePos[] pfps, int nMaxFaceNums, int nSampleSize);


    /**
     * 获取特征点长度（当前版本为2560）
     * @return
     */
    public static native int GetFeaturesSize();

    /**
     * 从图像中提取人脸特征
     *
     * @param pImage 待抽取图片数据
     * @param nWidth 图片宽
     * @param nHeight 图片高
     * @param nChannel 色彩通道，必须填3
     * @param pfps 人脸坐标点，由人脸检测模块得到
     * @param pFeature 返回的人脸特征
     *
     * @return 特征提取成功 返回>0，否则返回对应的错误码
     */
    public static native int GetFeatures (byte[] pImage, int nWidth, int nHeight, int nChannel, THFI_FacePos pfps, byte[] pFeature);

    /**
     * 人脸特征1对1比对
     * @param pFeature1 待比对人脸1
     * @param pFeature2 待比对人脸2
     *
     * @return
     */
    public static native float FaceCompare (byte[] pFeature1,  byte[] pFeature2);
    /**
     * 人脸特征1对1比对，建议使用 {@link FaceCheck#FaceCompare(byte[], byte[])}
     * @deprecated
     *
     * @param pFeature1 待比对人脸1
     * @param pFeature2 待比对人脸2
     *
     * @return
     *
     * @see FaceCheck#FaceCompare(byte[], byte[])
     */
    public static native float FaceCompareShort (byte[] pFeature1,  byte[] pFeature2);

    /** ** ** ** ** ** ** ** ** ** ** ** ** <人脸质量检测模块相关接口> ** ** ** ** ** ** ** ** ** ** ** ** **/
    /**
     * 设置人脸质量检测参数
     * @param param 算法相关参数，主要是亮度阈值，如果参数为NULL，算法内部将使用默认值。参考：{@link THFQ_Param}
     *
     * @return 成功返回0，否则返回<0的数
     */
    public static native int THFQSetParam(THFQ_Param param);

    /**
     * 人脸质量检测（需要添加FaceQuality模块）
     * @param _channelID 通道id,与初始化THFQ_Create相关，一般来说合法的通道id为0到nChannelNum-1；
     * @param _imageArray 图像数据，图像数据必须为bgr顺序；
     * @param _bpp 图像数据格式，必须为24，代表bgr三通道数据；
     * @param _width 图像宽
     * @param _height 图像高
     * @param _facePosObjArray 人脸检测结果，由人脸检测接口 {@link FaceFunction#faceDetect(byte[], int, int, int, THFI_FacePos[])}得到；参考：{@link THFI_FacePos}
     * @param _resultObjArray 人脸质量检测结果，具体检测内容参考：{@link THFQ_Result}
     *
     * @return 成功返回0，否则返回<0的数
     */
    public static native int THFQCheck(short _channelID, byte[] _imageArray, int _bpp, int _width, int _height, THFI_FacePos[] _facePosObjArray, THFQ_Result[] _resultObjArray);

    /** 人脸亮度检测 */
    public static native int THFQCheckBrightness(short _channelID, byte[] _imageArray, int _bpp, int _width, int _height, THFI_FacePos[] _facePosObjArray, int[] _brightness);
    /** 人脸遮挡检测 */
    public static native int THFQCheckOcclusion(short _channelID, byte[] _imageArray, int _bpp, int _width, int _height, THFI_FacePos[] _facePosObjArray, int[] _occlusion);
    /** 人脸戴帽子检测 */
    public static native int THFQCheckHat(short _channelID, byte[] _imageArray, int _bpp, int _width, int _height, THFI_FacePos[] _facePosObjArray, int[] _hat);
    /** 人脸模糊度、戴眼镜检测 */
    public static native int THFQCheckBlurGlasses(short _channelID, byte[] _imageArray, int _bpp, int _width, int _height, THFI_FacePos[] _facePosObjArray, int[] _blur, int[] _glasses);

    /**
     * 人脸质量检测项
     * <p> {@link CheckOperation#CHECK_BRIGHTNESS}：检测亮度 </p>
     * <p> {@link CheckOperation#CHECK_OCCLUSION}：检测遮挡度 </p>
     * <p> {@link CheckOperation#CHECK_HAT}：检测帽子 </p>
     * <p> {@link CheckOperation#CHECK_BLUR}：检测模糊度 </p>
     * <p> {@link CheckOperation#CHECK_GLASSES}：检测眼镜 </p>
     */
    public enum CheckOperation{
        CHECK_BRIGHTNESS,
        CHECK_OCCLUSION,
        CHECK_HAT,
        CHECK_BLUR,
        CHECK_GLASSES,
    }
    /** ** ** ** ** ** ** ** ** ** ** ** ** <人脸质量检测模块相关接口> ** ** ** ** ** ** ** ** ** ** ** ** **/


    /** ** ** ** ** ** ** ** ** ** ** ** ** <人脸属性检测模块相关接口> ** ** ** ** ** ** ** ** ** ** ** ** **/
    /**
     * 人脸属性检测（需要添加FaceProperty模块）
     * @param _channelID 通道id,与初始化THFQ_Create相关，一般来说合法的通道id为0到nChannelNum-1；
     * @param _imageArray 图像数据，图像数据必须为bgr顺序；
     * @param _width 图像宽
     * @param _height 图像高
     * @param _facePosObjArray 人脸检测结果，由人脸检测接口 {@link FaceFunction#faceDetect(byte[], int, int, int, THFI_FacePos[])}得到；参考：{@link THFI_FacePos}
     * @param faceResult 人脸属性检测结果，具体检测内容参考：参考：{@link THFP_Result}
     * @return
     */
    public static native int THFPExecute(short _channelID, byte[] _imageArray, int _width, int _height, THFI_FacePos[] _facePosObjArray, THFP_Result faceResult);
    /** ** ** ** ** ** ** ** ** ** ** ** ** <人脸属性检测模块相关接口> ** ** ** ** ** ** ** ** ** ** ** ** **/


    /**
     *
     * @param _channelID 通道id,与初始化THFQ_Create相关，一般来说合法的通道id为0到nChannelNum-1；
     * @param _imageArray  图像数据，图像数据必须为bgr顺序；
     * @param _width 图像宽
     * @param _height 图像高
     * @param _facePosObjArray 人脸检测结果，由人脸检测接口 {@link FaceFunction#faceDetect(byte[], int, int, int, THFI_FacePos[])}得到；参考：{@link THFI_FacePos}
     * @param maskResult 戴口罩检测结果，0-1的浮点数，越大越可能佩戴口罩，建议判别阈值为0.5
     *
     * @return 1表示检测到口罩，0表示没有检测到口罩
     */
    public static native int THFMExecute(short _channelID, byte[] _imageArray, int _width, int _height, THFI_FacePos[] _facePosObjArray, float[] maskResult);


    /** ** ** ** ** ** ** ** ** ** ** ** ** > 测试相关接口 < ** ** ** ** ** ** ** ** ** ** ** ** **/
    private static native int Test(String read, String write, String pic1, String pic2);
    public static int test(Context context, String pic1, String pic2){
        createDir(context);
        return Test(readDir, writeDir, pic1, pic2);
    }

    public static native int getBmpHeaderSize();
    public static native int getBmpHeader(byte[] header, int width, int height, int bitcount);

    /** ** ** ** ** ** ** ** ** ** ** ** ** > 测试相关接口 < ** ** ** ** ** ** ** ** ** ** ** ** **/


    static {
        System.loadLibrary("FaceCheck");
    }
}
