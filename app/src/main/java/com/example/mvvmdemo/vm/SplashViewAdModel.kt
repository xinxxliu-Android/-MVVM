package com.example.mvvmdemo.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmdemo.base.BaseViewModel
import com.example.mvvmdemo.net.UiState

class SplashViewAdModel : BaseViewModel() {
    // 广告加载状态
    private val _adLoadState = MutableLiveData<UiState<Boolean>>()
    val adLoadState: LiveData<UiState<Boolean>> = _adLoadState

    // 设置广告加载状态
    fun setAdLoadingState() {
        _adLoadState.value = UiState.Loading
    }

    // 设置广告加载成功
    fun setAdLoadSuccess() {
        _adLoadState.value = UiState.Success(true)
    }

    // 设置广告加载失败
    fun setAdLoadFailed(errorMsg: String?) {
        _adLoadState.value = errorMsg?.let { UiState.Error(it) }
    }

    // 设置广告展示完成
    fun setAdClosed() {
        _adLoadState.value = UiState.Success(false)
    }
}