package com.example.mvvmdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmdemo.adapter.ArticleAdapter
import com.example.mvvmdemo.base.BaseActivity
import com.example.mvvmdemo.databinding.ActivityMainBinding
import com.example.mvvmdemo.fragment.CollectFragment
import com.example.mvvmdemo.fragment.HomeFragment
import com.example.mvvmdemo.fragment.SquareFragment
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.utils.UserManager
import com.example.mvvmdemo.vm.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val viewModel: MainViewModel by viewModels()

    private val homeFragment = HomeFragment()
    private val squareFragment = SquareFragment()
    private val collectFragment = CollectFragment()
    private var currentFragment: Fragment = homeFragment
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
    override fun initView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size)
        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
//        默认展示HomeFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()
        currentFragment = homeFragment
//        侧滑菜单点击事件
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.my_ji_fen -> {

                }
//                    收藏页
                R.id.nav_favorite -> {
                    if (UserManager.checkLoginAndGoToLogin(this)) {
                        switchFragment(collectFragment)
                    }
                }
//                    设置页面
                R.id.nav_settings -> {

                }

                R.id.about -> {
                    // 清除登录状态用于测试
//                    UserManager.clearAllForTesting()
//                    Toast.makeText(this, "已清除登录状态，请重新登录测试", Toast.LENGTH_LONG).show()
                }

                R.id.light_model -> {
                    AppCompatDelegate.
                    setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                R.id.logout -> {
                    UserManager.clearUser()
                    Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show()
                }
            }
            binding.drawerLayout.closeDrawers()
            true

        }
//        底部导航栏点击事件
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> switchFragment(HomeFragment())
                R.id.action_square -> switchFragment(SquareFragment())
            }
            true
        }

//        // 初始化RecyclerView
//        articleAdapter = ArticleAdapter()
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.adapter = articleAdapter
//        // 重试按钮
//        binding.btnRetry.setOnClickListener {
//            viewModel.fetchHomeArticles(0)
//        }
//
//        // 首次加载
//        viewModel.fetchHomeArticles(0)


    }

    private fun switchFragment(target: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, target).commit()
        currentFragment = target
    }

    override fun observeViewModel() {

//        viewModel.articleState.observe(this) { state ->
//            when (state) {
//                is UiState.Loading -> showLoading()
//                is UiState.Success -> {
//                    showContentView()
//                    articleAdapter.submitList(state.data)
//                }
//
//                is UiState.Empty -> showEmptyView()
//                is UiState.Error -> showError(state.message)
//            }
//        }


    }

    override fun showLoading() {
        super.showLoading()
//        showLoadingView()
    }

    override fun onSuccess(data: Any?) {
//        showContentView()
    }

    override fun showError(message: String?) {
//        showErrorView(message)
    }

    // 多状态切换
    private fun showLoadingView() {
//        binding.progressBar.visibility = View.VISIBLE
//        binding.emptyView.visibility = View.GONE
//        binding.errorView.visibility = View.GONE
//        binding.recyclerView.visibility = View.GONE
    }

    override fun showEmptyView() {
//        binding.progressBar.visibility = View.GONE
//        binding.emptyView.visibility = View.VISIBLE
//        binding.errorView.visibility = View.GONE
//        binding.recyclerView.visibility = View.GONE
    }

    private fun showErrorView(msg: String?) {
//        binding.progressBar.visibility = View.GONE
//        binding.emptyView.visibility = View.GONE
//        binding.errorView.visibility = View.VISIBLE
//        binding.tvError.text = msg ?: "加载失败"
//        binding.recyclerView.visibility = View.GONE
    }

    private fun showContentView() {
//        binding.progressBar.visibility = View.GONE
//        binding.emptyView.visibility = View.GONE
//        binding.errorView.visibility = View.GONE
//        binding.recyclerView.visibility = View.VISIBLE
    }
}