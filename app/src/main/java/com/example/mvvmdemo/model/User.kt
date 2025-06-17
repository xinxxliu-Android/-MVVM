package com.example.mvvmdemo.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField

data class User(
    var username: String = "",
    var age: String = "",
    var email: String = "",
    var isActive: Boolean = false
) 