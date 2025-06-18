package com.example.mvvmdemo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ArticlePage(
    val curPage: Int,
    val datas: List<Article>,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

@Parcelize
data class Article(
    val id: Int,
    val title: String,
    val link: String,
    val author: String,
    val shareUser: String?,
    val niceDate: String,
    val chapterName: String,
) : Parcelable
