package com.setiadev.githubuserapp.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "fav_user_data")
data class FavUser(
    val login: String,
    @PrimaryKey
    val id: Int,
    val avatar: String
): Serializable
