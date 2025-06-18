package com.example.mvvmdemo.vm

import androidx.lifecycle.viewModelScope
import com.example.mvvmdemo.base.BaseViewModel
import com.example.mvvmdemo.data.OfficialAccount
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OfficialFragmentViewModel : BaseViewModel() {
    
    private val repository = HomeRepository()
    
    private val _officialAccounts = MutableStateFlow<UiState<List<OfficialAccount>>>(UiState.Loading)
    val officialAccounts: StateFlow<UiState<List<OfficialAccount>>> = _officialAccounts
    
    /**
     * 获取公众号列表
     */
    fun getOfficialAccounts() {
        viewModelScope.launch {
            _officialAccounts.value = UiState.Loading
            _officialAccounts.value = repository.getOfficialAccounts()
        }
    }
    
    /**
     * 刷新公众号列表
     */
    fun refreshOfficialAccounts() {
        getOfficialAccounts()
    }
}