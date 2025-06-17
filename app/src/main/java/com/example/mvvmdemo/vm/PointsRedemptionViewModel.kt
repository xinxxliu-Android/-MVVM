package com.example.mvvmdemo.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.example.mvvmdemo.base.BaseViewModel
import com.example.mvvmdemo.net.UiState
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PointsRedemptionViewModel : BaseViewModel() {

    private val _currentPoints = MutableLiveData<Int>()
    val currentPoints: LiveData<Int> = _currentPoints

    private val _redeemResult = MutableLiveData<UiState<String>>()
    val redeemResult: LiveData<UiState<String>> = _redeemResult

    private val mmkv: MMKV = MMKV.defaultMMKV()!!
    private val POINTS_KEY = "user_points"

    init {
        // 初始化时加载积分
        loadPoints()
    }

    fun loadPoints() {
        launchRequest(
            block = {
                val points = withContext(Dispatchers.IO) {
                    mmkv.decodeInt(POINTS_KEY, 1250) // 初始1250积分
                }
                _currentPoints.postValue(points)
                UiState.Success("积分加载成功") // 返回一个成功状态
            },
            liveData = _redeemResult // 使用同一个LiveData来表示操作结果
        )
    }

    fun redeemPoints(amount: Int) {
        launchRequest(
            block = {
                delay(1000) // 模拟网络请求

                val current = _currentPoints.value ?: 0
                val requiredPoints = amount // 假设1元需要1积分

                if (current >= requiredPoints) {
                    val newPoints = current - requiredPoints
                    withContext(Dispatchers.IO) {
                        mmkv.encode(POINTS_KEY, newPoints)
                    }
                    _currentPoints.postValue(newPoints)
                    UiState.Success("成功提现 ${amount}元！")
                } else {
                    UiState.Error("积分不足，无法提现。")
                }
            },
            liveData = _redeemResult
        )
    }
} 