package com.example.mvvmdemo.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmdemo.base.BaseViewModel
import com.example.mvvmdemo.net.UiState

class RewardAdViewModel :BaseViewModel() {
    // 广告加载状态
    private val _adLoadState = MutableLiveData<UiState<Boolean>>()
    val adLoadState: LiveData<UiState<Boolean>> = _adLoadState

    // 奖励状态
    private val _rewardState = MutableLiveData<UiState<Int>>()
    val rewardState: LiveData<UiState<Int>> = _rewardState

    // 设置广告加载中状态
    fun setAdLoadingState() {
        _adLoadState.value = UiState.Loading
    }

    // 设置广告加载成功
    fun setAdLoadSuccess() {
        _adLoadState.value = UiState.Success(true)
    }

    // 设置广告加载失败
    fun setAdLoadFailed(errorMsg: String?) {
        _adLoadState.value = UiState.Error(errorMsg ?: "未知错误")
    }

    // 发放奖励
    fun giveReward() {
        // 这里可以根据实际需求设置奖励数量
        val rewardAmount = 10
        _rewardState.value = UiState.Success(rewardAmount)

        // 这里可以添加保存奖励到本地或发送到服务器的逻辑
    }
}