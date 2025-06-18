package com.example.mvvmdemo.net

import com.example.mvvmdemo.base.BaseResponse
import com.example.mvvmdemo.data.ArticlePage
import com.example.mvvmdemo.data.Banner
import com.example.mvvmdemo.data.OfficialAccount
import com.example.mvvmdemo.data.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    /**
     * 首页文章列表
     */
    @GET("article/list/{page}/json")
    suspend fun getHomeArticles(@Path("page") page: Int): BaseResponse<ArticlePage>

    /**
     * 首页Banner
     */
    @GET("banner/json")
    suspend fun getBanner(): BaseResponse<List<Banner>>

    /**
     * 用户登录
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): BaseResponse<User>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repasswoed: String
    ): BaseResponse<User>

    /**
     * 文章收藏
     */
    @POST("lg/collect/{id}/json")
    suspend fun collectArticle(@Path("id") articleId: Int): BaseResponse<Any>

    /**
     * 取消文章收藏
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun uncollectArticle(@Path("id") articleId: Int): BaseResponse<Any>

    /**
     * 获取用户收藏的文章列表
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectedArticles(@Path("page") page: Int): BaseResponse<ArticlePage>
    
    /**
     * 获取公众号列表
     */
    @GET("wxarticle/chapters/json")
    suspend fun getWxArticle(): BaseResponse<List<OfficialAccount>>

    /**
     * 获取公众号文章列表
     */
    @GET("wxarticle/list/{officialAccountId}/{page}/json")
    suspend fun getOfficialArticles(
        @Path("officialAccountId") officialAccountId: Int,
        @Path("page") page: Int
    ): BaseResponse<ArticlePage>

}