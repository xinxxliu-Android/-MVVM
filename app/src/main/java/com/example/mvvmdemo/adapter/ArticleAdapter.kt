package com.example.mvvmdemo.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.mvvmdemo.R
import com.example.mvvmdemo.data.Article
import com.example.mvvmdemo.utils.UserManager

/**
 * 文章列表适配器 - 使用BaseRecyclerViewAdapterHelper框架
 * 
 * 功能：
 * 1. 展示文章列表数据
 * 2. 处理文章项的点击事件
 * 3. 处理收藏功能（需要登录）
 * 4. 与StateLayout和SmartRefreshLayout配合使用
 * 
 * 使用方式：
 * 1. 创建实例并设置给RecyclerView
 * 2. 通过setList更新数据
 * 3. 通过addData加载更多数据
 * 4. 通过setOnItemClickListener设置点击事件
 * 5. 通过setOnLikeClickListener设置收藏事件
 * 
 * 注意：
 * - 空数据状态由StateLayout处理
 * - 加载更多由SmartRefreshLayout处理
 * - 此适配器专注于数据绑定和交互
 */
class ArticleAdapter : BaseQuickAdapter<Article, BaseViewHolder>
    (R.layout.item_article) {

    private var onLikeClickListener: ((Article, Int) -> Unit)? = null
    private var activityContext: Context? = null
    
    // 存储文章的收藏状态
    private val collectedArticles = mutableSetOf<Int>()

    init {
        // 开启动画效果
        animationEnable = true
        
        // 设置动画类型
        setAnimationWithDefault(AnimationType.AlphaIn)
    }

    override fun convert(holder: BaseViewHolder, item: Article) {
        // 绑定文章数据到视图
        holder.setText(R.id.tvTitle, item.title)
        holder.setText(R.id.tvAuthor, "作者：${item.author.ifEmpty { item.shareUser ?: "佚名" }}")
        holder.setText(R.id.tvDate, item.niceDate)
        
        // 设置收藏按钮点击事件
        holder.getView<android.widget.ImageView>(R.id.ivLike).setOnClickListener {
            handleCollectClick(item, holder.adapterPosition)
        }
        
        // 设置收藏按钮状态（根据是否已收藏）
        updateCollectButtonState(holder, item)
    }

    /**
     * 处理收藏点击事件
     */
    private fun handleCollectClick(article: Article, position: Int) {
        activityContext?.let { ctx ->
            // 调试：打印登录状态
//            UserManager.debugLoginStatus()
            
            // 检查登录状态
            if (UserManager.isLoggedIn()) {
                // 已登录，执行收藏操作
                println("User is logged in, executing collect operation")
                onLikeClickListener?.invoke(article, position)
            } else {
                // 未登录，跳转到登录页面
                println("User is not logged in, redirecting to login page")
                UserManager.goToLogin(ctx)
            }
        }
    }

    /**
     * 更新收藏按钮状态
     */
    private fun updateCollectButtonState(holder: BaseViewHolder, article: Article) {
        val collectButton = holder.getView<android.widget.ImageView>(R.id.ivLike)
        val isCollected = collectedArticles.contains(article.id)
        
        // 设置按钮的选中状态，这会自动切换背景图片
        collectButton.isSelected = isCollected
        
        // 也可以手动设置背景
        // collectButton.setImageResource(if (isCollected) R.drawable.ic_collect else R.drawable.ic_collect_not)
    }

    /**
     * 设置收藏点击监听器
     */
    fun setOnLikeClickListener(listener: (Article, Int) -> Unit) {
        onLikeClickListener = listener
    }

    /**
     * 设置Context（用于登录检查）
     */
    fun setActivityContext(context: Context) {
        this.activityContext = context
    }

    /**
     * 更新文章收藏状态
     */
    fun updateArticleCollectState(position: Int, isCollected: Boolean) {
        if (position in 0 until data.size) {
            val article = data[position]
            if (isCollected) {
                collectedArticles.add(article.id)
            } else {
                collectedArticles.remove(article.id)
            }
            // 通知适配器更新指定位置
            notifyItemChanged(position)
        }
    }

    /**
     * 设置文章收藏状态（用于初始化）
     */
    fun setArticleCollectState(articleId: Int, isCollected: Boolean) {
        if (isCollected) {
            collectedArticles.add(articleId)
        } else {
            collectedArticles.remove(articleId)
        }
        // 通知适配器更新所有数据
        notifyDataSetChanged()
    }

    /**
     * 批量设置文章收藏状态
     */
    fun setArticlesCollectState(collectedArticleIds: List<Int>) {
        collectedArticles.clear()
        collectedArticles.addAll(collectedArticleIds)
        notifyDataSetChanged()
    }

    /**
     * 获取文章的收藏状态
     */
    fun isArticleCollected(articleId: Int): Boolean {
        return collectedArticles.contains(articleId)
    }

    /**
     * 清空收藏状态
     */
    fun clearCollectStates() {
        collectedArticles.clear()
        notifyDataSetChanged()
    }

    /**
     * 提交新的数据列表
     * 用于下拉刷新时更新数据
     * 
     * @param list 新的文章列表数据
     */
    fun submitList(list: List<Article>?) {
        setList(list ?: emptyList())
    }

    /**
     * 添加更多数据
     * 用于加载更多时追加数据
     * 
     * @param list 要追加的文章列表数据
     */
    fun addList(list: List<Article>?) {
        if (list != null) {
            addData(list)
        }
    }

    /**
     * 获取当前数据列表
     */
    fun getDataList(): List<Article> {
        return data
    }

    /**
     * 清空数据
     */
    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }
}