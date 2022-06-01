package com.setiadev.githubuserapp.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavUserDao {
    @Insert
    suspend fun addToFav(favUser: FavUser)

    @Query("SELECT * FROM fav_user_data")
    fun getFavUser(): LiveData<List<FavUser>>

    @Query("SELECT count(*) FROM fav_user_data WHERE fav_user_data.id = :id")
    suspend fun userChecking(id: Int): Int

    @Query("DELETE FROM fav_user_data WHERE fav_user_data.id = :id")
    suspend fun removeFromFav(id: Int): Int
}