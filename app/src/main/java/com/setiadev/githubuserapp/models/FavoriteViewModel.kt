package com.setiadev.githubuserapp.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.setiadev.githubuserapp.local.FavUser
import com.setiadev.githubuserapp.local.FavUserDao
import com.setiadev.githubuserapp.local.UserDatabase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private var userDao: FavUserDao?
    private var userDatabase: UserDatabase?

    init {
        userDatabase = UserDatabase.getUserDatabase(application)
        userDao = userDatabase?.favUserDao()
    }

    fun getFavUser(): LiveData<List<FavUser>>? {
        return userDao?.getFavUser()
    }
}