package com.example.mvvmdemo.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmdemo.base.BaseViewModel
import com.example.mvvmdemo.data.Article
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.repository.HomeRepository

class MainViewModel : BaseViewModel() {
    private val repository = HomeRepository()
    private val _articleState = MutableLiveData<UiState<List<Article>>>()
    val articleState: LiveData<UiState<List<Article>>> = _articleState

    fun fetchHomeArticles(page: Int) {
        launchRequest(
            block = { repository.getHomeArticles(page) },
            liveData = _articleState,
            isEmpty = { it == null || it.isEmpty() }
        )
    }
}