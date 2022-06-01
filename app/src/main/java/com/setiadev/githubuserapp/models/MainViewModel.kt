package com.setiadev.githubuserapp.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setiadev.githubuserapp.apis.ApiClient
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    val actionUser = MutableLiveData<ActionResult>()

    fun getUser() {
        viewModelScope.launch {
            flow {
                val response = ApiClient.apiService.getUser()
                emit(response)
            }.onStart {
                actionUser.value = ActionResult.Loading(true)
            }.onCompletion {
                actionUser.value = ActionResult.Loading(false)
            }.catch {
                actionUser.value = ActionResult.Error(it)
            }.collect {
                actionUser.value = ActionResult.Success(it)
            }
        }
    }

    fun getUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient.apiService.getUserSearch(mapOf(
                    "q" to username
                ))
                emit(response)
            }.onStart {
                actionUser.value = ActionResult.Loading(true)
            }.onCompletion {
                actionUser.value = ActionResult.Loading(false)
            }.catch {
                actionUser.value = ActionResult.Error(it)
            }.collect {
                actionUser.value = ActionResult.Success(it.items)
            }
        }
    }
}