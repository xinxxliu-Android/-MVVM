package com.example.mvvmdemo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.mvvmdemo.adapter.ArticleAdapter
import com.example.mvvmdemo.base.BaseFragment
import com.example.mvvmdemo.data.Article
import com.example.mvvmdemo.data.Banner
import com.example.mvvmdemo.databinding.FragmentHomeBinding
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.vm.HomeFragmentViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.example.mvvmdemo.utils.UserManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.example.mvvmdemo.event.CollectEvent

/**
 * 首页Fragment
 * 
 * 功能：
 * 1. 展示文章列表
 * 2. 展示轮播图
 * 3. 支持下拉刷新和加载更多
 * 4. 处理加载状态和错误状态
 * 
 * 使用方式：
 * 1. 通过FragmentManager添加到容器中
 * 2. 自动加载文章列表和轮播图数据
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>() {
    /**
     * 文章列表适配器
     */
    private lateinit var articleAdapter: ArticleAdapter

    /**
     * ViewModel实例
     */
    override val viewModel: HomeFragmentViewModel by viewModels()

    /**
     * 当前页码
     */
    private var page = 0

    /**
     * 获取ViewBinding实例
     */
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)

    /**
     * 初始化视图
     * 设置RecyclerView、下拉刷新、加载更多等
     */
    override fun initView() {
        super.initView()
        // 初始化RecyclerView
        articleAdapter = ArticleAdapter()
        binding.recycle.layoutManager = LinearLayoutManager(context)
        binding.smartRoot.setRefreshHeader(ClassicsHeader(context))
        binding.smartRoot.setRefreshFooter(ClassicsFooter(context))
        
        // 设置适配器Context（用于登录检查）
        articleAdapter.setActivityContext(requireContext())
        
        // 设置适配器点击事件
        articleAdapter.setOnItemClickListener { adapter, view, position ->
            val article = adapter.data[position]
            // TODO: 处理文章点击事件，跳转到文章详情页
//            ToastUtils.showShort("点击了文章：${article.title}")
        }
        
        // 设置适配器长按事件 - 使用正确的API
        articleAdapter.setOnItemLongClickListener { adapter, view, position ->
            val article = adapter.data[position]
            // TODO: 处理文章长按事件，显示操作菜单
//            ToastUtils.showShort("长按了文章：${article.title}")
            true // 返回true表示消费了长按事件
        }
        
        // 设置适配器收藏事件
        articleAdapter.setOnLikeClickListener { article, position ->
            // TODO: 处理收藏逻辑，调用API
            handleCollectArticle(article, position)
        }
        
        // 下拉刷新
        binding.smartRoot.setOnRefreshListener {
            page = 0 // 重置页码
            viewModel.fetchHomeArticles(page)
            binding.stateLayout.showContent()
        }
        
        // 加载更多
        binding.smartRoot.setOnLoadMoreListener {
            page++
            viewModel.fetchHomeArticles(page)
        }
        
        binding.recycle.adapter = articleAdapter
        
        // 设置重试回调
        binding.stateLayout.setOnRetryClickListener {
            page = 0 // 重置页码
            viewModel.fetchHomeArticles(0)
        }

        // 首次加载
        viewModel.fetchHomeArticles(0)
        viewModel.fetchHomeBanner()
    }

    /**
     * 观察ViewModel数据变化
     * 处理文章列表和轮播图数据更新
     */
    override fun observeViewModel() {
        // 观察轮播图数据
        viewModel.bannerState.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    val bannerData = state.data
                    binding.banner.addBannerLifecycleObserver(this)
                        .setAdapter(object : BannerImageAdapter<Banner>(bannerData) {
                            override fun onBindView(
                                holder: BannerImageHolder,
                                data: Banner,
                                position: Int,
                                size: Int
                            ) {
                                Glide.with(holder.itemView)
                                    .load(data.imagePath)
                                    .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                                    .into(holder.imageView)
                            }
                        })
                        .setIndicator(CircleIndicator(requireContext()))
                }
                is UiState.Error -> {
                    binding.stateLayout.showError("轮播图加载失败")
                }
                else -> {}
            }
        }

        // 观察文章列表数据
        viewModel.articleState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    if (page == 0) {
                        // 首次加载或下拉刷新时显示加载状态
                        binding.stateLayout.showLoading()
                    }
                    // 加载更多时不显示加载状态，由SmartRefreshLayout处理
                }
                is UiState.Success -> {
                    if (page == 0) {
                        // 下拉刷新
                        if (state.data.isEmpty()) {
                            // 没有数据，显示空状态
                            binding.stateLayout.showEmpty()
                        } else {
                            // 有数据，显示内容
                            binding.stateLayout.showContent()
                            articleAdapter.submitList(state.data)
                        }
                        binding.smartRoot.finishRefresh()
                    } else {
                        // 加载更多
                        if (state.data.isEmpty()) {
                            // 没有更多数据
                            binding.smartRoot.finishLoadMoreWithNoMoreData()
                        } else {
                            // 添加更多数据
                            articleAdapter.addList(state.data)
                            binding.smartRoot.finishLoadMore()
                        }
                    }
                }
                is UiState.Empty -> {
                    if (page == 0) {
                        // 首次加载没有数据
                        binding.stateLayout.showEmpty()
                    } else {
                        // 加载更多没有数据
                        binding.smartRoot.finishLoadMoreWithNoMoreData()
                    }
                }
                is UiState.Error -> {
                    if (page == 0) {
                        // 首次加载失败
                        binding.stateLayout.showError("加载失败")
                    } else {
                        // 加载更多失败
                        binding.smartRoot.finishLoadMore(false)
                        ToastUtils.showShort("加载更多失败")
                    }
                }
            }
        }

        // 观察收藏状态
        viewModel.collectState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    // 可以显示收藏中的状态
                }
                is UiState.Success -> {
                    // 收藏操作成功，UI已经在handleCollectArticle中更新
                    ToastUtils.showShort("操作成功")
                }
                is UiState.Error -> {
                    // 收藏操作失败，恢复原状态
                    ToastUtils.showShort(state.message ?: "操作失败")
                }
                else -> {}
            }
        }
    }
    /**
     * 显示空数据视图
     * 实现BaseFragment的抽象方法
     */
    override fun showEmptyView() {
       binding.stateLayout.showEmpty()
    }
    /**
     * 显示加载中视图
     * 重写BaseFragment的方法
     */
    override fun showLoading() {
        binding.stateLayout.showLoading()
    }
    /**
     * 显示错误视图
     * 重写BaseFragment的方法
     */
    override fun showError(message: String?) {
        binding.stateLayout.showError(message)
    }

    /**
     * 显示成功视图
     * 重写BaseFragment的方法
     */
    override fun onSuccess(data: Any?) {
        binding.stateLayout.showContent()
    }

    /**
     * 处理文章收藏
     */
    private fun handleCollectArticle(article: Article, position: Int) {
        // 获取当前收藏状态
        val currentCollectedState = articleAdapter.isArticleCollected(article.id)
        
        // 先更新UI状态（乐观更新）
        articleAdapter.updateArticleCollectState(position, !currentCollectedState)
        
        // 调用API
        if (currentCollectedState) {
            // 当前是收藏状态，执行取消收藏
            viewModel.uncollectArticle(article.id)
        } else {
            // 当前是未收藏状态，执行收藏
            viewModel.collectArticle(article.id)
        }

        // 发送收藏状态变化事件
        EventBus.getDefault().post(CollectEvent(article.id, !currentCollectedState))
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCollectEvent(event: CollectEvent) {
        // 更新文章收藏状态
        val position = articleAdapter.data.indexOfFirst { it.id == event.articleId }
        if (position != -1) {
            articleAdapter.updateArticleCollectState(position, event.isCollected)
        }
    }
}