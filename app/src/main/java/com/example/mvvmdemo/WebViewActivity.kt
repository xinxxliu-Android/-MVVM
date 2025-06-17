package com.example.mvvmdemo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mvvmdemo.base.BaseActivity
import com.example.mvvmdemo.databinding.ActivityWebViewBinding
import com.example.mvvmdemo.vm.WebViewModel

class WebViewActivity : BaseActivity<ActivityWebViewBinding,WebViewModel>() {
    override val viewModel: WebViewModel by viewModels()

    override fun getViewBinding(): ActivityWebViewBinding
    = ActivityWebViewBinding.inflate(layoutInflater)

    override fun initView() {

    }

    override fun observeViewModel() {
        TODO("Not yet implemented")
    }

    override fun showEmptyView() {
        TODO("Not yet implemented")
    }

}