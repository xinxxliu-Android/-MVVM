package com.example.mvvmdemo.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmdemo.base.BaseViewModel
import com.example.mvvmdemo.data.Repo
import com.example.mvvmdemo.data.User
import com.example.mvvmdemo.net.UiState
import com.example.mvvmdemo.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel : BaseViewModel() {
//    private val repository = UserRepository()
//
//    private val _userState = MutableLiveData<UiState<User>>()
//    private val _repoState = MutableLiveData<UiState<List<Repo>>>()
//
//    val userState: LiveData<UiState<User>> = _userState
//    val repoState: LiveData<UiState<List<Repo>>> = _repoState
//
//    fun fetchUser(user: String) {
//        Log.e("lx", "fetchUser被调用")
//        _userState.value = UiState.Loading
//        viewModelScope.launch {
//            val result = repository.getUser(user)
//            _userState.value = result
//        }
//    }
//
//    fun fetchUserRepos(user: String) {
//
//        launchRequest(
//            block = { repository.getUserRepos(user) },
//            liveData = _repoState,
//            isEmpty = { it == null || it.isEmpty() }
//
//        )
//    }

}