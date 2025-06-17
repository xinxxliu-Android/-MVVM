package com.example.mvvmdemo.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.example.mvvmdemo.base.BaseViewModel
import com.example.mvvmdemo.net.UiState
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdRevenueViewModel : BaseViewModel() {

    private val _todayRevenue = MutableLiveData<Double>()
    val todayRevenue: LiveData<Double> = _todayRevenue

    private val _totalRevenue = MutableLiveData<Double>()
    val totalRevenue: LiveData<Double> = _totalRevenue

    private val _watchedAds = MutableLiveData<Int>()
    val watchedAds: LiveData<Int> = _watchedAds

    private val _adStatsLoadResult = MutableLiveData<UiState<String>>()
    val adStatsLoadResult: LiveData<UiState<String>> = _adStatsLoadResult

    private val mmkv: MMKV = MMKV.defaultMMKV()!!
    private val TODAY_REVENUE_KEY = "today_revenue"
    private val TOTAL_REVENUE_KEY = "total_revenue"
    private val WATCHED_ADS_KEY = "watched_ads"
    private val LAST_RESET_DATE_KEY = "last_reset_date"

    init {
        loadAdStats()
    }

    fun loadAdStats() {
        launchRequest(
            block = {
                withContext(Dispatchers.IO) {
                    resetDailyStatsIfNeeded()
                    _todayRevenue.postValue(mmkv.decodeDouble(TODAY_REVENUE_KEY, 0.0))
                    _totalRevenue.postValue(mmkv.decodeDouble(TOTAL_REVENUE_KEY, 0.0))
                    _watchedAds.postValue(mmkv.decodeInt(WATCHED_ADS_KEY, 0))
                }
                LogUtils.d("AdRevenueViewModel", "加载广告统计: 今日收益=${_todayRevenue.value}, 总收益=${_totalRevenue.value}, 已观看=${_watchedAds.value}")
                UiState.Success("广告统计加载成功")
            },
            liveData = _adStatsLoadResult
        )
    }

    fun addAdReward(amount: Double) {
        launchRequest(
            block = {
                withContext(Dispatchers.IO) {
                    // 更新今日收益
                    val currentTodayRevenue = (_todayRevenue.value ?: 0.0) + amount
                    _todayRevenue.postValue(currentTodayRevenue)
                    mmkv.encode(TODAY_REVENUE_KEY, currentTodayRevenue)

                    // 更新总收益
                    val currentTotalRevenue = (_totalRevenue.value ?: 0.0) + amount
                    _totalRevenue.postValue(currentTotalRevenue)
                    mmkv.encode(TOTAL_REVENUE_KEY, currentTotalRevenue)

                    // 更新观看广告数量
                    val currentWatchedAds = (_watchedAds.value ?: 0) + 1
                    _watchedAds.postValue(currentWatchedAds)
                    mmkv.encode(WATCHED_ADS_KEY, currentWatchedAds)
                }
                LogUtils.d("AdRevenueViewModel", "新增广告奖励: $amount, 当前今日收益=${_todayRevenue.value}, 总收益=${_totalRevenue.value}, 已观看=${_watchedAds.value}")
                UiState.Success("广告奖励添加成功")
            },
            liveData = _adStatsLoadResult // 再次使用同一个LiveData来表示操作结果
        )
    }

    private fun resetDailyStatsIfNeeded() {
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val lastResetDate = mmkv.decodeString(LAST_RESET_DATE_KEY, "")

        if (today != lastResetDate) {
            // 如果日期不一致，重置今日收益和观看广告数量
            mmkv.encode(TODAY_REVENUE_KEY, 0.0)
            mmkv.encode(WATCHED_ADS_KEY, 0)
            mmkv.encode(LAST_RESET_DATE_KEY, today)
            _todayRevenue.postValue(0.0)
            _watchedAds.postValue(0)
            LogUtils.d("AdRevenueViewModel", "每日统计已重置")
        }
    }
} 