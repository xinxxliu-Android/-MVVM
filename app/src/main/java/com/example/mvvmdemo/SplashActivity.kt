package com.example.mvvmdemo


import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.czhj.sdk.common.models.App
import com.example.mvvmdemo.base.BaseActivity
import com.example.mvvmdemo.databinding.ActivitySplashBinding
import com.example.mvvmdemo.databinding.DialogPrivacyPolicyBinding
import com.example.mvvmdemo.vm.SplashViewModel


class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {
    override val viewModel: SplashViewModel by viewModels()
    override fun getViewBinding() = ActivitySplashBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun initView() {
        // 判断是否已同意隐私协议
        if (hasAgreedPrivacy()) {
            goMain()
        } else {
            showPrivacyDialog()
        }

    }
    override fun observeViewModel() {
        // 如有需要可监听viewModel的LiveData
    }

    override fun showEmptyView() {
    }

    private fun showPrivacyDialog() {
        val dialogBinding = DialogPrivacyPolicyBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialogBinding.btnAgree.setOnClickListener {
            setAgreedPrivacy()
            dialog.dismiss()
            // 同意后初始化SDK
            (application as MyApp).initSigMobSdk()
            // 跳转广告页
            startActivity(Intent(this, SplashAdActivity::class.java))
            finish()
        }
        dialogBinding.btnDisagree.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }
    private fun goMain() {
        startActivity(Intent(this, SplashAdActivity::class.java))
        finish()
    }

    private fun hasAgreedPrivacy(): Boolean {
        return getSharedPreferences("app", MODE_PRIVATE).getBoolean("privacy_agreed", false)
    }

    private fun setAgreedPrivacy() {
        getSharedPreferences("app", MODE_PRIVATE).edit().putBoolean("privacy_agreed", true).apply()
    }
}