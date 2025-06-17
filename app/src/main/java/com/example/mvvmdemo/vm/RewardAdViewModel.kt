package com.example.mvvmdemo.vm

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.example.mvvmdemo.MyApp
import com.example.mvvmdemo.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RewardAdViewModel : BaseViewModel() {

    private val _adLoadStatus = MutableLiveData<Boolean>()
    val adLoadStatus: LiveData<Boolean> = _adLoadStatus

    fun loadRewardAd(application: MyApp) {
//        if (!application.hasAgreedPrivacy()) {
//            LogUtils.e("RewardAdViewModel", "用户未同意隐私政策，无法加载广告")
//            _adLoadStatus.postValue(false)
//            return
//        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
//                application.loadRewardAd()
//                _adLoadStatus.postValue(application.isRewardAdReady())
            } catch (e: Exception) {
                LogUtils.e("RewardAdViewModel", "加载广告失败: ${e.message}")
                _adLoadStatus.postValue(false)
            }
        }
    }

    fun showRewardAd(application: MyApp, activity: Activity) {
//        if (!application.hasAgreedPrivacy()) {
//            LogUtils.e("RewardAdViewModel", "用户未同意隐私政策，无法显示广告")
//            return
//        }

//        if (application.isRewardAdReady()) {
//            application.showRewardAd(activity)
//        } else {
//            LogUtils.e("RewardAdViewModel", "广告未准备好")
//        }
    }

//    fun isAdReady(application: MyApp): Boolean {
//        return application.isRewardAdReady()
//    }
}