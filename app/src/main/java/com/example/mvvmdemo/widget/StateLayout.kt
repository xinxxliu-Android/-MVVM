package com.example.mvvmdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.mvvmdemo.databinding.LayoutStateBinding

/**
 * 状态布局组件
 * 
 * 功能：
 * 1. 统一管理加载中、空数据、错误等状态
 * 2. 提供状态切换方法
 * 3. 支持自定义错误信息和重试回调
 * 
 * 使用方式：
 * 1. 在布局文件中使用
 * 2. 调用showLoading/showEmpty/showError等方法切换状态
 * 3. 设置重试回调处理重试事件
 */
class StateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutStateBinding = LayoutStateBinding.inflate(LayoutInflater.from(context), this)
    private var onRetryClickListener: (() -> Unit)? = null

    init {
        binding.btnRetry.setOnClickListener {
            onRetryClickListener?.invoke()
        }
    }

    /**
     * 显示加载中状态
     */
    fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.emptyView.visibility = View.GONE
        binding.errorView.visibility = View.GONE
    }

    /**
     * 显示空数据状态
     */
    fun showEmpty() {
        binding.progressBar.visibility = View.GONE
        binding.emptyView.visibility = View.VISIBLE
        binding.errorView.visibility = View.GONE
    }

    /**
     * 显示错误状态
     * 
     * @param errorMsg 错误信息
     */
    fun showError(errorMsg: String?) {
        binding.progressBar.visibility = View.GONE
        binding.emptyView.visibility = View.GONE
        binding.errorView.visibility = View.VISIBLE
        binding.tvError.text = errorMsg ?: "加载失败"
    }

    /**
     * 显示内容状态
     */
    fun showContent() {
        binding.progressBar.visibility = View.GONE
        binding.emptyView.visibility = View.GONE
        binding.errorView.visibility = View.GONE
    }

    /**
     * 设置重试点击监听器
     * 
     * @param listener 重试回调
     */
    fun setOnRetryClickListener(listener: () -> Unit) {
        onRetryClickListener = listener
    }
} 