package com.example.mvvmdemo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.mvvmdemo.MainActivity
import com.example.mvvmdemo.ToolbarTitleListener
import com.example.mvvmdemo.net.UiState

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {
    protected lateinit var binding: VB
    protected abstract val viewModel: VM
    
    // Toolbar标题更新监听器
    protected var toolbarTitleListener: ToolbarTitleListener? = null

    //    子类实现，返回对应的ViewBinding实例
    abstract fun getViewBinding(inflater: LayoutInflater,
                                container: ViewGroup?): VB
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 获取ToolbarTitleListener
        toolbarTitleListener = activity as? ToolbarTitleListener
        initView()
        observeViewModel()
    }
    
    /**
     * 更新Toolbar标题
     * @param title 要显示的标题
     */
    protected fun updateToolbarTitle(title: String) {
        toolbarTitleListener?.updateToolbarTitle(title)
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
    open fun initView() {}
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