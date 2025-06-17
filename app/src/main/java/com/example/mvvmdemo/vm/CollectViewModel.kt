package com.example.mvvmdemo.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmdemo.base.BaseViewModel
import com.example.mvvmdemo.data.ArticlePage
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.repository.UserRepository

/**
 * 收藏页面ViewModel
 * 
 * 功能：
 * 1. 管理收藏文章列表数据
 * 2. 处理取消收藏操作
 * 3. 提供状态管理
 * 
 * 使用方式：
 * 1. 在CollectFragment中使用
 * 2. 观察articleState获取文章列表
 * 3. 观察uncollectState获取取消收藏结果
 */
class CollectViewModel : BaseViewModel() {
    
    private val userRepository = UserRepository()
    
    /**
     * 文章列表状态LiveData
     * 用于观察文章列表数据变化
     */
    private val _articleState = MutableLiveData<UiState<ArticlePage>>()
    val articleState: LiveData<UiState<ArticlePage>> = _articleState
    
    /**
     * 取消收藏状态LiveData
     * 用于观察取消收藏操作结果
     */
    private val _uncollectState = MutableLiveData<UiState<Boolean>>()
    val uncollectState: LiveData<UiState<Boolean>> = _uncollectState
    
    /**
     * 获取用户收藏的文章列表
     * 
     * @param page 页码，从0开始
     * 
     * 使用方式：
     * 1. 首次加载时传入page=0
     * 2. 加载更多时传入递增的页码
     */
    fun fetchCollectedArticles(page: Int) {
        launchRequest(
            block = { userRepository.getCollectedArticles(page) },
            liveData = _articleState,
            isEmpty = { it == null || it.datas.isEmpty() }
        )
    }
    
    /**
     * 取消文章收藏
     * 
     * @param articleId 文章ID
     */
    fun uncollectArticle(articleId: Int) {
        launchRequest(
            block = { userRepository.uncollectArticle(articleId) },
            liveData = _uncollectState
        )
    }
} 