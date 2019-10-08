# CoolKotlin [![](https://jitpack.io/v/Leessy/CoolKotlin.svg)](https://jitpack.io/#Leessy/CoolKotlin)

### 必须同时引入的库

某些库需要以下内容同时加入配置（并且配置kotlin）
``` Gradle
dependencies {
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:+'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:+'
    implementation 'io.reactivex.rxjava2:rxjava:+'
    implementation 'io.reactivex.rxjava2:rxandroid:+'
}
```

### 请注意！！！
####
* 请勿直接全部引入，例：
``` Gradle
dependencies {
    //直接使用全部功能lib会异常
    implementation 'com.github.Leessy.CoolKotlin:aifacecore:0.2.35'
}
```
### 正常引入方式
* 1.使用人脸算法库：
``` Gradle
dependencies {
    implementation 'com.github.Leessy.CoolKotlin:aifacecore:0.2.35'
}
```

* 2.使用USB相机库：
``` Gradle
dependencies {
    implementation 'com.github.Leessy.CoolKotlin:uvccamera:0.2.35'
}
```

* 3.使用读卡模块功能库（只能同时使用一种）：
``` Gradle
dependencies {
    //相同功能的只能选择一种导入
    implementation 'com.github.Leessy.CoolKotlin:jidacard:0.2.35'
    //implementation 'com.github.Leessy.CoolKotlin:jidacardusb:0.2.35'
    //implementation 'com.github.Leessy.CoolKotlin:yxcardsdk:0.2.35'
}
```
