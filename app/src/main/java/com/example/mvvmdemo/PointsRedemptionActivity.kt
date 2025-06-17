package com.example.mvvmdemo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmdemo.base.BaseActivity
import com.example.mvvmdemo.databinding.ActivityPointsRedemptionBinding
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.vm.PointsRedemptionViewModel

class PointsRedemptionActivity : BaseActivity<ActivityPointsRedemptionBinding, PointsRedemptionViewModel>() {

    override val viewModel: PointsRedemptionViewModel by lazy { ViewModelProvider(this).get(PointsRedemptionViewModel::class.java) }

    private var selectedAmount: Int = 0

    override fun getViewBinding(): ActivityPointsRedemptionBinding {
        return ActivityPointsRedemptionBinding.inflate(layoutInflater)
    }

    override fun initView() {
        setupToolbar()
        setupListeners()
        // 加载积分数据
        viewModel.loadPoints()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "积分兑换现金"
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupListeners() {
        binding.btn10Yuan.setOnClickListener { selectAmount(10) }
        binding.btn20Yuan.setOnClickListener { selectAmount(20) }
        binding.btn30Yuan.setOnClickListener { selectAmount(30) }

        binding.btnRedeemNow.setOnClickListener { redeemPoints() }
        binding.btnBindWechat.setOnClickListener { bindWechatAccount() }
    }

    private fun selectAmount(amount: Int) {
        selectedAmount = amount
        // TODO: 更新UI以显示选中状态，可以通过修改按钮背景或者文本颜色来实现
        Toast.makeText(this, "已选择兑换 ${amount}元", Toast.LENGTH_SHORT).show()
    }

    private fun redeemPoints() {
        if (selectedAmount == 0) {
            Toast.makeText(this, "请选择兑换金额", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.redeemPoints(selectedAmount)
    }

    private fun bindWechatAccount() {
        Toast.makeText(this, "绑定微信账户功能待实现", Toast.LENGTH_SHORT).show()
    }

    override fun observeViewModel() {
        viewModel.currentPoints.observe(this) { points ->
            binding.tvCurrentPoints.text = "${points} 积分"
        }

        viewModel.redeemResult.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    // 显示加载中状态，例如显示一个进度条
                    Toast.makeText(this, "正在提现...", Toast.LENGTH_SHORT).show()
                }
                is UiState.Success -> {
                    Toast.makeText(this, state.data, Toast.LENGTH_SHORT).show()
                }
                is UiState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.Empty -> {
                    // 提现操作通常不会返回空状态，但如果需要，可以处理
                    Toast.makeText(this, "没有可提现的数据", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun showEmptyView() {
        // TODO: 根据需要实现空页面显示逻辑
    }
} 