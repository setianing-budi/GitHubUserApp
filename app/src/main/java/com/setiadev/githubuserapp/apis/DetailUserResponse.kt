package com.setiadev.githubuserapp.apis

data class DetailUserResponse(
    val avatar_url: String,
    val company: Any,
    val followers: Int,
    val following: Int,
    val id: Int,
    val location: Any,
    val login: String,
    val name: Any,
    val public_repos: Int,
)