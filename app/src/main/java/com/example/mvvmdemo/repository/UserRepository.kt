package com.example.mvvmdemo.repository

import com.example.mvvmdemo.base.BaseRepository
import com.example.mvvmdemo.data.ArticlePage
import com.example.mvvmdemo.data.User
import com.example.mvvmdemo.net.ApiService
import com.example.mvvmdemo.net.RetrofitClient
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.utils.UserManager
import okhttp3.ResponseBody
import retrofit2.Response

class UserRepository : BaseRepository() {
    private val api = RetrofitClient.instance.create(ApiService::class.java)

    /**
     * 用户登录
     * 注意：登录成功后需要保存cookie用于后续API调用
     */
    suspend fun login(username: String, password: String): UiState<User> {
        return safeApiCall {
            println("=== Login Repository Debug ===")
            println("Attempting login with username: $username")
            
            val response = api.login(username, password)
            println("Login response errorCode: ${response.errorCode}")
            println("Login response errorMsg: ${response.errorMsg}")
            
            if (response.errorCode == 0) {
                val user = response.data ?: throw Exception("登录失败，请稍后重试")
                println("Login successful, user: ${user.username}")
                
                // 保存用户信息
                UserManager.saveUser(user)
                println("User info saved to UserManager")
                
                // 手动构造cookie（wanandroid API的特殊处理）
                // 根据API文档，登录成功后需要保存cookie用于后续请求
                // 使用标准的cookie格式
                val cookie = "loginUserName=$username; loginUserPassword=$password"
                UserManager.saveCookie(cookie)
                
                println("Manual cookie constructed and saved: $cookie")
                println("================================")
                
                user
            } else {
                println("Login failed: ${response.errorMsg}")
                println("================================")
                throw Exception(response.errorMsg ?: "登录失败，请稍后重试")
            }
        }
    }

    /**
     * 用户注册
     */
    suspend fun register(username: String, password: String, repassword: String): UiState<User> {
        return safeApiCall {
            val response = api.register(username, password, repassword)
            if (response.errorCode == 0) {
                response.data ?: throw Exception("注册失败，请稍后重试")
            } else {
                throw Exception(response.errorMsg ?: "注册失败，请稍后重试")
            }
        }
    }

    /**
     * 文章收藏
     */
    suspend fun collectArticle(articleId: Int): UiState<Boolean> {
        return safeApiCall {
            val response = api.collectArticle(articleId)
            if (response.errorCode == 0) {
                true
            } else {
                throw Exception(response.errorMsg ?: "收藏失败，请稍后重试")
            }
        }
    }

    /**
     * 取消文章收藏
     */
    suspend fun uncollectArticle(articleId: Int): UiState<Boolean> {
        return safeApiCall {
            val response = api.uncollectArticle(articleId)
            if (response.errorCode == 0) {
                true
            } else {
                throw Exception(response.errorMsg ?: "取消收藏失败，请稍后重试")
            }
        }
    }

    /**
     * 获取用户收藏的文章列表
     */
    suspend fun getCollectedArticles(page: Int): UiState<ArticlePage> {
        return safeApiCall {
            val response = api.getCollectedArticles(page)
            if (response.errorCode == 0) {
                response.data ?: throw Exception("获取收藏列表失败，请稍后重试")
            } else {
                throw Exception(response.errorMsg ?: "获取收藏列表失败，请稍后重试")
            }
        }
    }
}