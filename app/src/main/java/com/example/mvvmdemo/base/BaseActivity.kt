package com.example.mvvmdemo.base


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.mvvmdemo.net.UiState

/**
 * Activity基类，支持ViewBinding和通用UI状态监听
 * @param VB ViewBinding类型
 * @param VM ViewModel类型
 * 用法：所有Activity继承BaseActivity，自动支持ViewBinding和UI状态监听。
 */
abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {
    protected lateinit var binding: VB
    protected abstract val viewModel: VM

    //    子类实现，返回对应的ViewBinding实例
    abstract fun getViewBinding(): VB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        observeUiState()
        initView()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    //    监听通用UI状态，自动分发到对应方法
    private fun observeUiState() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> showLoading()
                is UiState.Success<*> -> onSuccess(state.data)
                is UiState.Error -> showError(state.message)
                is UiState.Empty -> showEmptyView()
            }
        }
    }

    /** 子类实现，初始化UI */
    abstract fun initView()

    /** 子类实现，监听专属业务LiveData */
    abstract fun observeViewModel()

    /** 子类实现，显示空页面 */
    abstract fun showEmptyView()

    /** 可选重写，显示加载中 */
    open fun showLoading() {}

    /** 可选重写，显示成功 */
    open fun onSuccess(data: Any?) {}

    /** 可选重写，显示错误 */
    open fun showError(message: String?) {}
}