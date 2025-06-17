package com.example.mvvmdemo.base

/**
 * 返回数据结构
 * {
 *     "data": ...,
 *     "errorCode": 0,
 *     "errorMsg": ""
 * }
 */
data class BaseResponse<T>(
    val data: T?,
    val errorCode: Int,
    val errorMsg: String?
)