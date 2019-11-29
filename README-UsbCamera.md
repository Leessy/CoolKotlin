# USB相机封装库说明
## 项目基于 [saki4510t/UVCCamera](https://github.com/saki4510t/UVCCamera) 的UVC相机库封装


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
    //相机封装库
    implementation 'com.github.Leessy.CoolKotlin:uvccamera:0.2.38'
    // RxJava 2
    implementation 'io.reactivex.rxjava2:rxjava:2.2.11'
    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
    //coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.1'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.1'
	}
```

### 使用示例
+ 初始化

```
//初始化相机管理工具,可选传入相机默认分辨率,并确认是否启用自动设置size默认关
fun initCameras(c: Context, w: Int = 640, h: Int = 480, usingDfSize: Boolean = false) {
	...
}
初始化相机（默认对所有接入相机进行权限请求）
CamerasMng.initCameras(application)
//CamerasMng.initCameras(application，usingDfSize = true)
//CamerasMng.initCameras(application，w = 1280, h = 720, usingDfSize = true)
```

+ 启动相机 设置预览

```
//方法一：获取设备列表 选择指定相机启动
CamerasMng.cameraList.forEach {
            Log.d("CamerasMng", "CamerasMng pid ${it.pid}")
            if (it.pid == 3) {//选择指定相机进行启动
                c = it
               val b = c?.openCamera()//启动相机
				//设置相机参数 宽、高 max_fps最大帧数（根据相机支持帧率）
				//frameType（相机数据类型YUV、 MJPEG）
				//bandwidthFactor带宽（0-1.0）
                c?.setPreviewSize(cameraColorW, cameraColorH, max_fps = 25)
				//c?.setFrameCall（...）//设置相机数据帧回调
				c?.startPreview(surface)//启动相机预览
				//c?.startPreview()//启动相机--不需预览只需要回调数据的方式
            }
        }

//方法二：选择相机，直接调用startPreview方法启动并预览（需要一次性传入正确参数配置）
CamerasMng.cameraList.forEach {
            Log.d("CamerasMng", "CamerasMng pid ${it.pid}")
            if (it.pid == 3) {//选择指定相机进行启动
                c = it
				//参数1、2 SurfaceTexture、Surface（预览方式，两都不传默认不进行预览启动相机）
				//参数3、4传入相机预览宽高
				//参数5、max_fps最大帧数默认15；参数6 、frameType数据类型（）默认MJPEG
				//参数7、previewcall数据回调对象
				//调用示例
                c?.startPreview(previewTexture = surfaceTexture,
                    surface = null,
                    w = 640,
                    h = 480,
                    max_fps = 25,
                    frameType = Camera.FRAME_FORMAT_MJPEG,
                    previewcall = object : IFrameCall {
                        override fun call(bf: ByteBuffer, w: Int, h: Int) {
							//数据回调
                        }
                    })
        }

//关闭相机
//1.关闭相机预览保留相机数据回调
	c?.stopSecede()

//2.关闭相机（一般在SurfaceTextureDestroyed时调用）
	c?.stopPreview()//或者 c?.destroyCamera()

//3.关闭后台启动的相机
CamerasMng.destroyAllCamera()

//4.关比所有相机并反初始化
CamerasMng.destroy()

//5.根据PID过滤不需要处理的UVC设备（初始之前配置）
CamerasMng.addPIDfilters(123, 456,...)

```

