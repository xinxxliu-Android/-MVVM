package com.example.mvvmdemo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * 公众号TAB信息数据类
 */
@Parcelize
data class OfficialAccount(
    val articleList: List<Article>,
    val author: String,
    val children: List<@RawValue Any>, // 根据数据结构，children为空数组
    val courseId: Int,
    val cover: String,
    val desc: String,
    val id: Int,
    val lisense: String,
    val lisenseLink: String,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val type: Int,
    val userControlSetTop: Boolean,
    val visible: Int
) : Parcelable