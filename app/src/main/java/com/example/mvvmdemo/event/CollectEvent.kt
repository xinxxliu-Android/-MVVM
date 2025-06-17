package com.example.mvvmdemo.event

/**
 * 收藏状态变化事件
 * @param articleId 文章ID
 * @param isCollected 是否已收藏
 */
data class CollectEvent(
    val articleId: Int,
    val isCollected: Boolean
) 