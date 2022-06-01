package com.setiadev.githubuserapp.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.setiadev.githubuserapp.apis.ApiClient
import com.setiadev.githubuserapp.local.FavUser
import com.setiadev.githubuserapp.local.FavUserDao
import com.setiadev.githubuserapp.local.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel(application: Application): AndroidViewModel(application) {

    val actionDetail = MutableLiveData<ActionResult>()
    val actionFollowers = MutableLiveData<ActionResult>()
    val actionFollowing = MutableLiveData<ActionResult>()

    private var userDao: FavUserDao?
    private var userDatabase: UserDatabase?

    init {
        userDatabase = UserDatabase.getUserDatabase(application)
        userDao = userDatabase?.favUserDao()
    }

    fun getDetailUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient.apiService.getDetailUser(username)
                emit(response)
            }.onStart {
                actionDetail.value = ActionResult.Loading(true)
            }.onCompletion {
                actionDetail.value = ActionResult.Loading(false)
            }.catch {
                actionDetail.value = ActionResult.Error(it)
            }.collect {
                actionDetail.value = ActionResult.Success(it)
            }
        }
    }

    fun getUserFollowers(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient.apiService.getUserFollowers(username)
                emit(response)
            }.onStart {
                actionFollowers.value = ActionResult.Loading(true)
            }.onCompletion {
                actionFollowers.value = ActionResult.Loading(false)
            }.catch {
                actionFollowers.value = ActionResult.Error(it)
            }.collect {
                actionFollowers.value = ActionResult.Success(it)
            }
        }
    }

    fun getUserFollowing(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient.apiService.getUserFollowing(username)
                emit(response)
            }.onStart {
                actionFollowing.value = ActionResult.Loading(true)
            }.onCompletion {
                actionFollowing.value = ActionResult.Loading(false)
            }.catch {
                actionFollowing.value = ActionResult.Error(it)
            }.collect {
                actionFollowing.value = ActionResult.Success(it)
            }
        }
    }

    fun addToFav(
        username: String,
        id: Int,
        avatar: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavUser(
                username, id, avatar
            )
            userDao?.addToFav(user)
        }
    }

    suspend fun userChecking(id: Int) = userDao?.userChecking(id)

    fun removeFromFav(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFav(id)
        }
    }
}