package com.AiChlFace;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.AiChlFace.FACE_DETECT_RESULT;
import com.leessy.liuc.aiface.CheckLicense;
import com.leessy.liuc.aiface.DebugL;

// 多通道人脸识别认证 SDK 接口类
public class AiChlFace {
    public static int inits = -100;//初始化状态记录

    /**
     * 封装初始化接口
     *
     * @param context
     * @param nMaxChannelNum
     * @return
     */
    public static int InitCardLicense(Context context, int nMaxChannelNum, boolean isV10) {
        SetAuth(3, 0);
        String strCacheDir = context.getCacheDir().getAbsolutePath();
        CheckLicense.UpDateLicense(context, strCacheDir, 3);
        if (isV10) {
            inits = InitV10(nMaxChannelNum, strCacheDir);
        } else {
            inits = Init(nMaxChannelNum, strCacheDir);
        }
        return inits;
    }

    /**
     * 封装初始化接口
     *
     * @param context
     * @param nMaxChannelNum
     * @return
     */
    public static int InitDm2016License(Context context, int nMaxChannelNum, boolean isV10) {
        SetAuth(2, 0);
        String strCacheDir = context.getCacheDir().getAbsolutePath();
        CheckLicense.UpDateLicense(context, strCacheDir, 2);
        if (isV10) {
            inits = InitV10(nMaxChannelNum, strCacheDir);
        } else {
            inits = Init(nMaxChannelNum, strCacheDir);
        }
        return inits;
    }

    /**
     * 封装初始化接口
     *
     * @param context
     * @param nMaxChannelNum
     * @return
     */
    public static int InitDebug(Context context, int nMaxChannelNum, boolean isV10) {
        SetAuth(100, 0);
        String strCacheDir = context.getCacheDir().getAbsolutePath();
        if (isV10) {
            inits = InitV10(nMaxChannelNum, strCacheDir);
        } else {
            inits = Init(nMaxChannelNum, strCacheDir);
        }
        return inits;
    }

    public static int InitNet(Context context, int nMaxChannelNum) {
//        SetAuth(100, 0);
        String strCacheDir = context.getCacheDir().getAbsolutePath();
        return Init(nMaxChannelNum, strCacheDir);
    }

    public static int InitComplex(Context context, int nMaxChannelNum) {
        DebugL.start(context);
        SetAuth(100, 0);
        String strCacheDir = context.getCacheDir().getAbsolutePath();
        int r = Init(nMaxChannelNum, strCacheDir);
        DebugL.finished(context);
        return r;
    }


    /***************************************以下原内容**************************************/


    // 获取SDK版本（可不调用）
    // 返回：SDK版本号
    // 备注：可不调用，任何时候均可调用
    public static native int Ver();

    // 手持机通过授权供应服务器获取本设备的授权(设备出厂前调用)
    // 输入参数： strIP ---- 云授权服务器IP地址
    //            nPort ---- 云授权服务器工作端口（授权供应服务器默认工作端口为6490）
    // 输出参数：无
    // 返回：获取的注册码
    // 备注：1. 如果本设备已有授权，不得重复申请（因服务器记录丢失等原因，重复申请或被视为新设备申请授权从而浪费授权个数）
    //      2.  必须关闭身份证读卡模块才能调用，建议在加载身份证模块前调用
    //      3. 本接口会自动对手持机身份证模块供电，完成后自动断开
    public static String GetLicenseCode(Activity activity, String strIP, int nPort) {
        Context ctx = activity.getApplicationContext();

        // 对身份证模块供电
//        WtWdPowerUtils.setIDPower(ctx);
        // 向授权服务供应服务器申请授权
        String str = GetLicense(activity, strIP, nPort);
        // 关闭身份证模块电源，如果接下来需要加载身份证模块开始读卡，建议这里不要关电
//        WtWdPowerUtils.closeIDPower(ctx);
        return str;
    }

    // 手持机验证本设备的授权
    // 输入参数： strLicense ---- 本设备的授权
    // 输出参数：无
    // 返回：0：验证授权成功，-1：授权无效，-2：非手持机或手持机身份证模块未供电
    // 备注：1. 本接口必须在初始化前调用，并且不与 SetAuth同时调用
    //      2.  必须关闭身份证读卡模块才能调用，建议在加载身份证模块前初始化算法
    //      3. 本接口会自动对手持机身份证模块供电，完成后自动断开
    public static int VerifyLicenseCode(Activity activity, String strLicense) {
        Context ctx = activity.getApplicationContext();
        // 对身份证模块供电
//        WtWdPowerUtils.setIDPower(ctx);
        // 验证授权
        int ret = VerifyLicense(activity, strLicense);
        // 关闭身份证模块电源，如果接下来需要加载身份证模块开始读卡，建议这里不要关电
//        WtWdPowerUtils.closeIDPower(ctx);
        return ret;
    }

    // 设置认证方式
    // 输入参数：
    //     nAuthType ---- 认证方式：0-红色或灰色USB加密狗或USB加密芯片，2-板载I2C加密芯片，3-读卡器I2C加密芯片，其它-保留
    //     nUsbDogHandle ---- USB加密狗认证时指定加密狗设备句柄，需据此才能操作USB加密狗设备（可参考DEMO例程请求权限并获取设备句柄）
    //                        非USB加密狗认证时本参数无意义，可指定任何值
    // 备注：必须在SDK初始化前调用才有效
    public static native void SetAuth(int nAuthType, int nUsbDogHandle);

    // SDK(V7算法)初始化 【为兼容旧版本而保留，新用户不要调用本接口 】
    // 输入参数：
    //     nMaxChannelNum ----  需要开启的最大通道数(受加密狗控制)
    //     strCachePath ---- 本APP的cache目录，需要此目录有可读写权限，且能根据上级目录找到lib目录加载模型文件（可参考DEMO例程获取cache目录）
    // 返回：成功返回0，许可无效返回-1，算法初始化失败返回-2
    // 备注：检测人脸、获取特征大小、提取特征、一对一及一对多等接口都必须在SDK初始化成功后才能调用
    //       本接口为V7算法版本保留，无需兼容V7算法的用户应调用V10接口 InitV10 或 InitExV10
    public static native int Init(int nMaxChannelNum, String strCacheDir);

    // SDK(V7算法)初始化
    // 输入参数：
    //     nMaxChannelNum ----  需要开启的最大通道数(受加密狗控制)
    //     strCachePath ---- 本APP的cache目录，需要此目录有可读写权限，且能根据上级目录找到lib目录加载模型文件（可参考DEMO例程获取cache目录）
    // 返回：成功返回0，许可无效返回-1，算法初始化失败返回-2
    // 备注：检测人脸、获取特征大小、提取特征、一对一及一对多等接口都必须在SDK初始化成功后才能调用
    public static native int InitV10(int nMaxChannelNum, String strCacheDir);

    // SDK(V7算法)初始化(允许指定独立的库文件及临时文件目录) 【为兼容旧版本而保留，新用户不要调用本接口 】
    // 输入参数： strLibPath ---- SDK依赖的LIB文件所在目录
    //            strCachePath ---- 临时文件目录，需要此目录有可读写权限（可参考DEMO例程获取cache目录）
    // 返回：成功返回0，许可无效返回-1，算法初始化失败返回-2
    // 备注：检测人脸、获取特征大小、提取特征、一对一及一对多等接口都必须在SDK初始化成功后才能调用
    //       本接口支持LIB文件目录与临时文件目录任意指定
    //       本接口为V7算法版本保留，无需兼容V7算法的用户应调用V10接口 InitV10 或 InitExV10
    public static native int InitEx(int nMaxChannelNum, String strLibDir, String strCacheDir);

    // SDK(V10算法)初始化(允许指定独立的库文件及临时文件目录)
    // 输入参数： strLibPath ---- SDK依赖的LIB文件所在目录
    //            strCachePath ---- 临时文件目录，需要此目录有可读写权限（可参考DEMO例程获取cache目录）
    // 返回：成功返回0，许可无效返回-1，算法初始化失败返回-2
    // 备注：检测人脸、获取特征大小、提取特征、一对一及一对多等接口都必须在SDK初始化成功后才能调用
    //       本接口支持LIB文件目录与临时文件目录任意指定
    public static native int InitExV10(int nMaxChannelNum, String strLibDir, String strCacheDir);

    // SDK反初始化
    // 备注：必须在初始化成功后调用，反初始化后不能再调用除获取SDK版本及SDK初始化外的任何接口
    public static native void Uninit();

    // 设置检测人脸的图象压缩大小
    // 输入参数：nDetectSize ---- 检测图象的最大宽度或高度（象素单位），超过此大小则压缩至此大小以内，此参数为0表示采用原图检测不压缩
    // 输出参数：无
    // 返回：无
    // 备注：原始图象的宽或高超过输入参数值时，将被压缩至此参数值以内再检测以保证检测速度；
    //       输入参数越大，则压缩比越小，支持的人脸大小则越大，检测识别精度越大，但检测效率低；
    //       输入参数越小，则压缩比越大，支持的人脸大小则越小，检测识别精度越低，但检测效率高；
    //       输入参数为0时，对原始图象直接检测，精度最高，可支持极小的人脸，但分辨率越高，检测效率越低
    //       不调用本接口时，SDK默认值为320，建议在较高性能的平台设置为640（单人脸检测识别场景建议图象分辨率为640x480，更高分辨率可能导致精度降低）
    public static native void SetDetectSize(int nDetectSize);

    // 设置身份证小照片的质量检测
    // 输入参数：bCheckQuality
    //              0 ---- 对于身份证小照片（102x126分辨率）不检测质量，较模糊的照片也能检测出人脸并能参与比对
    //              1 ---- 对于身份证小照片（102x126分辨率）也检测质量，较模糊的照片不返回人脸
    // 输出参数：无
    // 返回：无
    // 备注：本接口只影响身份证小照片，特指从身份证阅读器中读出来的分辨率为102x126的照片
    //       不调用本接口时，SDK默认不检测身份证小照片的质量，适合一对一人证核验场景。如果用于1:N场景，请设置为检测质量，避免特别模糊的身份证小照片引起误识
    public static native void SetIdPhotoQuality(int bCheckQuality);

    // 检测单个（最大的）人脸
    // 输入参数：
    //     nChannelNo ----  通道号(0 ~ nMaxChannelNum - 1)
    //     bRgb24 ---- RGB24格式的图象数据
    //     nWidth ---- 图象数据宽度（象素单位）
    //     nHeight ---- 图象数据高度（象素单位）
    // 输出参数：sFaceResult ---- 结构体存放检测到的人脸参数（人脸及眼睛等坐标位置及角度等，调用前必须分配有效的空间）
    // 返回：返回1表示检测到人脸，0表示无人脸，-1表示未授权，-2表示参数错误，其它表示内部错误
    public static native int DetectFace(int nChannelNo, byte[] bRgb24, int nWidth, int nHeight, FACE_DETECT_RESULT sFaceResult);

    // 检测单个（最大的）人脸, 支持NV21格式，支持旋转与镜象，支持中间区域检测以加快检测速度
    // 输入参数：
    //     nChannelNo ----  通道号(0 ~ nMaxChannelNum - 1)
    //     nFmt ---- 输入源图象数据格式（0：YUV420P, 1: NV12，2: NV21）
    //     bSrcImg ---- 输入源图象数据
    //     nWidth ---- 输入源图象的宽度（象素单位）
    //     nHeight ---- 输入源图象的高度（象素单位）
    //     nLeft ---- 检测区域左上角X坐标(相对于输入源图象，全图检测时填0)
    //     nTop ---- 检测区域左上角Y坐标(相对于输入源图象，全图检测时填0)
    //     nRight ---- 检测区域右下角X坐标(相对于输入源图象，全图检测时也可填0)
    //     nBottom ---- 检测区域右下角Y坐标(相对于输入源图象，全图检测时也可填0)
    //     nRotate ---- 旋转方式（对输入源图象旋转，0：不旋转，1：左旋90度，2：右旋90度）
    //     bMirror ---- 左右镜象（相对于旋转后的图象，0：左右不镜象，1：左右镜象）
    // 输出参数:
    //     bRgb24 ---- 输出的RGB24格式图象数据(裁减、旋转和镜象后的图象数据)
    //     nNewWidth ---- 输出图象的宽度(裁减、旋转和镜象后的图象宽度)
    //     nNewHeight ---- 输出图象的高度(裁减、旋转和镜象后的图象宽度)
    //     sFaceResult ---- 检测到的人脸参数（人脸及眼睛等坐标位置及角度等，相对于裁减、旋转和镜象后的图象，调用前必须分配有效的空间）
    // 返回：返回1表示检测到人脸，0表示无人脸，-1表示未授权，-2表示参数错误，其它表示内部错误
    // 备注：1. 使用此接口的检测结果来提取特征或检测活体时，必须使用这里的输出图象、输出图象的宽高和检测到的人脸参数做为参数
    //       2. 界面上需要画人脸人眼等坐标时，需要对检测出的人脸坐标参数根据裁减、旋转和镜象情况进行校正
    public static native int DetectFaceEx(int nChannelNo, int nFmt, byte[] bSrcImg, int nWidth, int nHeight, int nLeft, int nTop, int nRight, int nBottom, int nRotate, int bMirror, byte[] bRgb24, int[] nNewWidth, int[] nNewHeight, FACE_DETECT_RESULT sFaceResult);

    // 检测多人脸
    // 输入参数：
    //     nChannelNo ----  通道号(0 ~ nMaxChannelNum - 1)
    //     bRgb24 ---- RGB24格式的图象数据
    //     nWidth ---- 图象数据宽度（象素单位）
    //     nHeight ---- 图象数据高度（象素单位）
    //     nMaxFace ---- 最多支持的人脸个数
    // 输出参数：
    //     sFaceResult ---- 结构体存放检测到的人脸参数（人脸及眼睛等坐标位置及角度等，调用前必须分配nMaxFace 个对象空间）
    // 返回：返回检测到的人脸个数，0表示无人脸，-1表示未授权，-2表示参数错误，其它表示内部错误
    // 备注: sFaceResult 需分配不少于 nMaxFace 个对象空间，实际填充的人脸参数个数以返回的人脸个数为准
    public static native int DetectAllFaces(int nChannelNo, byte[] bRgb24, int nWidth, int nHeight, int nMaxFace, FACE_DETECT_RESULT[] sFaceResult);

    // 检测多人脸，支持NV21格式，支持旋转与镜象
    // 输入参数：
    //     nChannelNo ----  通道号(0 ~ nMaxChannelNum - 1)
    //     nFmt ---- 输入源图象数据格式（0：YUV420P, 1: NV12，2: NV21）
    //     bSrcImg ---- 输入源图象数据
    //     nWidth ---- 输入源图象的宽度（象素单位）
    //     nHeight ---- 输入源图象的高度（象素单位）
    //     nRotate ---- 旋转方式（对输入源图象旋转，0：不旋转，1：左旋90度，2：右旋90度）
    //     bMirror ---- 左右镜象（相对于旋转后的图象，0：左右不镜象，1：左右镜象）
    //     nMaxFace ---- 最多支持的人脸个数
    // 输出参数:
    //     bRgb24 ---- 输出的RGB24格式图象数据(裁减、旋转和镜象后的图象数据)
    //     nNewWidth ---- 输出图象的宽度(裁减、旋转和镜象后的图象宽度)
    //     nNewHeight ---- 输出图象的高度(裁减、旋转和镜象后的图象宽度)
    //     sFaceResult ---- 检测到的人脸参数（人脸及眼睛等坐标位置及角度等，相对于裁减、旋转和镜象后的图象，调用前必须分配nMaxFace 个对象空间）
    // 返回：返回检测到的人脸个数，0表示无人脸，-1表示未授权，-2表示参数错误，其它表示内部错误
    // 备注：1. sFaceResult 需分配不少于 nMaxFace 个对象空间，实际填充的人脸参数个数以返回的人脸个数为准
    //       2. 使用此接口的检测结果来提取特征时，必须使用这里的输出图象、输出图象的宽高和检测到的人脸参数做为参数
    //       3. 界面上需要画人脸人眼等坐标时，需要对检测出的人脸坐标参数根据裁减、旋转和镜象情况进行校正
    public static native int DetectAllFacesEx(int nChannelNo, int nFmt, byte[] bSrcImg, int nWidth, int nHeight, int nRotate, int bMirror, int nMaxFace, byte[] bRgb24, int[] nNewWidth, int[] nNewHeight, FACE_DETECT_RESULT[] sFaceResult);

    // 检测人脸质量，包括口罩遮挡等属性
    // 输入参数：
    //     nChannelNo ----  通道号(0 ~ nMaxChannelNum - 1)
    //     bRgb24 ---- RGB24格式的图象数据
    //     nWidth ---- 图象数据宽度
    //     nHeight ---- 图象数据高度
    //     sFaceResult ---- 检测到的人脸参数（必须将检测人脸返回的结果原样传入）
    // 输出参数：sFaceQuality ---- 返回人脸质量
    // 返回：成功返回0，未授权返回-1，参数错误返回-2，失败返回其它
    public static native int FaceQuality(int nChannelNo, byte[] bRgb24, int nWidth, int nHeight, FACE_DETECT_RESULT sFaceResult, FACE_QUALITY sFaceQuality);

    // 获取特征码大小
    // 返回：特征码大小
    public static native int FeatureSize();

    // 提取特征码
    // 输入参数：
    //     nChannelNo ----  通道号(0 ~ nMaxChannelNum - 1)
    //     bRgb24 ---- RGB24格式的图象数据
    //     nWidth ---- 图象数据宽度
    //     nHeight ---- 图象数据高度
    //     sFaceResult ---- 检测到的人脸参数（必须将检测人脸返回的结果原样传入）
    // 输出参数：bFeature ---- 特征码（调用前必须分配有效的空间）
    // 返回：成功返回0，未授权返回-1，参数错误返回-2，失败返回其它
    public static native int FeatureGet(int nChannelNo, byte[] bRgb24, int nWidth, int nHeight, FACE_DETECT_RESULT sFaceResult, byte[] bFeature);

    // 一对一特征比对
    // 输入参数：
    //     nChannelNo ----  通道号(0 ~ nMaxChannelNum - 1)
    //     bFeature1 ---- 第1个人脸特征
    //     bFeature2 ---- 第2个人脸特征
    // 返回：返回两个人脸特征对应的人脸的相似度（0-100）
    public static native int FeatureCompare(int nChannelNo, byte[] bFeature1, byte[] bFeature2);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                               //
    //  以下为一对多比对接口，对于目标人员较多且相对固定的场景，效率远比循环调用一对一接口时要高得多                 //
    //                                                                                                               //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 创建一对多特征比对列表
    // 输入参数：nMaxFeatureNum ---- 列表能容纳的最大特征数
    // 返回：返回特征比对列表句柄，为空表示创建列表失败
    // 备注：能容纳的最大特征数越大，则消耗的内存越多，另外实际支持的最大特征数可能受加密狗限制
    public static native int ListCreate(int nMaxFeatureNum);

    // 向一对多特征比对列表加入目标特征
    // 输入参数：
    //        hList ---- 目标特征要加入的特征比对列表句柄
    //        nPos ---- 指针非空时存放特征插入位置，位置值为0表示插到列表最前面，为-1或大于当前列表中的特征数则表示插到列表最后面
    //        bFeatures ---- 要插入的目标特征码，多个特征按特征码大小（IdFaceSdkFeatureSize函数接口返回的值）对齐
    //        nFeatureNum ---- 要插入的特征数量
    // 输出参数：pnPos ---- 指针非空时返回第一个特征的插入位置
    // 返回：返回当前列表的总特征数
    public static native int ListInsert(int hList, int[] nPos, byte[] bFeatures, int nFeatureNum);

    // 从一对多特征比对列表中删除部分特征
    // 输入参数：
    //        hList ---- 要删除特征的特征比对列表句柄
    //        nPos ---- 要删除的特征的起始位置
    //        nFeatureNum ---- 要删除的特征数量
    // 返回：返回当前列表的总特征数
    public static native int ListRemove(int hList, int nPos, int nFeatureNum);

    // 清空一对多特征比对列表中的所有特征
    // 输入参数：
    //        hList ---- 要清空的一对多特征比对列表句柄
    // 返回：无
    // 备注：调用后列表中的实际特征数量为0（列表能容纳的最大特征数保持不变）
    public static native void ListClearAll(int hList);

    // 一对多特征比对
    // 输入参数：
    //        nChannelNo ----  通道号(0 ~ nMaxChannelNum - 1)
    //        hList ---- 存放要参与比较的目标特征库的特征比对列表句柄
    //        bFeature ---- 要参与特征比对的源特征码
    //        nPosBegin ---- 要参与比对的目标特征的起始位置（如果比对全部目标特征，则起始位置填0）
    //        nCompareNum ---- 要参与比对的目标特征的数量（如果比对全部目标特征，则比对数量填0或-1或实际特征数或最大特征数）
    // 输出参数：nScores ---- 顺序存放与各目标特征进行比对的相似度（每个特征比对的结果均为一字节，值范围为0-100）
    // 返回：返回实际参与比对的特征数量，也是返回的相似度个数
    public static native int ListCompare(int nChannelNo, int hList, byte[] bFeature, int nPosBegin, int nCompareNum, byte[] nScores);

    // 销毁一对多特征比对列表
    // 输入参数：
    //        hList ---- 要销毁的一对多特征比对列表句柄
    // 返回：无
    // 备注：特征比对列表不再使用时需调用此接口释放资源
    public static native void ListDestroy(int hList);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                               //
    //  以下为活体检测接口                                                                                           //
    //                                                                                                               //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 标准活体检测（需要双模彩色和红外图象）
    // 输入参数：
    //           nChannelNo ----  通道号(0 ~ nMaxChannelNum - 1)
    //           nWidth ---- 图象数据宽度（象素单位）
    //           nHeight ---- 图象数据高度（象素单位）
    //           bColorRgb24 ---- RGB24格式的彩色图象数据
    //           sColorFaceResult ---- 彩色图象检测出的人脸参数（调用 DetectFace 对彩色图象检测出的结果）
    //           nColorLeftEyeX, nColorLeftEyeY, nColorRightEyeX, nColorRightEyeY ---- 彩色图象检测出的左右眼坐标（支持第三方人脸检测算法对彩色图象检测出的人眼坐标）
    //           bBwRgb24 ---- RGB24格式的黑白图象数据
    //           sBwFaceResult ---- 黑白图象检测出的人脸参数（调用 DetectFace 对黑白图象检测出的结果）
    //           nBWLeftEyeX, nBWLeftEyeY, nBWRightEyeX, nBWRightEyeY ---- 黑白图象检测出的左右眼坐标（支持第三方人脸检测算法对黑白图象检测出的人眼坐标）
    // 输出参数：无
    // 返回值：
    //    1 ---- 已确认是活体,本轮结果无需继续检测
    //    0 ---- 本帧不确认是活体,需继续检测
    //   -1 ---- 活体检测功能未授权
    //   -2 ---- 参数错误
    //   -X ---- 内部错误
    // 备注：
    //    1. LiveDetect 需要同时对黑白图象和彩色图象进行人脸检测，并且都能检测到人脸，然后将检测结果传入（内部不需要再检测人脸直接判别是否活体）；如果只有一个图象能检测到活体，则视为非活体
    //    2. LiveDetectColor / LiveDetectColorEyes 只需要对彩色图象进行人脸检测，并能检测到人脸，然后将检测结果传入（内部会先对黑白图象检测人脸然后判别是否活体）；如果彩色图象不能检测到活体，则视为非活体
    //    3. LiveDetectBW / LiveDetectBWEyes 只需要对黑白图象进行人脸检测，并能检测到人脸，然后将检测结果传入（内部会先对彩色图象检测人脸然后判别是否活体）；如果黑白图象不能检测到活体，则视为非活体
    //    4. LiveDetect 不需要对图象进行人脸检测，只需要传入彩色和黑白图象（内部会先对两个图象检测人脸然后判别是否活体）
    //    5. 过程中只要一次确认是活体，则本次结果确认为活体；如超时仍无一次确认是活体，则本次结果确认为非活体
    public static native int LiveDetect(int nChannelNo, int nWidth, int nHeight, byte[] bColorRgb24, FACE_DETECT_RESULT sColorFaceResult, byte[] bBwRgb24, FACE_DETECT_RESULT sBwFaceResult);

    public static native int LiveDetectColor(int nChannelNo, int nWidth, int nHeight, byte[] bColorRgb24, FACE_DETECT_RESULT sColorFaceResult, byte[] bBwRgb24);

    public static native int LiveDetectColorEyes(int nChannelNo, int nWidth, int nHeight, byte[] bColorRgb24, int nColorLeftEyeX, int nColorLeftEyeY, int nColorRightEyeX, int nColorRightEyeY, byte[] bBwRgb24);

    public static native int LiveDetectBW(int nChannelNo, int nWidth, int nHeight, byte[] bColorRgb24, byte[] bBwRgb24, FACE_DETECT_RESULT sBwFaceResult);

    public static native int LiveDetectBWEyes(int nChannelNo, int nWidth, int nHeight, byte[] bColorRgb24, byte[] bBwRgb24, int nBwLeftEyeX, int nBWLeftEyeY, int nBwRightEyeX, int nBwRightEyeY);

    public static native int LiveDetectImg(int nChannelNo, int nWidth, int nHeight, byte[] bColorRgb24, byte[] bBwRgb24);

    // 单目活体检测（只需要单路图象，彩色或红外均支持）
    // 输入参数：
    //           nChannelNo ----  通道号(0 ~ nMaxChannelNum - 1)
    //           nCameraType ---- 采集设备类型（0-彩色相机，1-黑白相机）
    //           nWidth ---- 图象数据宽度（象素单位）
    //           nHeight ---- 图象数据高度（象素单位）
    //           bRgb24 ---- RGB24格式的图象数据
    //           sFaceResult ---- 图象检测出的人脸参数（调用 AiFaceDetectFace 对图象检测出的结果，传入此参数检测效率最高）
    //           nLeftEyeX, nLeftEyeY, nRightEyeX, nRightEyeY ---- 图象检测出的左右眼坐标（支持第三方人脸检测算法对图象检测出的人眼坐标, 检测效率比传入sFaceResult略低）
    // 输出参数：无
    // 返回值：
    //    1 ---- 已确认是活体,本轮结果无需继续检测
    //    0 ---- 本帧不确认是活体,需继续检测
    //   -1 ---- 活体检测功能未授权
    //   -2 ---- 参数错误
    //   -X ---- 内部错误
    // 备注：
    //    1. LiveDetectOneCamera / LiveDetectEyesOneCamera 需要对图象进行人脸检测，并能检测到人脸，然后将检测结果传入（内部不需要再检测人脸直接判别是否活体）；如果图象不能检测到活体，则视为非活体
    //    2. LiveDetectImgOneCamera 不需要对图象进行人脸检测，直接传入图象（内部会先对图象检测人脸然后判别是否活体，检测效率最低）
    //    3. 过程中只要一次确认是活体，则本次结果确认为活体；如超时仍无一次确认是活体，则本次结果确认为非活体
    public static native int LiveDetectOneCamera(int nChannelNo, int nCameraType, int nWidth, int nHeight, byte[] bRgb24, FACE_DETECT_RESULT sFaceResult);

    public static native int LiveDetectEyesOneCamera(int nChannelNo, int nCameraType, int nWidth, int nHeight, byte[] bRgb24, int nLeftEyeX, int nLeftEyeY, int nRightEyeX, int nRightEyeY);

    public static native int LiveDetectImgOneCamera(int nChannelNo, int nCameraType, int nWidth, int nHeight, byte[] bRgb24);

    // 设置活体检测的判活阈值
    // 输入参数：threshold ---- 想要设置的阈值,支持的阈值范围为 30~70，默认值为50.
    // 输出参数：无
    // 返回值：无
    // 备注：设置过高的阈值则真人也难判别为活体，设置过低的阈值会导致照片也可能误判为活体。建议保持默认值或在默认值左右微调
    //       任何时候调用均有效
    public static native void SetLiveFaceThreshold(int threshold);

    // 获取判活阈值
    // 输入参数：无
    // 输出参数：无
    // 返回值：返回当前活体检测的判活阈值（默认值为50）
    // 备注：任何时候调用均有效
    public static native int GetLiveFaceThreshold();

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                               //
    //  以下为支持应用做二次加密开放接口                                                                                               //
    //                                                                                                               //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 获取加密狗的唯一ID
    // 输入参数：无
    // 输出参数：bHwID ---- 返回加密狗的唯一ID（8字节二进制数据，调用前必须预分配8字节缓冲区）
    // 返回值：无
    public static native void AiDogGetID(byte[] bHwID);

    // 将应用所需的加密数据写入加密狗中
    // 输入参数：bData ---- 要写入加密狗中的加密数据
    //           nDataLen ---- 要写入的加密数据长度，最大支持64字节
    // 输出参数：无
    // 返回值：返回0表示写数据成功，其它表示写数据失败
    public static native int AiDogWriteData(byte[] bData, int nDataLen);

    // 读出应用写入加密狗中的加密数据
    // 输入参数：nReadLen ---- 要读出的加密数据的长度，最大支持64字节
    // 输出参数：bData ---- 返回读出的加密狗中的加密数据
    // 返回值：返回0表示读数据成功，其它表示读数据失败
    public static native int AiDogReadData(byte[] bData, int nReadLen);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                               //
    //  以下为辅助接口                                                                                               //
    //                                                                                                               //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 图象文件解码出原始图象数据，支持 JPEG、PNG 及 BMP 文件
    // 输入参数：
    //        strFileName ---- 要解码的图象文件名
    //        nBufferSize ---- 图象数据缓冲区长度，长度值小于解码数据所需空间时，解码失败只返回图象分辨率不返回数据
    //        nDepth ---- 请求解码后的图象数据的颜色深度，要求灰度图象数据时真 8，要求RGB24格式的图象数据时填 24
    // 输出参数：
    //        bBuffer ---- 解码出的图象数据，必须预先分配足够的缓冲区，或者传入空值表示只检测图象分辨率不返回数据
    //        nWidth ---- 返回图象宽度
    //        nHeight ---- 返回图象高度
    // 返回：0-成功 ，< 0 失败
    public static native int ReadImageFile(String strFileName, byte[] bBuffer, int nBufSize, int[] nWidth, int[] nHeight, int nDepth);

    // RGB24或灰度图象数据压缩成JPEG文件
    // 输入参数：
    //        strFileName ---- 要保存的JPEG文件名
    //        bData ---- RGB24或灰度图象数据
    //        nWidth ---- 图象宽度
    //        nHeight ---- 图象高度
    //        nDepth ---- 图象数据的颜色深度，灰度图象数据必须为 8，RGB24图象数据必须为 24
    //        nQuality ---- 图象质量（范围：0-100, 值越大图象质量超好文件也越大，建议一般场景为 75，要求高图象质量时为 95）
    // 返回：0-成功 ，< 0 失败
    public static native int SaveJpegFile(String strFileName, byte[] bData, int nWidth, int nHeight, int nDepth, int nQuality);

    // YUV422图象数据转换为RGB24图象数据
    // 输入参数：
    //        bYuv422 ---- YUV422格式的图象数据（bYExchange参数为0时，图象数据序列：U Y V Y U Y V Y ...，bYExchange参数为1时，图象数据序列为：Y U Y V Y U Y V ...）
    //        nWidth ---- 图象宽度
    //        nHeight ---- 图象高度
    //        bYExchange ---- Y与UV排列位置交换（为0表示UYVY，为1表示YUYV）
    // 输出参数：
    //        bRgb24 ---- RGB24格式的图象数据，必须预先分配足够的缓冲区
    // 返回：0-成功 ，< 0 失败
    public static native int YUV422_TO_RGB24(byte[] bYuv422, int nWidth, int nHeight, int bYExchange, byte[] bRgb24);

    // YUV420P图象数据转换为RGB24图象数据
    // 输入参数：
    //        bYuv420P ---- YUV420P格式的图象数据（nFmt参数为0时，图象数据序列：Y Y Y Y ... U ... V ..., nFmt为1时，图象数据序列为：Y Y Y Y ... U V ...,nFmt为2时，图象数据序列为：Y Y Y Y ... V U ...）
    //        nWidth ---- 图象宽度
    //        nHeight ---- 图象高度
    //        nFmt ---- 图象数据格式（0：YUV420P, 1: NV12，2: NV21）
    // 输出参数：
    //        bRgb24 ---- RGB24格式的图象数据，必须预先分配足够的缓冲区
    // 返回：0-成功 ，< 0 失败
    public static native int YUV420P_TO_RGB24(byte[] bYuv420P, int nWidth, int nHeight, int nFmt, byte[] bRgb24);

    // 获取可用的CPU核心总数
    // 输入参数：无
    // 输出参数：无
    // 返回值：返回当前设备可用的CPU核心总数
    // 备注：根据系统策略不同，安卓设备有时并非所有CPU核心都处于开启状态，插电工作的设备，应将策略调整为最高性能
    public static native int GetCpuNum();

    // 设置各功能的CPU核心数（多核CPU有效）
    // 输入参数： nFuncNo ---- 功能号（0-全部功能，1-人脸检测，2-特征提取，3-活体检测）
    //            nCoreNum ---- 该功能允许同时启用的CPU核心数
    // 输出参数：无
    // 返回：无
    // 备注：默认各功能同时启用的核心数为总核心数-1，初始化前调用有效
    public static native void SetFuncCpuNum(int nFuncNo, int nCoreNum);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                               //
    //  以下为手持机内部接口，必须在手持机身份证模块已供电时调用，外部建议调用AiFaceGetLicense / AiFaceVerifyLicense     //
    //                                                                                                               //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 手持机通过授权供应服务器获取本设备的授权
    // 输入参数： strIP ---- 云授权服务器IP地址
    //            nPort ---- 云授权服务器工作端口（云授权服务器默认工作端口为6389）
    // 输出参数：无
    // 返回：获取的注册码
    // 备注：1. 如果本设备已有授权，不得重复申请（因服务器记录丢失等原因，重复申请或被视为新设备申请授权从而浪费授权个数）
    //       2. 调用本接口前必须对手持机身份证模块供电
    public static native String GetLicense(Activity activity, String strIP, int nPort);

    // 手持机验证本设备的授权
    // 输入参数： strLicense ---- 本设备的授权
    // 输出参数：无
    // 返回：0：验证授权成功，-1：授权无效，-2：非手持机或手持机身份证模块未供电
    // 备注：调用本接口前必须对手持机身份证模块供电
    public static native int VerifyLicense(Activity activity, String strLicense);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                               //
    //  加载动态库，Android 5.0以上只需加需AiFace这个主动态库（其它会自动加载）                                         //
    //                                                                                                               //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static {
        try {
            if(android.os.Build.VERSION.SDK_INT < 21) {
            System.loadLibrary("THFaceImage");
            System.loadLibrary("THFaceLive");
            System.loadLibrary("THFaceQuality");
//            System.loadLibrary("omp");
            }
            System.loadLibrary("AiChlFace");
        } catch (Throwable e) {
            Log.e("LoadLibrary", "Load library AiChlFace fail.");
        }
    }
}

