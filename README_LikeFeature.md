# 点赞功能实现说明

## 概述

本项目实现了文章点赞功能，包含登录状态检查和用户信息管理。

## 功能特性

### 1. 登录状态检查
- 点击点赞按钮时自动检查登录状态
- 未登录时自动跳转到登录页面
- 已登录时执行点赞操作

### 2. 用户信息管理
- 使用MMKV存储用户登录状态
- 登录成功后自动保存用户信息
- 支持用户token管理

### 3. 点赞交互
- 支持点赞状态切换
- 实时更新UI状态
- 提供点赞回调接口

## 实现架构

### 1. UserManager - 用户管理工具类

```kotlin
object UserManager {
    // 检查登录状态
    fun isLoggedIn(): Boolean
    
    // 获取用户信息
    fun getCurrentUser(): User?
    
    // 保存用户信息
    fun saveUser(user: User)
    
    // 清除用户信息
    fun clearUser()
    
    // 跳转登录页面
    fun goToLogin(context: Context)
    
    // 检查登录并跳转
    fun checkLoginAndGoToLogin(context: Context): Boolean
}
```

### 2. ArticleAdapter - 文章适配器

```kotlin
class ArticleAdapter : BaseQuickAdapter<Article, BaseViewHolder> {
    // 设置点赞监听器
    fun setOnLikeClickListener(listener: (Article, Int) -> Unit)
    
    // 设置Context（用于登录检查）
    fun setActivityContext(context: Context)
    
    // 更新点赞状态
    fun updateArticleLikeState(position: Int, isLiked: Boolean)
}
```

### 3. HomeFragment - 首页Fragment

```kotlin
class HomeFragment : BaseFragment {
    override fun initView() {
        // 设置适配器Context
        articleAdapter.setActivityContext(requireContext())
        
        // 设置点赞监听器
        articleAdapter.setOnLikeClickListener { article, position ->
            handleLikeArticle(article, position)
        }
    }
    
    private fun handleLikeArticle(article: Article, position: Int) {
        // 处理点赞逻辑
    }
}
```

## 使用流程

### 1. 用户点击点赞按钮

```kotlin
// 在ArticleAdapter中
private fun handleLikeClick(article: Article, position: Int) {
    context?.let { ctx ->
        // 检查登录状态
        if (UserManager.checkLoginAndGoToLogin(ctx)) {
            // 已登录，执行点赞操作
            onLikeClickListener?.invoke(article, position)
        }
        // 未登录时自动跳转到登录页面
    }
}
```

### 2. 登录成功后保存用户信息

```kotlin
// 在LoginActivity中 (com.example.mvvmdemo.LoginActivity)
viewModel.loginState.observe(this) { state ->
    when (state) {
        is UiState.Success -> {
            state.data?.let { user ->
                UserManager.saveUser(user)
                navigateToMain()
            }
        }
    }
}
```

### 3. 处理点赞逻辑

```kotlin
// 在HomeFragment中
private fun handleLikeArticle(article: Article, position: Int) {
    // 调用点赞API
    viewModel.likeArticle(article.id)
    
    // 更新UI状态
    articleAdapter.updateArticleLikeState(position, true)
}
```

## 数据存储

### 1. MMKV存储

```kotlin
// 存储键值
private const val KEY_USER_TOKEN = "user_token"
private const val KEY_USER_INFO = "user_info"
private const val KEY_IS_LOGGED_IN = "is_logged_in"
```

### 2. 存储内容

- **登录状态**: 布尔值，表示用户是否已登录
- **用户Token**: 字符串，用于API认证
- **用户信息**: JSON字符串，包含用户详细信息

## 扩展功能

### 1. 点赞状态持久化

```kotlin
// 在Article数据类中添加点赞状态
data class Article(
    val id: Int,
    val title: String,
    val isLiked: Boolean = false, // 添加点赞状态
    // ... 其他字段
)
```

### 2. 点赞数量显示

```kotlin
// 在布局文件中添加点赞数量显示
<TextView
    android:id="@+id/tvLikeCount"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="0" />
```

### 3. 批量点赞操作

```kotlin
// 支持批量点赞
fun batchLikeArticles(articleIds: List<Int>) {
    // 批量处理点赞逻辑
}
```

## 注意事项

### 1. 登录状态检查
- 每次点赞操作前都要检查登录状态
- 未登录时自动跳转到登录页面
- 登录成功后返回原页面

### 2. 网络请求
- 点赞操作需要调用后端API
- 处理网络请求失败的情况
- 支持离线点赞（本地缓存）

### 3. UI更新
- 点赞状态变化时及时更新UI
- 避免重复点击
- 提供加载状态提示

### 4. 数据同步
- 本地点赞状态与服务器同步
- 处理数据冲突
- 支持数据恢复

## 最佳实践

### 1. 错误处理

```kotlin
private fun handleLikeArticle(article: Article, position: Int) {
    try {
        // 执行点赞操作
        viewModel.likeArticle(article.id)
    } catch (e: Exception) {
        // 处理错误
        ToastUtils.showShort("点赞失败，请重试")
    }
}
```

### 2. 防重复点击

```kotlin
private var isLikeProcessing = false

private fun handleLikeClick(article: Article, position: Int) {
    if (isLikeProcessing) return
    
    isLikeProcessing = true
    // 执行点赞操作
    // 操作完成后重置状态
    isLikeProcessing = false
}
```

### 3. 状态管理

```kotlin
// 使用ViewModel管理点赞状态
class HomeViewModel : ViewModel() {
    private val _likeState = MutableLiveData<UiState<Boolean>>()
    val likeState: LiveData<UiState<Boolean>> = _likeState
    
    fun likeArticle(articleId: Int) {
        // 处理点赞逻辑
    }
}
```

## 总结

点赞功能的实现涉及多个组件：

1. **UserManager**: 管理用户登录状态和信息
2. **ArticleAdapter**: 处理点赞交互和UI更新
3. **HomeFragment**: 协调点赞逻辑和状态管理
4. **LoginActivity**: 处理登录和用户信息保存

通过合理的架构设计和状态管理，实现了完整的点赞功能，包括登录检查、状态管理和UI更新。 