package com.example.mvvmdemo.repository

import com.blankj.utilcode.util.LogUtils
import com.example.mvvmdemo.base.BaseRepository
import com.example.mvvmdemo.data.Article
import com.example.mvvmdemo.data.Banner
import com.example.mvvmdemo.data.OfficialAccount
import com.example.mvvmdemo.net.ApiService
import com.example.mvvmdemo.net.RetrofitClient
import com.example.mvvmdemo.net.UiState

class HomeRepository : BaseRepository() {
    private val api = RetrofitClient.instance.create(ApiService::class.java)


    suspend fun getHomeArticles(page: Int): UiState<List<Article>> {
        return safeApiCall {
            val resp = api.getHomeArticles(page)
            if (resp.errorCode == 0) {
                resp.data?.datas ?: emptyList()
            } else {
                throw Exception(resp.errorMsg ?: "未知错误")
            }
        }
    }

    /**
     * 轮播图
     */
    suspend fun getBanner(): UiState<List<Banner>> {
        return safeApiCall {
            val resp = api.getBanner()
            LogUtils.d("HomeBanner:$resp")
            if (resp.errorCode == 0) {
                resp.data ?: emptyList()
            } else {
                throw Exception(resp.errorMsg ?: "未知错误")
            }
        }
    }

    /**
     * 获取公众号列表
     */
    suspend fun getOfficialAccounts(): UiState<List<OfficialAccount>> {
        return safeApiCall {
            val resp = api.getWxArticle()
            if (resp.errorCode == 0) {
                resp.data ?: emptyList()
            } else {
                throw Exception(resp.errorMsg ?: "未知错误")
            }
        }
    }

    /**
     * 获取公众号文章列表
     * @param officialAccountId 公众号Id
     * @param page 取值范围 1-40 不传则使用默认值，一旦传入了 page_size，后续该接口分页都需要带上，否则会造成分页读取错误。
     */
    suspend fun getOfficialArticles(officialAccountId: Int, page: Int = 1): UiState<List<Article>> {
        return safeApiCall {
            val resp = api.getOfficialArticles(officialAccountId, page)
            if (resp.errorCode == 0) {
                val articles = resp.data?.datas ?: emptyList()
                articles
            } else {
                throw Exception(resp.errorMsg ?: "未知错误")
            }
        }
    }
}