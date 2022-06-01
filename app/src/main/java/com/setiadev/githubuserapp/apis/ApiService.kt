package com.setiadev.githubuserapp.apis

import com.setiadev.githubuserapp.BuildConfig
import retrofit2.http.*

interface ApiService {

    @JvmSuppressWildcards
    @GET("users")
    suspend fun getUser(@Header("Authorization") authorization: String = BuildConfig.TOKEN): MutableList<UserResponse.Item>

    @JvmSuppressWildcards
    @GET("users/{username}")
    suspend fun getDetailUser(@Path("username") username: String,
                              @Header("Authorization") authorization: String = BuildConfig.TOKEN): DetailUserResponse

    @JvmSuppressWildcards
    @GET("users/{username}/followers")
    suspend fun getUserFollowers(@Path("username") username: String,
                                 @Header("Authorization") authorization: String = BuildConfig.TOKEN): MutableList<UserResponse.Item>

    @JvmSuppressWildcards
    @GET("users/{username}/following")
    suspend fun getUserFollowing(@Path("username") username: String,
                                 @Header("Authorization") authorization: String = BuildConfig.TOKEN): MutableList<UserResponse.Item>

    @JvmSuppressWildcards
    @GET("search/users")
    suspend fun getUserSearch(@QueryMap params: Map<String, Any>,
                              @Header("Authorization") authorization: String = BuildConfig.TOKEN) : UserResponse
}