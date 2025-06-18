package com.example.mvvmdemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.example.mvvmdemo.R
import com.example.mvvmdemo.adapter.ArticleAdapter
import com.example.mvvmdemo.base.BaseFragment
import com.example.mvvmdemo.databinding.FragmentCollectBinding
import com.example.mvvmdemo.databinding.FragmentHomeBinding
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.utils.UserManager
import com.example.mvvmdemo.vm.CollectViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.example.mvvmdemo.event.CollectEvent

/**
 * 收藏页面Fragment
 * 
 * 功能：
 * 1. 展示用户收藏的文章列表
 * 2. 支持下拉刷新和加载更多
 * 3. 处理登录状态检查
 * 4. 使用StateLayout管理状态
 * 
 * 使用方式：
 * 1. 在MainActivity中添加到ViewPager
 * 2. 需要登录才能访问
 */
class CollectFragment : BaseFragment<FragmentCollectBinding,CollectViewModel>() {
    override val viewModel: CollectViewModel by viewModels()
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCollectBinding.inflate(inflater, container, false)


    private lateinit var articleAdapter: ArticleAdapter
    
    private var page = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 检查登录状态
        if (!UserManager.isLoggedIn()) {
            UserManager.goToLogin(requireContext())
            return
        }

        initView()
        observeViewModel()
        // 移除首次加载时自动刷新数据
        // refreshData()
        
        // 显示初始状态，提示用户下拉刷新
        binding.stateLayout.showEmpty()
    }

    override fun initView() {
        // 初始化适配器
        articleAdapter = ArticleAdapter()
        binding.recycle.layoutManager = LinearLayoutManager(context)
        // 移除自动刷新
        // binding.smartRoot.autoRefresh()
        // 设置适配器Context（用于登录检查）
        articleAdapter.setActivityContext(requireContext())

        // 设置适配器点击事件
        articleAdapter.setOnItemClickListener { adapter, view, position ->
            val article = adapter.data[position]
            // TODO: 处理文章点击事件，跳转到文章详情页
        }

        // 设置适配器收藏事件
        articleAdapter.setOnLikeClickListener { article, position ->
            // 处理取消收藏逻辑
            handleUncollectArticle(article, position)
        }

        // 下拉刷新
        binding.smartRoot.setOnRefreshListener {
            page = 0 // 重置页码
            viewModel.fetchCollectedArticles(page)
            // 移除直接调用showContent()，让状态管理完全由observeViewModel()处理
            // binding.stateLayout.showContent()
        }


        // 加载更多
        binding.smartRoot.setOnLoadMoreListener {
            page++
            viewModel.fetchCollectedArticles(page)
        }

        binding.recycle.adapter = articleAdapter

        // 设置重试回调
        binding.stateLayout.setOnRetryClickListener {
            page = 0 // 重置页码
            viewModel.fetchCollectedArticles(0)
        }
    }

    override fun observeViewModel() {
        // 观察收藏文章列表数据
        viewModel.articleState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    if (page == 0){
                        binding.stateLayout.showLoading()
                    }

                }
                is UiState.Success -> {
                    if (page == 0) {
                        // 下拉刷新或首次加载
                        if (state.data.datas.isEmpty()) {
                            // 没有数据，显示空状态
                            binding.stateLayout.showEmpty()
                        } else {
                            // 有数据，显示内容
                            binding.stateLayout.showContent()
                            // 设置所有文章的收藏状态为已收藏
                            articleAdapter.setArticlesCollectState(state.data.datas.map { it.id })
                            articleAdapter.submitList(state.data.datas)
                        }
                        binding.smartRoot.finishRefresh()
                    } else {
                        // 加载更多
                        if (state.data.datas.isEmpty()) {
                            // 没有更多数据
                            binding.smartRoot.finishLoadMoreWithNoMoreData()
                        } else {
                            // 添加更多数据
                            // 设置新加载文章的收藏状态为已收藏
                            articleAdapter.setArticlesCollectState(state.data.datas.map { it.id })
                            articleAdapter.addList(state.data.datas)
                            binding.smartRoot.finishLoadMore()
                        }
                    }
                }
                is UiState.Empty -> {
                    if (page == 0) {
                        // 首次加载没有数据
                        binding.stateLayout.showEmpty()
                        binding.smartRoot.finishRefresh()
                    } else {
                        // 加载更多没有数据
                        binding.smartRoot.finishLoadMoreWithNoMoreData()
                    }
                }
                is UiState.Error -> {
                    if (page == 0) {
                        // 首次加载失败
                        binding.stateLayout.showError("加载失败")
                        binding.smartRoot.finishRefresh()
                    } else {
                        // 加载更多失败
                        binding.smartRoot.finishLoadMore(false)
                        ToastUtils.showShort("加载更多失败")
                    }
                }
            }
        }

        // 观察取消收藏状态
        viewModel.uncollectState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    // 取消收藏操作进行中
                }
                is UiState.Success -> {
                    // 取消收藏成功，UI已经在handleUncollectArticle中更新
                    ToastUtils.showShort("取消收藏成功")
                }
                is UiState.Error -> {
                    // 取消收藏失败，恢复原状态
                    ToastUtils.showShort(state.message ?: "取消收藏失败")
                }
                else -> {}
            }
        }
    }

    override fun showEmptyView() {
        binding.stateLayout.showEmpty()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        // 移除每次进入收藏页面都刷新数据
        // refreshData()
        // 移除自动刷新
        // binding.smartRoot.autoRefresh(2000)
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCollectEvent(event: CollectEvent) {
        // 更新文章收藏状态
        val position = articleAdapter.data.indexOfFirst { it.id == event.articleId }
        if (position != -1) {
            articleAdapter.updateArticleCollectState(position, event.isCollected)
        }
    }
    
    /**
     * 处理取消收藏
     */
    private fun handleUncollectArticle(article: com.example.mvvmdemo.data.Article, position: Int) {
        // 获取当前收藏状态
        val currentCollectedState = articleAdapter.isArticleCollected(article.id)
        
        // 先更新UI状态（乐观更新）
        articleAdapter.updateArticleCollectState(position, !currentCollectedState)
        
        // 调用取消收藏API
        viewModel.uncollectArticle(article.id)

        // 发送收藏状态变化事件
        EventBus.getDefault().post(CollectEvent(article.id, !currentCollectedState))
    }
} 