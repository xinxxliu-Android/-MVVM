package com.example.mvvmdemo

import android.content.Intent
import android.text.InputType
import android.widget.Toast
import androidx.activity.viewModels
import com.example.mvvmdemo.base.BaseActivity
import com.example.mvvmdemo.databinding.ActivityLoginBinding
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.vm.LoginViewModel

/**
 * 登录界面
 */
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    override val viewModel: LoginViewModel by viewModels()

    override fun getViewBinding(): ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)

    override fun initView() {
        // 设置生命周期所有者
        binding.lifecycleOwner = this
        
        // 绑定 ViewModel
        binding.viewModel = viewModel

        // 默认显示登录表单
        binding.viewFlipper.displayedChild = 0

        // 设置选项卡切换
        setupTabSelection()

        // 设置密码可见性切换
        setupPasswordToggles()

        // 设置登录按钮点击事件
        binding.btnLogin.setOnClickListener {
            viewModel.login()
        }

        // 设置注册按钮点击事件
        binding.btnRegister.setOnClickListener {
            viewModel.register()
        }
    }

    private fun setupTabSelection() {
        binding.tvLogin.setOnClickListener {
            updateTabUI(true)
            binding.viewFlipper.displayedChild = 0 // 显示登录表单
        }

        binding.tvRegister.setOnClickListener {
            updateTabUI(false)

            binding.viewFlipper.displayedChild = 1 // 显示注册表单
        }
    }

    private fun updateTabUI(isLoginSelected: Boolean) {
        binding.tvLogin.setTextColor(if (isLoginSelected) getColor(android.R.color.black) else getColor(android.R.color.darker_gray))
        binding.tvRegister.setTextColor(if (isLoginSelected) getColor(android.R.color.darker_gray) else getColor(android.R.color.black))
    }

    private fun setupPasswordToggles() {
        // 登录密码切换
        binding.ivPasswordToggle.setOnClickListener {
            togglePasswordVisibility(binding.etPassword, binding.ivPasswordToggle)
        }

        // 注册密码切换
        binding.ivRegPasswordToggle.setOnClickListener {
            togglePasswordVisibility(binding.etRegPassword, binding.ivRegPasswordToggle)
        }

        // 确认密码切换
        binding.ivConfirmPasswordToggle.setOnClickListener {
            togglePasswordVisibility(binding.etConfirmPassword, binding.ivConfirmPasswordToggle)
        }
    }

    private fun togglePasswordVisibility(editText: android.widget.EditText, imageView: android.widget.ImageView) {
        val isPasswordVisible = editText.inputType and InputType.TYPE_TEXT_VARIATION_PASSWORD == 0
        editText.inputType = if (isPasswordVisible) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        editText.setSelection(editText.text.length)
    }

    override fun observeViewModel() {
        // 观察登录状态
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    // 显示加载中
                }
                is UiState.Success -> {
                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = "登录"
                    // 用户信息和cookie已经在UserRepository中保存
                    navigateToMain()
                }
                is UiState.Error -> {
                    // 显示错误信息
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // 处理其他状态
                }
            }
        }

        // 观察注册状态
        viewModel.registerState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    // 显示加载中
                }
                is UiState.Success -> {
                    // 注册成功，切换到登录表单
                    Toast.makeText(this, "注册成功，请登录", Toast.LENGTH_SHORT).show()
                    // 切换到登录表单
                    viewModel.onLoginTabSelected()
                    // 清空注册表单
                    binding.etUsername.text.clear()
                    binding.etRegPassword.text.clear()
                    binding.etConfirmPassword.text.clear()
                }
                is UiState.Error -> {
                    // 显示错误信息
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // 处理其他状态
                }
            }
        }
    }

    override fun showEmptyView() {
        // 登录页面不需要空视图
    }

    /**
     * 跳转到主页
     */
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}