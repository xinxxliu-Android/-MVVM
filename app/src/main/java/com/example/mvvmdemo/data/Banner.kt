package com.example.mvvmdemo.data

/**
 * 轮播图数据
 */
data class Banner(
    /**
     * 描述
     */
    var desc:String,
    /**
     * id
     */
    var id: Int,
    /**
     * 图片路径
     */
    var imagePath:String,
    var isVisible:Int,
    var order :Int,
    var title :String,
    var type :Int,
    var url :String
)
