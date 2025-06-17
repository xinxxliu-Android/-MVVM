package com.example.mvvmdemo.base

import com.example.mvvmdemo.net.UiState

/**
 * Repository基类，统一处理网络异常和token失效
 *使用方法：用法：所有Repository的网络请求都用safeApiCall包裹。
 * @Link UserRepository
 */
open class BaseRepository {
    suspend fun <T> safeApiCall(call: suspend () -> T): UiState<T> {
        /**
         *  网络请求安全调用，自动捕获异常并返回UiState
         */
        return try {
            UiState.Success(call())
        } catch (e: Exception) {
            // 统一处理网络异常
            val errorMsg = when (e) {
                is java.net.UnknownHostException -> "网络不可用，请检查网络连接"
                is java.net.SocketTimeoutException -> "请求超时，请稍后重试"
                is retrofit2.HttpException -> {
                    // 这里可以根据code处理token失效
                    if (e.code() == 401) {
                        // token失效
                        "登录已过期，请重新登录"
                    } else {
                        "服务器错误(${e.code()})"
                    }
                }
                else -> e.message ?: "未知错误"
            }
            UiState.Error(errorMsg)
        }
    }
}