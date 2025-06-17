package com.example.mvvmdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.blankj.utilcode.util.LogUtils
import com.example.mvvmdemo.databinding.ActivitySplashAdBinding
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.vm.SplashViewAdModel
import com.sigmob.windad.Splash.WindSplashAD
import com.sigmob.windad.Splash.WindSplashADListener
import com.sigmob.windad.Splash.WindSplashAdRequest
import com.sigmob.windad.WindAdError

/**
 * 启动页Activity
 * 
 * 功能：
 * 1. 显示启动画面
 * 2. 加载广告
 * 3. 处理隐私政策
 * 
 * 使用方式：
 * 1. 作为应用入口Activity
 * 2. 自动处理启动画面和广告加载
 */
class SplashAdActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashAdBinding
    private val viewModel = SplashViewAdModel()
    
    // 替换为您在Sigmob平台申请的开屏广告位ID
    private val placementId: String = "ea1f8f21300"
    private var canJumpImmediately = false
    private var splashAd: WindSplashAD? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // 安装启动画面
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        binding = ActivitySplashAdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 设置启动画面退出监听
        splashScreen.setKeepOnScreenCondition {
            // 返回true表示保持启动画面，false表示退出启动画面
            // 这里可以根据需要控制启动画面的显示时间
            false
        }

        // 检查隐私政策
        if (!hasAgreedPrivacy()) {
            showPrivacyDialog()
        } else {
            // 加载广告
            loadAd()
        }

        // 观察广告加载状态
        observeViewModel()
    }
    
    /**
     * 加载广告
     */
    private fun loadAd() {
        viewModel.setAdLoadingState()
        binding.progressBar.visibility = View.VISIBLE

        // 创建开屏广告请求对象
        val adRequest = WindSplashAdRequest(placementId, null, null)
        // 设置广告加载超时时间，单位秒
        adRequest.fetchDelay = 5
        // 设置广告结束后是否自动隐藏，单独Activity建议设为true
        adRequest.isDisableAutoHideAd = true
        // 创建开屏广告对象
        splashAd = WindSplashAD(adRequest, object : WindSplashADListener {
            override fun onSplashAdShow(placementId: String) {
                // 广告展示成功
                LogUtils.d("SplashAd", "onSplashAdShow")
                viewModel.setAdLoadSuccess()
                binding.progressBar.visibility = View.GONE
            }

            override fun onSplashAdLoadSuccess(placementId: String) {
                // 广告加载成功，展示广告
                LogUtils.d("SplashAd", "onSplashAdLoadSuccess")
                if (splashAd?.isReady() == true) {
                    splashAd?.show(binding.splashContainer)
                }
            }

            override fun onSplashAdLoadFail(error: WindAdError, placementId: String) {
                // 广告加载失败，直接跳转到主页面
                LogUtils.e("SplashAd", "onSplashAdLoadFail: ${error.message}")
                viewModel.setAdLoadFailed(error.message)
            }

            override fun onSplashAdClick(placementId: String) {
                // 广告被点击
                LogUtils.d("SplashAd", "onSplashAdClick")
            }

            override fun onSplashAdClose(placementId: String) {
                // 广告关闭
                LogUtils.d("SplashAd", "onSplashAdClose")
                viewModel.setAdClosed()
            }

            override fun onSplashAdSkip(placementId: String) {
                // 广告被跳过
                LogUtils.d("SplashAd", "onSplashAdSkip")
                viewModel.setAdClosed()
            }

            override fun onSplashAdShowError(error: WindAdError, placementId: String) {
                // 广告展示失败
                LogUtils.e("SplashAd", "onSplashAdShowError: ${error.message}")
                viewModel.setAdLoadFailed(error.message)
            }
        })
        // 加载广告
        splashAd?.loadAd()
    }
    
    /**
     * 显示隐私政策对话框
     */
    private fun showPrivacyDialog() {
        // TODO: 实现隐私政策对话框
        // 这里模拟用户同意隐私政策
        getSharedPreferences("app", MODE_PRIVATE).edit()
            .putBoolean("privacy_agreed", true)
            .apply()
        loadAd()
    }
    
    /**
     * 检查是否已同意隐私政策
     */
    private fun hasAgreedPrivacy(): Boolean {
        return getSharedPreferences("app", MODE_PRIVATE).getBoolean("privacy_agreed", false)
    }
    
    /**
     * 观察ViewModel数据变化
     */
    private fun observeViewModel() {
        viewModel.adLoadState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    // 显示加载中UI
                    binding.progressBar.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    // 隐藏加载UI
                    binding.progressBar.visibility = View.GONE
                    if (state.data == false) {
                        // 广告已关闭，跳转到主页面
                        jumpWhenCanClick()
                    }
                }
                is UiState.Error -> {
                    // 加载失败，跳转到主页面
                    binding.progressBar.visibility = View.GONE
                    jumpToMainActivity()
                }
                else -> {}
            }
        }
    }
    
    /**
     * 控制跳转逻辑
     * 当开屏广告为普链类广告时，点击会打开一个广告落地页，此时不能立即跳转到主页
     * 当从广告落地页返回后，才可以跳转到主页
     */
    private fun jumpWhenCanClick() {
        if (canJumpImmediately) {
            jumpToMainActivity()
        } else {
            canJumpImmediately = true
        }
    }
    
    /**
     * 跳转到主页面
     */
    private fun jumpToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    
    override fun onPause() {
        super.onPause()
        canJumpImmediately = false
    }
    
    override fun onResume() {
        super.onResume()
        if (canJumpImmediately) {
            jumpToMainActivity()
        }
        canJumpImmediately = true
    }
    
    override fun onDestroy() {
        super.onDestroy()
        splashAd?.destroy()
    }
}