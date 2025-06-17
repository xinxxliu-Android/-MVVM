package com.example.mvvmdemo.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmdemo.base.BaseViewModel
import com.example.mvvmdemo.data.User
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {
    private val userRepository = UserRepository()

    // 表单切换状态
    private val _isLoginSelected = MutableLiveData(true)
    val isLoginSelected: LiveData<Boolean> = _isLoginSelected

    // 登录表单数据
    val phone = MutableLiveData("")
    val password = MutableLiveData("")
    private val _isPasswordVisible = MutableLiveData(false)
    val isPasswordVisible: LiveData<Boolean> = _isPasswordVisible

    // 注册表单数据
    val username = MutableLiveData("")
    val regPassword = MutableLiveData("")
    private val _isRegPasswordVisible = MutableLiveData(false)
    val isRegPasswordVisible: LiveData<Boolean> = _isRegPasswordVisible
    val confirmPassword = MutableLiveData("")
    private val _isConfirmPasswordVisible = MutableLiveData(false)
    val isConfirmPasswordVisible: LiveData<Boolean> = _isConfirmPasswordVisible

    // 登录状态
    private val _loginState = MutableLiveData<UiState<User>>()
    val loginState: LiveData<UiState<User>> = _loginState

    // 注册状态
    private val _registerState = MutableLiveData<UiState<User>>()
    val registerState: LiveData<UiState<User>> = _registerState

    // 加载状态
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        // 观察登录状态变化
        loginState.observeForever { state ->
            _isLoading.value = state is UiState.Loading
        }

        // 观察注册状态变化
        registerState.observeForever { state ->
            _isLoading.value = state is UiState.Loading
        }
    }

    override fun onCleared() {
        super.onCleared()
        // 移除观察者
        loginState.removeObserver { }
        registerState.removeObserver { }
    }

    // 切换登录/注册表单
    fun onLoginTabSelected() {
        _isLoginSelected.value = true
    }

    fun onRegisterTabSelected() {
        _isLoginSelected.value = false
    }

    // 切换密码可见性
    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !(_isPasswordVisible.value ?: false)
    }

    fun toggleRegPasswordVisibility() {
        _isRegPasswordVisible.value = !(_isRegPasswordVisible.value ?: false)
    }

    fun toggleConfirmPasswordVisibility() {
        _isConfirmPasswordVisible.value = !(_isConfirmPasswordVisible.value ?: false)
    }

    // 登录
    fun login() {
        val phoneValue = phone.value ?: ""
        val passwordValue = password.value ?: ""
        
        if (phoneValue.isEmpty()) {
            _loginState.value = UiState.Error("请输入手机号")
            return
        }

        if (passwordValue.isEmpty()) {
            _loginState.value = UiState.Error("请输入密码")
            return
        }

        // 使用 launchRequest 方法处理网络请求
        launchRequest(
            block = { userRepository.login(phoneValue, passwordValue) },
            liveData = _loginState
        )
    }

    // 注册
    fun register() {
        val usernameValue = username.value ?: ""
        val passwordValue = regPassword.value ?: ""
        val confirmPasswordValue = confirmPassword.value ?: ""
        
        if (usernameValue.isEmpty()) {
            _registerState.value = UiState.Error("请输入用户名")
            return
        }

        if (passwordValue.isEmpty()) {
            _registerState.value = UiState.Error("请输入密码")
            return
        }

        if (confirmPasswordValue.isEmpty()) {
            _registerState.value = UiState.Error("请确认密码")
            return
        }

        if (passwordValue != confirmPasswordValue) {
            _registerState.value = UiState.Error("两次输入的密码不一致")
            return
        }

        // 使用 launchRequest 方法处理网络请求
        launchRequest(
            block = { userRepository.register(usernameValue, passwordValue, confirmPasswordValue) },
            liveData = _registerState
        )
    }
}