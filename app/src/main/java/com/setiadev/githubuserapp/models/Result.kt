package com.setiadev.githubuserapp.models

sealed class ActionResult {
    data class Success<out T>(val data: T): ActionResult()
    data class Error(val exception: Throwable): ActionResult()
    data class  Loading(val isLoading: Boolean): ActionResult()
}
