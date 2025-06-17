package com.example.mvvmdemo.utils

import android.content.Context
import android.content.Intent
import com.example.mvvmdemo.LoginActivity
import com.example.mvvmdemo.data.User
import com.tencent.mmkv.MMKV

/**
 * 用户管理工具类
 * 
 * 功能：
 * 1. 管理用户登录状态
 * 2. 存储和获取用户信息
 * 3. 处理登录跳转
 * 4. 管理cookie持久化
 * 
 * 使用方式：
 * 1. 检查登录状态：UserManager.isLoggedIn()
 * 2. 获取用户信息：UserManager.getCurrentUser()
 * 3. 保存用户信息：UserManager.saveUser(user)
 * 4. 清除用户信息：UserManager.clearUser()
 * 5. 跳转登录：UserManager.goToLogin(context)
 * 6. 获取cookie：UserManager.getCookie()
 */
object UserManager {
    
    private const val KEY_USER_TOKEN = "user_token"
    private const val KEY_USER_INFO = "user_info"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_COOKIE = "user_cookie"
    
    private val mmkv: MMKV by lazy { MMKV.defaultMMKV() }
    
    /**
     * 检查用户是否已登录
     */
    fun isLoggedIn(): Boolean {
        return mmkv.decodeBool(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * 获取当前用户信息
     */
    fun getCurrentUser(): User? {
        val userJson = mmkv.decodeString(KEY_USER_INFO, null)
        return if (userJson != null) {
            try {
                // 这里可以使用Gson或其他JSON库来解析
                // 暂时返回null，实际使用时需要实现JSON解析
                null
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
    
    /**
     * 获取用户token
     */
    fun getUserToken(): String? {
        return mmkv.decodeString(KEY_USER_TOKEN, null)
    }
    
    /**
     * 获取用户cookie
     */
    fun getCookie(): String? {
        return mmkv.decodeString(KEY_USER_COOKIE, null)
    }
    
    /**
     * 保存用户信息
     */
    fun saveUser(user: User) {
        mmkv.encode(KEY_IS_LOGGED_IN, true)
        mmkv.encode(KEY_USER_TOKEN, user.token)
        // 这里可以使用Gson或其他JSON库来序列化
        // mmkv.encode(KEY_USER_INFO, gson.toJson(user))
    }
    
    /**
     * 保存用户cookie
     */
    fun saveCookie(cookie: String) {
        mmkv.encode(KEY_USER_COOKIE, cookie)
        mmkv.encode(KEY_IS_LOGGED_IN, true)
    }
    
    /**
     * 清除用户信息
     */
    fun clearUser() {
        mmkv.encode(KEY_IS_LOGGED_IN, false)
        mmkv.removeValueForKey(KEY_USER_TOKEN)
        mmkv.removeValueForKey(KEY_USER_INFO)
        mmkv.removeValueForKey(KEY_USER_COOKIE)
    }
    
    /**
     * 跳转到登录页面
     */
    fun goToLogin(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }
    
    /**
     * 检查登录状态，如果未登录则跳转到登录页面
     */
    fun checkLoginAndGoToLogin(context: Context): Boolean {
        return if (isLoggedIn()) {
            true
        } else {
            goToLogin(context)
            false
        }
    }

} 