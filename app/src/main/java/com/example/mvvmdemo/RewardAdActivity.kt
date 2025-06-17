package com.example.mvvmdemo

import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmdemo.base.BaseActivity
import com.example.mvvmdemo.databinding.ActivityRewardAdBinding
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.vm.AdRevenueViewModel
import com.sigmob.windad.WindAdError
import com.sigmob.windad.WindAds
import com.sigmob.windad.rewardVideo.WindRewardAdRequest
import com.sigmob.windad.rewardVideo.WindRewardInfo
import com.sigmob.windad.rewardVideo.WindRewardVideoAd
import com.sigmob.windad.rewardVideo.WindRewardVideoAdListener


class RewardAdActivity : BaseActivity<ActivityRewardAdBinding, AdRevenueViewModel>() {
    private var windRewardVideoAd: WindRewardVideoAd? = null
    private val placementId: String = "ea1f8ea2d90"
    override val viewModel: AdRevenueViewModel by lazy {
        ViewModelProvider(this).get(
            AdRevenueViewModel::class.java
        )
    }

    override fun getViewBinding(): ActivityRewardAdBinding {
        return ActivityRewardAdBinding.inflate(layoutInflater)
    }

    override fun initView() {
        setupToolbar()
        setupListeners()
        val request = WindRewardAdRequest(placementId, null, null)
        windRewardVideoAd = WindRewardVideoAd(request)
        windRewardVideoAd?.setWindRewardVideoAdListener(object : WindRewardVideoAdListener {
            override fun onRewardAdLoadSuccess(placementId: String?) {
                Toast.makeText(applicationContext, "激励视频广告数据返回成功", Toast.LENGTH_SHORT)
                    .show();
            }

            override fun onRewardAdPreLoadSuccess(placementId: String?) {
                Toast.makeText(
                    applicationContext,
                    "激励视频广告缓存加载成功,可以播放",
                    Toast.LENGTH_SHORT
                ).show();

            }

            override fun onRewardAdPreLoadFail(placementId: String?) {
                Toast.makeText(applicationContext, "激励视频广告数据返回失败", Toast.LENGTH_SHORT)
                    .show();

            }

            override fun onRewardAdPlayStart(placementId: String?) {
                Toast.makeText(applicationContext, "激励视频广告播放开始", Toast.LENGTH_SHORT)
                    .show();

            }

            override fun onRewardAdPlayEnd(placementId: String?) {
                Toast.makeText(applicationContext, "激励视频广告播放结束", Toast.LENGTH_SHORT)
                    .show();

            }

            override fun onRewardAdClicked(placementId: String?) {
                Toast.makeText(
                    applicationContext,
                    "激励视频广告CTA点击事件监听",
                    Toast.LENGTH_SHORT
                ).show();

            }

            override fun onRewardAdClosed(placementId: String?) {
            }

            override fun onRewardAdRewarded(rewardInfo: WindRewardInfo?, placementId: String?) {
                if (rewardInfo != null) {
                    if (rewardInfo.isReward()) {
                        Toast.makeText(
                            applicationContext,
                            "激励视频广告完整播放，给予奖励",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }

            override fun onRewardAdLoadError(error: WindAdError?, placementId: String?) {
                Toast.makeText(applicationContext, "激励视频广告加载错误", Toast.LENGTH_SHORT)
                    .show();

            }

            override fun onRewardAdPlayError(error: WindAdError?, placementId: String?) {
                Toast.makeText(applicationContext, "激励视频广告播放错误", Toast.LENGTH_SHORT)
                    .show();

            }

        })

        // 加载广告统计数据
        viewModel.loadAdStats()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "广告收益平台"
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupListeners() {
        binding.btnLoadAd.setOnClickListener { loadAd() }
        binding.btnWatchAd.setOnClickListener { watchAd() }
    }

    /**
     * 加载广告
     */
    private fun loadAd() {
        windRewardVideoAd?.loadAd()
    }

    /**
     * 观看广告
     */
    private fun watchAd() {
        if (windRewardVideoAd != null && windRewardVideoAd!!.isReady) {
           val showOption = HashMap<String,String>()
            showOption.put(WindAds.AD_SCENE_ID,placementId)
            showOption.put(WindAds.AD_SCENE_DESC,"测试场景")
            windRewardVideoAd!!.show(showOption)
        }
    }

    override fun observeViewModel() {
        viewModel.todayRevenue.observe(this) {
            binding.tvTodayRevenue.text = "%.2f 元".format(it)
        }
        viewModel.totalRevenue.observe(this) {
            binding.tvTotalRevenue.text = "%.2f 元".format(it)
        }
        viewModel.watchedAds.observe(this) {
            binding.tvWatchedAds.text = "${it} 个广告"
        }

        // 观察广告统计加载结果和奖励添加结果
        viewModel.adStatsLoadResult.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    // 可以显示加载指示器
                }

                is UiState.Success -> {
                    // 数据加载成功或奖励添加成功，Toast已在observeAdRewardEvent中处理
                }

                is UiState.Error -> {
                    Toast.makeText(this, "操作失败: ${state.message}", Toast.LENGTH_SHORT).show()
                }

                is UiState.Empty -> {
                    // 通常不会有空状态，但可以处理
                }
            }
        }
        observeAdRewardEvent()
    }

    private fun observeAdRewardEvent() {
//        MyApp.adRewardedEvent.observe(this) { rewardAmount ->
//            viewModel.addAdReward(rewardAmount)
//            Toast.makeText(this, "恭喜您，获得 ${rewardAmount} 元广告收益！", Toast.LENGTH_SHORT).show()
//        }
    }

    override fun showEmptyView() {
        // TODO: 根据需要实现空页面显示逻辑
    }
} 