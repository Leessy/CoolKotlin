# CoolKotlin [![](https://jitpack.io/v/Leessy/CoolKotlin.svg)](https://jitpack.io/#Leessy/CoolKotlin)

### 引入支持

某些库需要以下内容同时加入配置
``` Gradle
dependencies {
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0-M2'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.0-M2'
    implementation 'io.reactivex.rxjava2:rxjava:2.2.2'
    implementation 'io.reactivex.rxjava2:rxandroid:2.1.0'
}
```

### 注意
    某些引入不能同时使用，主要原因为有同名的.so