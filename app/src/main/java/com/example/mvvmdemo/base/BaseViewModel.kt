package com.example.mvvmdemo.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmdemo.net.UiState
import kotlinx.coroutines.launch

/**
 * ViewModel基类
 * 
 * 功能：
 * 1. 提供统一的UI状态管理
 * 2. 封装网络请求处理
 * 3. 管理协程生命周期
 * 
 * 使用方式：
 * 1. 继承此类创建具体的ViewModel
 * 2. 使用launchRequest处理网络请求
 * 3. 通过LiveData观察数据变化
 */
open class BaseViewModel : ViewModel() {
    /**
     * 通用的UI状态LiveData
     * 用于在需要时监听通用UI状态变化
     */
    private val _uiState = MutableLiveData<UiState<Any>>()
    val uiState: LiveData<UiState<Any>> = _uiState

    /**
     * 统一的网络请求处理方法
     * 
     * @param block 挂起函数，返回UiState类型的数据
     * @param liveData 需要更新状态的LiveData对象
     * @param isEmpty 判断数据是否为空的函数，用于处理空数据状态
     * 
     * 使用示例：
     * launchRequest(
     *     block = { repository.getData() },
     *     liveData = _dataState,
     *     isEmpty = { it == null || it.isEmpty() }
     * )
     */
    protected fun <T> launchRequest(
        block: suspend () -> UiState<T>,
        liveData: MutableLiveData<UiState<T>>,
        isEmpty: ((T?) -> Boolean)? = null
    ) {
        liveData.value = UiState.Loading
        viewModelScope.launch {
            val result = block()
            if (result is UiState.Success && isEmpty != null && isEmpty(result.data)) {
                liveData.value = UiState.Empty
            } else {
                liveData.value = result
            }
        }
    }
}