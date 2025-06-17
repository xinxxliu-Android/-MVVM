package com.example.mvvmdemo.repository

import com.blankj.utilcode.util.LogUtils
import com.example.mvvmdemo.base.BaseRepository
import com.example.mvvmdemo.data.Article
import com.example.mvvmdemo.data.Banner
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
}