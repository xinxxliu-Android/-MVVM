package com.example.mvvmdemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blankj.utilcode.util.LogUtils
import com.example.mvvmdemo.base.BaseActivity
import com.example.mvvmdemo.databinding.ActivityRewardAdBinding
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.vm.RewardAdViewModel
import com.sigmob.windad.WindAdError
import com.sigmob.windad.WindAds
import com.sigmob.windad.rewardVideo.WindRewardAdRequest
import com.sigmob.windad.rewardVideo.WindRewardInfo
import com.sigmob.windad.rewardVideo.WindRewardVideoAd
import com.sigmob.windad.rewardVideo.WindRewardVideoAdListener

class RewardAdActivity : BaseActivity<ActivityRewardAdBinding,RewardAdViewModel>() {
   // 替换为您在Sigmob平台申请的激励视频广告位ID
   private val placementId = "95c505808825306bba57271b66f81633" // 使用您的Sigmob Public Key作为测试
    override val viewModel: RewardAdViewModel by viewModels()
    private var rewardVideoAd: WindRewardVideoAd? = null

    override fun getViewBinding() = ActivityRewardAdBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        // 安装启动画面
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        binding.btnLoadAd.setOnClickListener{
            loadRewardVideoAd()
        }
         // 展示广告按钮点击事件
         binding.btnShowAd.setOnClickListener {
            showRewardVideoAd()
        }
    }

    override fun observeViewModel() {
        viewModel.adLoadState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
//                    binding.progressBar.visibility = android.view.View.VISIBLE
//                    binding.tvStatus.text = "正在加载激励广告..."
                }
                is UiState.Success -> {
//                    binding.progressBar.visibility = android.view.View.GONE
//                    binding.tvStatus.text = "激励广告加载成功，可以观看"
//                    binding.btnShowAd.isEnabled = true
                }
                is UiState.Error -> {
//                    binding.progressBar.visibility = android.view.View.GONE
//                    binding.tvStatus.text = "激励广告加载失败: ${state.message}"
//                    binding.btnShowAd.isEnabled = false
                }
                else -> {}
            }
        }
        viewModel.rewardState.observe(this) { state ->
            if (state is UiState.Success) {
                Toast.makeText(this, "恭喜获得${state.data}个奖励!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun loadRewardVideoAd() {
        viewModel.setAdLoadingState()

        // 创建广告请求对象
        val options = HashMap<String, String>()
        val request = WindRewardAdRequest(placementId, "user123", options as Map<String, Any>?)
        
        // 允许锁屏播放和保持屏幕常亮（可选）
        request.setEnableScreenLockDisPlayAd(true)
        request.setEnableKeepOn(true)

        // 创建激励视频广告对象
        rewardVideoAd = WindRewardVideoAd(request)
        
        // 设置广告监听器
        rewardVideoAd?.setWindRewardVideoAdListener(object : WindRewardVideoAdListener {
            // 广告数据返回成功（仅Sigmob渠道有此回调）
            override fun onRewardAdPreLoadSuccess(placementId: String) {
                LogUtils.d("激励视频广告数据返回成功")
            }

            // 广告数据返回失败（仅Sigmob渠道有此回调）
            override fun onRewardAdPreLoadFail(placementId: String) {
                LogUtils.d("激励视频广告数据返回失败")
                runOnUiThread {
                    viewModel.setAdLoadFailed("广告数据返回失败")
                }
            }

            // 广告加载成功
            override fun onRewardAdLoadSuccess(placementId: String) {
                LogUtils.d("激励视频广告加载成功")
                runOnUiThread {
                    viewModel.setAdLoadSuccess()
                }
            }

            // 广告加载失败
            override fun onRewardAdLoadError(error: WindAdError, placementId: String) {
                LogUtils.d("激励视频广告加载失败: ${error.message}")
                runOnUiThread {
                    viewModel.setAdLoadFailed(error.message)
                }
            }

            // 广告开始播放
            override fun onRewardAdPlayStart(placementId: String) {
                LogUtils.d("激励视频广告开始播放")
            }

            // 广告播放失败
            override fun onRewardAdPlayError(error: WindAdError, placementId: String) {
                LogUtils.d("激励视频广告播放失败: ${error.message}")
            }

            // 广告播放结束
            override fun onRewardAdPlayEnd(placementId: String) {
                LogUtils.d("激励视频广告播放结束")
            }

            // 广告被点击
            override fun onRewardAdClicked(placementId: String) {
                LogUtils.d("激励视频广告被点击")
            }

            // 用户获得奖励
            override fun onRewardAdRewarded(rewardInfo: WindRewardInfo, placementId: String) {
                LogUtils.d("用户获得奖励")
                if (rewardInfo.isReward()) {
                    runOnUiThread {
                        viewModel.giveReward()
                    }
                }
            }

            // 广告关闭
            override fun onRewardAdClosed(placementId: String) {
                LogUtils.d("激励视频广告关闭")
                runOnUiThread {
                    binding.btnShowAd.isEnabled = false
                    binding.tvStatus.text = "广告已关闭，可以重新加载"
                }
            }
        })

        // 加载广告
        rewardVideoAd?.loadAd()
    }
    private fun showRewardVideoAd() {
        if (rewardVideoAd != null && rewardVideoAd?.isReady() == true) {
            // 设置播放场景参数（可选）
            val showOption = HashMap<String, String>()
            showOption[WindAds.AD_SCENE_ID] = "1001"
            showOption[WindAds.AD_SCENE_DESC] = "激励视频播放场景"
            
            // 展示广告
            rewardVideoAd?.show(showOption)
        } else {
            Toast.makeText(this, "广告尚未准备好，请先加载广告", Toast.LENGTH_SHORT).show()
        }
    }

    override fun showEmptyView() {
       
    }
    override fun onDestroy() {
        super.onDestroy()
        rewardVideoAd?.destroy()
        rewardVideoAd = null
    }
}