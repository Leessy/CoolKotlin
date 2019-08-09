package com.leessy.ofm1000test

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import javax.security.cert.X509Certificate
import kotlin.properties.Delegates

/**
 * Created by lc on 2019/6/12.
 */
object RetrofitHelper {
    private var retrofit: Retrofit? = null

    private var service: ApiService by Delegates.notNull()

    fun apiService(): ApiService {
        if (retrofit == null) {
            service = getRetrofit()!!.create(ApiService::class.java)
        }
        return service
    }

    //    private var baseUrl by Preference(Constant.BASE_URL_KEY,Constant.BASE_URL_DEFAULT)

    private fun getRetrofit(): Retrofit? {
        if (retrofit == null) {
            synchronized(RetrofitHelper::class.java) {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                            .baseUrl("http://192.168.11.106:7040")  // baseUrl
                            .client(getOkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build()
                }
            }
        }
        return retrofit
    }

    fun resetHttps() {
        retrofit = null
        service = getRetrofit()!!.create(ApiService::class.java)
    }

    /**
     * 获取 OkHttpClient
     */
    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
//        val httpLoggingInterceptor = HttpLoggingInterceptor()
//        if (BuildConfig.DEBUG) {
//            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//        } else {
//            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
//        }

        //设置 请求的缓存的大小跟位置
//        val cacheFile = File(ShanghaiLixiang.apiService().cacheDir, "cache")
//        val cache = Cache(cacheFile, 1024*1024*5)


        //ssl相关
        var sslContext: SSLContext? = null
        try {
            sslContext = SSLContext.getInstance("SSL")
            sslContext!!.init(null, arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkServerTrusted(
                        chain: Array<out java.security.cert.X509Certificate>?,
                        authType: String?
                ) {
                }

                override fun checkClientTrusted(
                        chain: Array<out java.security.cert.X509Certificate>?,
                        authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate?> {
                    return arrayOfNulls(0)
                }
            }), SecureRandom())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        val DO_NOT_VERIFY = object : HostnameVerifier {
            override fun verify(hostname: String, session: SSLSession): Boolean {
                return true
            }
        }


        return builder.run {
            //            addInterceptor(httpLoggingInterceptor)
//            addInterceptor(HeaderInterceptor())
//            addInterceptor(RequestEncryptInterceptor())//请求数据加密在 okhttp中添加
//            addInterceptor(SaveCookieInterceptor())
//            addInterceptor(CacheInterceptor())
//            cache(cache)  //添加缓存
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(15, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
//            sslSocketFactory(sslContext?.socketFactory)//ssl
//            hostnameVerifier(DO_NOT_VERIFY)//忽略ssl

            retryOnConnectionFailure(true) // 错误重连
            // cookieJar(CookieManager())
        }.build()
    }

}