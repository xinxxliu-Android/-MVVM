package com.example.mvvmdemo.net

/**
 * UI状态密封类
 * 
 * 用于统一管理UI的各种状态，包括：
 * 1. 加载中状态
 * 2. 成功状态（携带数据）
 * 3. 错误状态（携带错误信息）
 * 4. 空数据状态
 * 
 * 使用方式：
 * 1. ViewModel中返回UiState类型的数据
 * 2. View层通过when表达式处理不同状态
 * 
 * @param T 数据类型参数，表示成功状态下携带的数据类型
 */
sealed class UiState<out T> {
    /**
     * 加载中状态
     * 表示数据正在加载中，UI应显示加载动画
     */
    object Loading : UiState<Nothing>()

    /**
     * 成功状态
     * 表示数据加载成功，携带实际数据
     * 
     * @param data 成功加载的数据
     */
    data class Success<T>(val data: T) : UiState<T>()

    /**
     * 错误状态
     * 表示数据加载失败，携带错误信息
     * 
     * @param message 错误信息，可为空
     */
    data class Error(val message: String) : UiState<Nothing>()

    /**
     * 空数据状态
     * 表示数据加载成功但结果为空
     * 例如：列表数据为空
     */
    data object Empty : UiState<Nothing>()
}