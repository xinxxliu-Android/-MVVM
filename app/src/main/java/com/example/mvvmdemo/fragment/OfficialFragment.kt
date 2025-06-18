package com.example.mvvmdemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ToastUtils
import com.example.mvvmdemo.adapter.OfficialAccountAdapter
import com.example.mvvmdemo.adapter.OfficialPagerAdapter
import com.example.mvvmdemo.base.BaseFragment
import com.example.mvvmdemo.data.OfficialAccount
import com.example.mvvmdemo.databinding.FragmentOfficialBinding
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.vm.OfficialFragmentViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class OfficialFragment : BaseFragment<FragmentOfficialBinding, OfficialFragmentViewModel>() {
    override val viewModel: OfficialFragmentViewModel by viewModels()

    private var pagerAdapter: OfficialPagerAdapter? = null

    //    private lateinit var officialAccountAdapter: OfficialAccountAdapter
    companion object {
        private const val ARG_OFFICIAL_ACCOUNT = "official_account"

        fun newInstance(officialAccount: OfficialAccount): OfficialArticleFragment {
            return OfficialArticleFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_OFFICIAL_ACCOUNT, officialAccount)
                }
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOfficialBinding.inflate(inflater, container, false)

    override fun initView() {
        super.initView()
        setupViewPager()
    }

    override fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.officialAccounts.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // 显示加载状态
                        showLoading()
                    }

                    is UiState.Success -> {
                        // 显示公众号列表
                        showOfficialAccounts(state.data)
                    }

                    is UiState.Error -> {
                        // 显示错误状态
                        showError(state.message)
                    }

                    else -> {
                        // 处理其他状态
                    }
                }
            }
        }
    }

    override fun showEmptyView() {
        // 显示空状态
        binding.stateLayout.showEmpty()
    }

    override fun onResume() {
        super.onResume()
        // 页面恢复时获取公众号列表
        viewModel.getOfficialAccounts()
    }

    /**
     * 设置ViewPager
     */
    private fun setupViewPager() {
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    /**
     * 显示公众号Tab列表
     */
    private fun showOfficialAccounts(accounts: List<OfficialAccount>) {
        if (accounts.isEmpty()) {
            showEmptyView()
        } else {
            binding.stateLayout.showContent()
            setupTabs(accounts)
        }
    }

    /**
     * 设置Tab
     */
    private fun setupTabs(accounts: List<OfficialAccount>) {
        // 创建ViewPager适配器
        pagerAdapter = OfficialPagerAdapter(requireActivity(), accounts)
        binding.viewPager.adapter = pagerAdapter

        // 设置TabLayout和ViewPager的联动
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = pagerAdapter?.getPageTitle(position)
        }.attach()
    }

    /**
     * 处理公众号点击事件
     */
    private fun onOfficialAccountClick(account: com.example.mvvmdemo.data.OfficialAccount) {
        // TODO: 跳转到公众号详情页面或文章列表页面
        // 可以传递account.id作为参数
        ToastUtils.showShort("点击了公众号: ${account.name}")
    }

    /**
     * 显示加载状态
     */
    override fun showLoading() {
        binding.stateLayout.showLoading()
    }


}