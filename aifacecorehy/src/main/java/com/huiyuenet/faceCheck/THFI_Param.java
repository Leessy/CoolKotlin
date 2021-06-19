package com.huiyuenet.faceCheck;

import java.util.ArrayList;
import java.util.List;

/**
 * 人脸SDK相关的参数配置、常量类
 */
public class THFI_Param {

    //TODO ================================= 参数配置部分（start） =========================================//
    /**
     * 人脸检测图像降采样比，值越小 表示人脸可检测距离越远，同时人脸检测耗时也越多，目前建议值：360、240、0，如果使用
     * 场景不需要远距离人脸（1m之外），那么建议将该值配置成 240 或 360
     *
     * 说明：这个参数会影响检测距离和检测速度。应该根据产品的应用场景合理设置这个参数。在满足工作距离的前提下，尽可能多的缩小图像检测，而带来处理速度的提升。
     */
    public static final int SAMPLE_SIZE = 360; // 0; //360;  // 240

    /** 活体分数阈值（如果开启IR图像活体检测，活体分数建议0.6；如果只做RGB活体检测，活体分数建议0.8） */
    public static float LIVE_THRESHOLD = 0.60f;

    /** 比对分数阈值，高于该值即表示比对通过（可设置0~1之间的值，推荐0.7） */
    public static float COMPARE_THRESHOLD = 0.7f;

    /**图像质量分数阈值，算法的confidence低于该值，将不会进行活体、抽特征等操作；注：戴口罩活体检测时，建议将该值配置为0.5*/
    public static float CONFIDENCE_THRESHOLD = 0.7f;

    /** 人脸在整个图像中的可见比，如果大于该范围，表示距离过近，视为图像无效 */
    public static final float FACE_VISIBILITY = 0.35f;

    /** 人脸偏转角度限制值（建议建模时20度以内，检测时30度以内） */
    public static final int FACE_VALID_ANGLE = 30;

    /** 是否启用libTHFaceQuality.so库做人脸质量检测（主要包括：Blur、Brightness、Glasses等的检测，具体参考：{@link THFQ_Result}） */
    public static final boolean ENABLE_FACE_QUALITY_CHECK = false;  // 默认不开放此功能，如果需要，请与项目负责人联系

    /** 是否启用libTHFaceProperty.so库做人脸质量检测（主要包括：age、gender、mask等的检测，具体参考：{@link THFP_Result}） */
    public static final boolean ENABLE_FACE_PROPERTY_CHECK = false; // 默认不开放此功能，如果需要，请与项目负责人联系

    /** 是否启用libTHFaceMask.so库做人脸戴口罩检测（主要包括：age、gender、mask等的检测，具体参考：{@link FaceCheck#THFMExecute(short, byte[], int, int, THFI_FacePos[], float[])}） */
    public static final boolean ENABLE_FACE_MASK_CHECK = false; // 默认不开放此功能，如果需要，请与项目负责人联系

    /** 最大支持的检测人脸数量 */
    public static final int MAX_FACE_NUMS = 1;

    /** 建模、比对超时时间（单位：ms） */
    public static final int OPERATE_TIME_OUT = 5000;

    public static final String SERVER_IP = "http://192.144.141.249:7801/activation";

    /**
     * 是否使用指定类型的图像类型判断方式进行RGB/IR的判断；
     *
     * 图像数据判断说明：由于双目摄像头每次启动时IR或者RGB摄像头对应的ID不固定，因此需要在Camera的
     * onPreviewFrame回调中，根据图像数据动态判断图像类型，以此来保证传递给算法正确的图像
     *
     * 【注意】：如果图像数据不正确（如：RGB数据当做IR数据来处理）就会导致活体检测分数异常，因此可以通过
     *          活体检测分数来判断图像数据是否正常；
     */
    public static final boolean IsImageTypeA = false;  // 第一种判断方式
    public static final boolean IsImageTypeB = false;  // 第二种判断方式
    public static final boolean IsImageTypeC = true;   // 第三种方式：CameraID固定，不判断图像数据，如果系统每次重启CameraID都固定，那么建议使用这种方式

    /**
     * 2020.01.16 添加
     *
     * 是否使用USB连接方式；如果为true，则双目摄像头 一个使用 Android Camera API 打开，另一个使用UVC协议打开；否则两个摄像头都
     * 通过 Android Camera API 打开
     *
     * 如果设备无法通过Android Camera API同时打开两个摄像头，需要将该值修改为true
     */
    public static final boolean UseUsbCamera = false;

    //TODO ================================= 参数配置部分（end） =========================================//


    public static final int IMG_HEIGHT = 480;
    public static final int IMG_WIDTH = 640;

    public static int EnrolledNum = 0;

    public static int CameraID = 0;

    public static final String SP_FILE_NAME = "sp_matrix";
    public static final String SP_KEY_MATRIX_ROTATE1 = "sp_matrix_rotate1";
    public static final String SP_KEY_MATRIX_ROTATE2 = "sp_matrix_rotate2";
    public static final String SP_KEY_MATRIX_FLIP1 = "sp_matrix_flip1";
	public static final String SP_KEY_MATRIX_FLIP2 = "sp_matrix_flip2";

    public static int faceFeatureSize = 2560;

    public static final String SUFFIX = ".v10";

    public static List<String> FaceName = new ArrayList<>();
    public static List<String> FacePhoneNumber = new ArrayList<>();

    // ==================人脸算法函数执行返回错误码===============================
    public static final int  ERROR_1                =    -1;       //
    public static final int  ERROR_2                =    -2;       //
    public static final int  ERROR_3                =    -3;       //
    public static final int  ERROR_99               =    -99;      // invalid license
    // ==================人脸算法函数执行返回错误码===============================


    // ==================授权检测相关错误码===============================
    public static final int  VALID                  =    0;           // 成功。仅当此时获取encrypt
    public static final int  ERROR_PARAM            =    -1000;       // 参数校验错误.一般是由于有参数值为空.
    public static final int  ERROR_INVALID_DEVICE   =    -1001;       // 无法匹配到设备类型
    public static final int  ERROR_INVALID_DATE     =    -1002;       // 请求时间超出授权期限
    public static final int  ERROR_INVALID_COUNT    =    -1003;       // 超出该设备的授权数量
    public static final int  ERROR_DEFAULT          =    -1004;       // 系统错误
    // ==================授权检测相关错误码===============================


    // ==================算法校验相关错误码===============================
    public static final int  ALG_VALID              =     0;        // 成功。
    public static final int  ERR_RANDOM             =    -2000;     // 请求随机数错误
    public static final int  ERR_INVALID_AES		=	 -2099;     // AES验证错误
    // ==================算法校验相关错误码===============================



    // ====================人脸算法相关错误码=============================
    public static final int SUCCESS                 =    0;         // 成功
    public static final int ERR_OVER_CUR_CAPACITY   =    -3000;     // 特征容量超过当前已分配空间限制，需要重新分配空间
    public static final int ERR_OVER_MAX_CAPACITY   =    -3001;     // 特征容量超过限制，目前最大支持 200000
    public static final int ERR_INVALID_PARAM       =    -3002;     // 应用层传入参数错误
    public static final int ERR_UNINIT              =    -3003;     // 未执行初始化相关方法
    public static final int ERR_UNLOADED            =    -3004;     // 调用方法时，未加载对应的库（如：libTHFaceQuality.so、libTHFaceProperty.so）
    // ====================人脸算法相关错误码=============================

}



