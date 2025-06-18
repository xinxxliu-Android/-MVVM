package com.example.mvvmdemo.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmdemo.base.BaseViewModel
import com.example.mvvmdemo.data.Article
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.repository.HomeRepository
import com.example.mvvmdemo.repository.UserRepository
import kotlinx.coroutines.launch

class OfficialArticleViewModel : BaseViewModel() {

    private val repository = HomeRepository()
    private val userRepository = UserRepository()
    private val _articleState = MutableLiveData<UiState<List<Article>>>()
    val articleState: LiveData<UiState<List<Article>>> = _articleState
    /**
     * 收藏状态LiveData
     * 用于观察收藏操作结果
     */
    private val _collectState = MutableLiveData<UiState<Boolean>>()
    val collectState: LiveData<UiState<Boolean>> = _collectState
    private var currentOfficialAccountId: Int = 1
    private var currentPage: Int = 1
    private var isRefreshing: Boolean = false

    /**
     * 获取公众号文章列表
     *  @param officialAccountId 公众号Id
     *  @param page 取值范围 1-40 不传则使用默认值，一旦传入了 page_size，后续该接口分页都需要带上，否则会造成分页读取错误。
     */
    fun fetchOfficialArticles(officialAccountId: Int, page: Int = 1) {
        currentOfficialAccountId = officialAccountId
        currentPage = page
        launchRequest(
            block = { repository.getOfficialArticles(officialAccountId, page) },
            liveData = _articleState,
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