package com.example.mvvmdemo

import androidx.activity.viewModels
import com.example.mvvmdemo.base.BaseActivity
import com.example.mvvmdemo.databinding.ActivityResigerBinding
import com.example.mvvmdemo.vm.RegisterViewModel

class RegisterActivity :BaseActivity<ActivityResigerBinding,RegisterViewModel>(){
    override val viewModel: RegisterViewModel by viewModels()

    override fun getViewBinding() =ActivityResigerBinding.inflate(layoutInflater)

    override fun initView() {
    }

    override fun observeViewModel() {
    }

    override fun showEmptyView() {
    }

}