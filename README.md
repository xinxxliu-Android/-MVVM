# MVVMDemo

一个基于MVVM架构的Android应用示例。

## 功能特性

- MVVM架构模式
- 动态Toolbar标题更新
- Fragment切换
- 网络请求处理
- 状态管理

## Toolbar标题动态更新功能

### 功能说明

MainActivity中的Toolbar是公用的，通过切换不同的Fragment可以动态更改对应的标题。

### 实现方式

1. **MainActivity实现ToolbarTitleListener接口**
   - 提供`updateToolbarTitle(title: String)`方法
   - 在切换Fragment时自动更新标题

2. **BaseFragment提供标题更新方法**
   - 继承BaseFragment的Fragment可以直接调用`updateToolbarTitle(title)`
   - 在`initView()`或`onViewCreated()`中设置标题

3. **手动实现标题更新**
   - 对于没有继承BaseFragment的Fragment，可以手动获取ToolbarTitleListener并调用更新方法

### 使用示例

#### 在Fragment中设置标题

```kotlin
// 继承BaseFragment的Fragment
override fun initView() {
    super.initView()
    updateToolbarTitle(getString(R.string.home)) // 设置首页标题
}

// 或者手动实现
private fun updateToolbarTitle(title: String) {
    val activity = activity as? ToolbarTitleListener
    activity?.updateToolbarTitle(title)
}
```

#### 标题映射

- 首页: `R.string.home` -> "首页"
- 广场: `R.string.square` -> "广场"  
- 收藏: `R.string.collect` -> "收藏"

### 自动标题更新

当通过底部导航栏或侧滑菜单切换Fragment时，MainActivity会自动根据当前Fragment更新Toolbar标题。

## 项目结构

```
app/src/main/java/com/example/mvvmdemo/
├── base/                    # 基础类
│   ├── BaseActivity.kt     # 基础Activity
│   ├── BaseFragment.kt     # 基础Fragment（包含标题更新功能）
│   └── BaseViewModel.kt    # 基础ViewModel
├── fragment/               # Fragment
│   ├── HomeFragment.kt     # 首页Fragment
│   ├── SquareFragment.kt   # 广场Fragment
│   └── CollectFragment.kt  # 收藏Fragment
├── MainActivity.kt         # 主Activity（实现ToolbarTitleListener）
└── ToolbarTitleListener.kt # Toolbar标题更新接口
```

## 技术栈

- Kotlin
- MVVM架构
- ViewBinding
- LiveData
- Retrofit
- Glide
- EventBus

## 运行说明

1. 克隆项目
2. 在Android Studio中打开
3. 同步Gradle依赖
4. 运行应用

## 注意事项

- 确保所有Fragment都正确设置了标题
- 如果Fragment没有继承BaseFragment，需要手动实现标题更新
- 标题字符串资源在`strings.xml`中定义