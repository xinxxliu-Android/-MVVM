package com.example.mvvmdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.mvvmdemo.base.BaseActivity
import com.example.mvvmdemo.databinding.ActivityMainBinding
import com.example.mvvmdemo.fragment.CollectFragment
import com.example.mvvmdemo.fragment.HomeFragment
import com.example.mvvmdemo.fragment.SquareFragment
import com.example.mvvmdemo.utils.UserManager
import com.example.mvvmdemo.vm.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val viewModel: MainViewModel by viewModels()

    private lateinit var homeFragment: HomeFragment
    private lateinit var squareFragment: SquareFragment
    private lateinit var collectFragment: CollectFragment
    private var currentFragmentTag: String = "" // Keep track of current fragment by tag

    companion object {
        private const val TAG_HOME_FRAGMENT = "HomeFragment"
        private const val TAG_SQUARE_FRAGMENT = "SquareFragment"
        private const val TAG_COLLECT_FRAGMENT = "CollectFragment"
    }

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size)
        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        initFragments()
        setupNavigationListeners()
    }

    private fun initFragments() {
        homeFragment = supportFragmentManager.findFragmentByTag(TAG_HOME_FRAGMENT) as? HomeFragment ?: HomeFragment()
        squareFragment = supportFragmentManager.findFragmentByTag(TAG_SQUARE_FRAGMENT) as? SquareFragment ?: SquareFragment()
        collectFragment = supportFragmentManager.findFragmentByTag(TAG_COLLECT_FRAGMENT) as? CollectFragment ?: CollectFragment()

        // Add fragments if they haven't been added yet
        if (!homeFragment.isAdded) {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, homeFragment, TAG_HOME_FRAGMENT).commit()
        }
        if (!squareFragment.isAdded) {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, squareFragment, TAG_SQUARE_FRAGMENT).hide(squareFragment).commit()
        }
        if (!collectFragment.isAdded) {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, collectFragment, TAG_COLLECT_FRAGMENT).hide(collectFragment).commit()
        }

        switchFragment(homeFragment, TAG_HOME_FRAGMENT)
    }

    private fun setupNavigationListeners() {
        // 侧滑菜单点击事件
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.my_ji_fen -> {
                    startActivity(Intent(this, PointsRedemptionActivity::class.java))
                }
                R.id.nav_favorite -> {
                    if (UserManager.checkLoginAndGoToLogin(this)) {
                        switchFragment(collectFragment, TAG_COLLECT_FRAGMENT)
                    }
                }
                R.id.nav_settings -> {
                    Toast.makeText(this, "设置功能待实现", Toast.LENGTH_SHORT).show()
                }
                R.id.about -> {
                    Toast.makeText(this, "关于功能待实现", Toast.LENGTH_SHORT).show()
                }
                R.id.light_model -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    Toast.makeText(this, "已切换到夜间模式", Toast.LENGTH_SHORT).show()
                }
                R.id.logout -> {
                    UserManager.clearUser()
                    Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show()
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }

        // 底部导航栏点击事件
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> switchFragment(homeFragment, TAG_HOME_FRAGMENT)
                R.id.action_square -> switchFragment(squareFragment, TAG_SQUARE_FRAGMENT)
            }
            true
        }
    }

    private fun switchFragment(targetFragment: Fragment, targetTag: String) {
        if (currentFragmentTag == targetTag) {
            return
        }

        val transaction = supportFragmentManager.beginTransaction()

        supportFragmentManager.findFragmentByTag(currentFragmentTag)?.let {
            transaction.hide(it)
        }

        transaction.show(targetFragment)
        transaction.commit()
        currentFragmentTag = targetTag
    }

    override fun observeViewModel() {
        // MainViewModel的LiveData观察
    }

    override fun showLoading() {
        super.showLoading()
        // binding.stateLayout.showLoading() // 如果有StateLayout，可以在这里调用
    }

    override fun onSuccess(data: Any?) {
        super.onSuccess(data)
        // binding.stateLayout.showContent() // 如果有StateLayout，可以在这里调用
    }

    override fun showError(message: String?) {
        super.showError(message)
        // binding.stateLayout.showError() // 如果有StateLayout，可以在这里调用
    }

    override fun showEmptyView() {
//        super.showEmptyView()
        // binding.stateLayout.showEmpty() // 如果有StateLayout，可以在这里调用
    }
}