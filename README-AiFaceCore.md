# AiFaceCore核心库封装说明
+ 算法使用物理加密IC方式初始化，非定制设备无法使用
+ 算法跟据常用人脸识别场景封装；
+ 算法支持多通道处理数据（多线程同时检测人脸等），目前默认支持两个彩色相机，两个红外相机；



## 使用说明
>项目主要使用kotlin语言，使用示例将使用kotlin（java使用参考kotlin方式修改即可）

### 工程配置

>项目主要使用kotlin中协程([coroutines](https://github.com/Kotlin/kotlinx.coroutines))
>封装库内使用Rxjava2([Rxjava2](https://github.com/ReactiveX/RxJava))

示例：
```
allprojects {
    repositories {
		//project gradle配置
        maven { url "https://jitpack.io" }
    }
}
dependencies {
    //人脸识别库
    implementation 'com.github.Leessy.CoolKotlin:aifacecore:0.2.38'
    // RxJava 2
    implementation 'io.reactivex.rxjava2:rxjava:2.2.11'
    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
    //coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.1'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.1'
	}
```


### 初始化
> 算法支持主要功能最大占用cpu数
> 算法支持活体检测
> 算法支持新版V10特征与V8特征码配置（互不兼容）

+ 初始化示例：
```
//0-全部功能，1-人脸检测，2-特征提取，3-活体检测
AiChlFace.SetFuncCpuNum(1, 1)
AiChlFace.SetFuncCpuNum(2, 1)
AiChlFace.SetFuncCpuNum(3, 1)
//配置特征码版本（默认V8）
AiFaceCore.isV10 = true
AiFaceCore.initAiFace(
    application, AiFaceType.MODE_DM2016, object : IAiFaceInitCall {
        override fun call(colorsInit: Boolean, irInit: Boolean) {
            //返回彩色、黑白（红外）算法的初始化结果
        }
    }
)
```



### 人脸识别&人库对比
> 默认使用相机回调数据（YUV420SP格式）
> 含人脸信息结果订阅与人脸框位置信息订阅
> 各通道同时只能有一处订阅
> 核心库现在最大检测速度每秒8次左右，最终速度根据处理性能决定

+ 发送数据到人脸算法核心库：
```
//相机回调数据（如果不需要每帧检测，可间隔跳过帧发送检测人脸，建议发送速率6-10帧/每秒）
camera.setFrameCall(object : IFrameCall {
            override fun call(bf: Byte, w: Int, h: Int) {
                    //发送到算法库识别
                    AiFaceCore.dataEmitter(
                        bytes,
                        ImageColor.COLOR,//相机数据颜色类型
                        w,//数据宽
                        h,//数据高
                        CameraID = 0,//相机编号默认0
                        stream = 2,//数据格式（0：YUV420P, 1: NV12，2: NV21）
                        bMirror = 1,//检测人脸时是否镜像
                        nRotate = 0//检测人脸时是否旋转
                    )
			}
		})
```

+ 接收人脸框数据（用于绘制人脸使用）：
```
    //人脸框数据处理
    private fun initFaceRect() {
        AiFaceCore.FollowFaceRect()
            .compose(this.bindUntilEvent(ActivityEvent.PAUSE))
            .observeOn(Schedulers.newThread())
            .map {
                it.apply {
                    rect = RectF(
                        it.nFaceLeft.toFloat(),
                        it.nFaceTop.toFloat(),
                        it.nFaceRight.toFloat(),
                        it.nFaceBottom.toFloat()
                    )
                }
            }
			//人脸位置过滤计算（可选）
            .FaceFilterCalculate(16, FACE_W_MIN, FACE_W_MAX, miniEdgeHorizontal, 0, miniQuality)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
				//判断人脸框数据来源
                if (it.imageColor == ImageColor.COLOR) {
					//faceview为绘制人脸框自定义view(自行实现)
                    faceView.setFaces(
                        it.rect,//人脸框rect
                        it.width,//传入图像宽高（旋转后）
                        it.height,//传入图像宽高（旋转后）
                        it.faceFilterRet != AiFaceFilter.DEFAULT//人脸位置过滤计算结果
                    )
                }
            }, {
                //异常
            })
    }
```
+ 接收人脸识别数据（提取特征码）：
```
        //人脸数据处理
        AiFaceCore.Follows(ImageColor.COLOR, CameraID = 0)
            .compose(this.bindUntilEvent(ActivityEvent.STOP))
            .sample(200, TimeUnit.MILLISECONDS)
            .DetectFace()
			//.DetectFaceAndFilter()//检测人脸并过滤无人脸数据
			//.Livings()//活体检测
			//.LivingsSinglePass()//活体检测单次判断
			//.filter { it.isLivings() }//过滤活体失败的数据
			//.FeatureGet()//提取人脸特征码
			//.filter { it.isReadyCompare() }//过滤提取特征码失败的数据
			//.map {
				//保存数据、对比或者其他逻辑处理
				//或取当前特征码与指定特征码1:1对比
				//t2.CompareRet =AiChlFace.FeatureCompare(t2.nChannelNo.ordinal, it.feature,
				//temp.templateColor).toLong()
			//}
            .subscribe({
			//人脸信息结果 it
			}）
```

+ 人库对比实现：
```
	//初始化彩色对比人库列表，长度1000个（此时分配内存）
    var isOK = ComparList.initCompareList(1000)
    ComparList.addTempList(list)//批量插入数据
    ComparList.addTemp(temp)//插入单个数据
```

+ 特征码管理接口（插入识别库对象必须实现）：
```
interface ITemp {
    fun getDataBaseID(): Long//数据ID识别结果时返回
    fun getFeaturenNormalTemp(): ByteArray //彩色特征码
    fun getFeaturenIrTemp(): ByteArray //红外特征码
}
```
+ 人脸识别+人库对比示例参考：

```
        //人脸数据处理
        AiFaceCore.Follows(ImageColor.COLOR, CameraID = 0)
            .compose(this.bindUntilEvent(ActivityEvent.STOP))
			.DetectFaceAndFilter()//检测人脸并过滤无人脸数据
			.LivingsSinglePass()//活体检测单次判断
			.filter { it.isLivings() }//过滤活体失败的数据
			.DetectFaceAndFilter()//提取人脸特征码并过滤失败数据
			.CompareListColor()//进行彩色人脸库识别
            .subscribe({
				it.CompareRet//对比最高分值数据
				it.CompareDataID//数据ID
			}）
```

+ 人脸识别结果图片转换（人脸区域截取）

```
//人脸数据处理
AiFaceCore.Follows(ImageColor.COLOR, CameraID = 0)
			.compose(this.bindUntilEvent(ActivityEvent.STOP))
			.DetectFaceAndFilter()//检测人脸并过滤无人脸数据
			//.......
            .subscribe({
				//图片数据转成bitmap
				val colors = convertByteToColor(data)//把BGR数据转换成RGB数据
				val bmp = Bitmap.createBitmap(colors, 0, width, width, height, Bitmap.Config.ARGB_8888)
				//截取人脸区域
				bmp = bmp.clipFace(
                                it.faceResult.nFaceLeft,
                                it.faceResult.nFaceTop,
                                it.faceResult.nFaceRight,
                                it.faceResult.nFaceBottom
                            )
			}）

    /*
     * 将BGR数组转化为 RGB像素数组 示例（java）
     */
    private static int[] convertByteToColor(byte[] data) {
        int size = data.length;
        if (size == 0) {
            return null;
        }
        // 理论上data的长度应该是3的倍数，这里做个兼容
        int arg = 0;
        if (size % 3 != 0) {
            arg = 1;
        }
        int[] color = new int[size / 3 + arg];
        if (arg == 0) {//  正好是3的倍数
            for (int i = 0; i < color.length; ++i) {
                //bgr顺序排列
                color[i] = (data[i * 3 + 2] << 16 & 0x00FF0000) |
                        (data[i * 3 + 1] << 8 & 0x0000FF00) |
                        (data[i * 3] & 0x000000FF) |
                        0xFF000000;
                //rgb顺序排列
//                color[i] = (data[i * 3] << 16 & 0x00FF0000) |
//                        (data[i * 3 + 1] << 8 & 0x0000FF00) |
//                        (data[i * 3 + 2] & 0x000000FF) |
//                        0xFF000000;
            }
        } else {// 不是3的倍数
            for (int i = 0; i < color.length - 1; ++i) {
                color[i] = (data[i * 3 + 2] << 16 & 0x00FF0000) |
                        (data[i * 3 + 1] << 8 & 0x0000FF00) |
                        (data[i * 3] & 0x000000FF) |
                        0xFF000000;
				//rgb顺序排列
//                color[i] = (data[i * 3] << 16 & 0x00FF0000) |
//                        (data[i * 3 + 1] << 8 & 0x0000FF00) |
//                        (data[i * 3 + 2] & 0x000000FF) |
//                        0xFF000000;
            }
            color[color.length - 1] = 0xFF000000;// 最后一个像素用黑色填充
        }
        return color;
    }

/**
 * 根据人脸框截取人脸图片 默认偏移offset=35像素点(Kotlin)
 */
fun Bitmap.clipFace(faceL: Int, faceT: Int, faceR: Int, faceB: Int, offset: Int = 35): Bitmap {
    try {
        val x = if (faceL - offset <= 0) 0 else (faceL - offset)
        val y = if (faceT - offset <= 0) 0 else (faceT - offset)
        return Bitmap.createBitmap(
            this,
            x, y,
            if ((faceR + offset) >= width) (width - x) else (faceR + offset) - x,
            if (faceB + offset >= height) (height - y) else (faceB + offset) - y
        )
    } catch (e: Exception) {
        logd("剪裁异常 $e")
    }
    return this
}
```

