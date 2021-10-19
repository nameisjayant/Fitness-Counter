package com.example.fitnesscounter.utils

sealed class ApiStates<out T> {

    data class Success<out R>(val data: R) : ApiStates<R>()
    data class Failure(val msg: String) : ApiStates<Nothing>()
    object Loading : ApiStates<Nothing>()

}
