package com.example.mvvmdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.mvvmdemo.base.BaseActivity
import com.example.mvvmdemo.databinding.ActivityMainBinding
import com.example.mvvmdemo.fragment.CollectFragment
import com.example.mvvmdemo.fragment.HomeFragment
import com.example.mvvmdemo.fragment.OfficialFragment
import com.example.mvvmdemo.fragment.SquareFragment
import com.example.mvvmdemo.utils.UserManager
import com.example.mvvmdemo.vm.MainViewModel

/**
 * Toolbar标题更新接口
 */
interface ToolbarTitleListener {
    fun updateToolbarTitle(title: String)
}

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), ToolbarTitleListener {
    override val viewModel: MainViewModel by viewModels()

    private lateinit var homeFragment: HomeFragment
    private lateinit var squareFragment: SquareFragment
    private lateinit var collectFragment: CollectFragment
    private lateinit var officialFragment : OfficialFragment
    private var currentFragmentTag: String = "" // Keep track of current fragment by tag

    companion object {
        private const val TAG_HOME_FRAGMENT = "HomeFragment"
        private const val TAG_SQUARE_FRAGMENT = "SquareFragment"
        private const val TAG_COLLECT_FRAGMENT = "CollectFragment"
        private const val TAG_OFFICIAL_FRAGMENT = "OfficialFragment"
    }

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initView() {
        initToolbar()
        initDrawerLayout()
        initFragments()
        setupNavigationListeners()
    }

    /**
     * 初始化Toolbar
     */
    private fun initToolbar() {
        setSupportActionBar(binding.appBarLayout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.appBarLayout.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    /**
     * 初始化抽屉布局
     */
    private fun initDrawerLayout() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBarLayout.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun initFragments() {
        homeFragment = supportFragmentManager.findFragmentByTag(TAG_HOME_FRAGMENT) as? HomeFragment ?: HomeFragment()
        squareFragment = supportFragmentManager.findFragmentByTag(TAG_SQUARE_FRAGMENT) as? SquareFragment ?: SquareFragment()
        collectFragment = supportFragmentManager.findFragmentByTag(TAG_COLLECT_FRAGMENT) as? CollectFragment ?: CollectFragment()
        officialFragment = supportFragmentManager.findFragmentByTag(TAG_OFFICIAL_FRAGMENT) as? OfficialFragment ?: OfficialFragment()

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
        if (!officialFragment.isAdded){
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, officialFragment, TAG_OFFICIAL_FRAGMENT).hide(officialFragment).commit()
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
                R.id.action_wechat -> switchFragment(officialFragment, TAG_OFFICIAL_FRAGMENT)
            }
            true
        }
    }

    /**
     * 更新Toolbar标题
     * @param title 要显示的标题
     */
    override fun updateToolbarTitle(title: String) {
        // 使用自定义的TextView显示标题
        binding.appBarLayout.tvTitle.text = title
        binding.appBarLayout.tvTitle.visibility = View.VISIBLE
        // 隐藏Toolbar的默认标题
        binding.appBarLayout.toolbar.title = ""
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
        
        // 根据当前fragment更新Toolbar标题
        updateToolbarTitleByFragment(targetTag)
    }

    /**
     * 根据fragment标签更新Toolbar标题
     * @param fragmentTag fragment的标签
     */
    private fun updateToolbarTitleByFragment(fragmentTag: String) {
        val title = when (fragmentTag) {
            TAG_HOME_FRAGMENT -> getString(R.string.home)
            TAG_SQUARE_FRAGMENT -> getString(R.string.square)
            TAG_COLLECT_FRAGMENT -> getString(R.string.collect)
            TAG_OFFICIAL_FRAGMENT ->getString(R.string.wechat)
            else -> getString(R.string.app_name)
        }
        updateToolbarTitle(title)
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