package com.example.mvvmdemo.net

import android.icu.util.TimeUnit
import com.example.mvvmdemo.utils.UserManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.wanandroid.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // 打印请求和响应body
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cookie拦截器，自动添加登录cookie
    private val cookieInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val cookie = UserManager.getCookie()
        val newRequest = if (cookie != null) {
            originalRequest.newBuilder()
                .addHeader("Cookie", cookie)
                .build()
        } else {
            originalRequest
        }

        newRequest.headers.forEach { (name, value) ->

        }
        chain.proceed(newRequest)
    }

    // Cookie捕获拦截器，用于保存登录后的cookie
    private val cookieCaptureInterceptor = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)

        // 检查是否是登录请求
        if (request.url.encodedPath.contains("/user/login")) {

            response.headers.forEach { (name, value) ->
                println("$name: $value")
            }
            
            // 从响应头中获取Set-Cookie
            val setCookieHeaders = response.headers.values("Set-Cookie")
            if (setCookieHeaders.isNotEmpty()) {
                // 保存cookie
                val cookie = setCookieHeaders.joinToString("; ")
                UserManager.saveCookie(cookie)

            } else {

                
                // 检查是否有其他形式的cookie信息
                val cookieHeader = response.headers["Cookie"]
                if (cookieHeader != null) {
                    UserManager.saveCookie(cookieHeader)

                }
                
                // 检查响应体是否包含cookie信息
                if (response.code == 200) {
                    // 对于wanandroid API，可能需要手动构造cookie
                    // 通常登录成功后，服务器会返回用户信息，我们可以基于此构造cookie
                }
            }
        }
        
        response
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(cookieCaptureInterceptor) // 先添加cookie捕获拦截器
        .addInterceptor(cookieInterceptor) // 再添加cookie添加拦截器
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)    // 读取超时
        .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .build()
        
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // 设置带日志和cookie的 OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}