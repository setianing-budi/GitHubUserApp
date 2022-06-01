package com.setiadev.githubuserapp.apis

data class UserResponse(
    val incomplete_results: Boolean,
    val items: MutableList<Item>,
    val total_count: Int
) {
    data class Item(
        val avatar_url: String,
        val id: Int,
        val login: String,
    )
}