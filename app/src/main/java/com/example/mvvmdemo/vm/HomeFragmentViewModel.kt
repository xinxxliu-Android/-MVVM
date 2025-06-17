package com.example.mvvmdemo.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmdemo.base.BaseViewModel
import com.example.mvvmdemo.data.Article
import com.example.mvvmdemo.data.Banner
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.repository.HomeRepository
import com.example.mvvmdemo.repository.UserRepository

/**
 * 首页Fragment的ViewModel
 * 
 * 功能：
 * 1. 管理首页文章列表数据
 * 2. 管理首页轮播图数据
 * 3. 处理分页加载
 * 
 * 使用方式：
 * 1. 在Fragment中通过viewModels()获取实例
 * 2. 观察articleState和bannerState获取数据
 * 3. 调用fetchHomeArticles和fetchHomeBanner加载数据
 */
class HomeFragmentViewModel : BaseViewModel() {
    /**
     * 数据仓库实例
     * 负责处理数据获取和缓存
     */
    private val repository = HomeRepository()
    private val userRepository = UserRepository()

    /**
     * 文章列表状态LiveData
     * 用于观察文章列表数据变化
     */
    private val _articleState = MutableLiveData<UiState<List<Article>>>()
    val articleState: LiveData<UiState<List<Article>>> = _articleState

    /**
     * 轮播图状态LiveData
     * 用于观察轮播图数据变化
     */
    private val _bannerState = MutableLiveData<UiState<List<Banner>>>()
    val bannerState: LiveData<UiState<List<Banner>>> = _bannerState

    /**
     * 收藏状态LiveData
     * 用于观察收藏操作结果
     */
    private val _collectState = MutableLiveData<UiState<Boolean>>()
    val collectState: LiveData<UiState<Boolean>> = _collectState

    /**
     * 获取首页文章列表
     * 
     * @param page 页码，从0开始
     * 
     * 使用方式：
     * 1. 首次加载时传入page=0
     * 2. 加载更多时传入递增的页码
     */
    fun fetchHomeArticles(page: Int) {
        launchRequest(
            block = { repository.getHomeArticles(page) },
            liveData = _articleState,
            isEmpty = { it == null || it.isEmpty() }
        )
    }

    /**
     * 获取首页轮播图数据
     * 
     * 使用方式：
     * 1. 在Fragment初始化时调用
     * 2. 观察bannerState获取数据
     */
    fun fetchHomeBanner() {
        launchRequest(
            block = { repository.getBanner() },
            liveData = _bannerState,
            isEmpty = { it == null || it.isEmpty() }
        )
    }

    /**
     * 文章收藏
     * 
     * @param articleId 文章ID
     */
    fun collectArticle(articleId: Int) {
        launchRequest(
            block = { userRepository.collectArticle(articleId) },
            liveData = _collectState
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
            liveData = _collectState
        )
    }
}