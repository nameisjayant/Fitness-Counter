package com.example.fitnesscounter.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

fun <T> toResultFlow(call: suspend () -> Response<T>): Flow<ApiStates<T>> = flow {

    emit(ApiStates.Loading)
    val response = call()

    try {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                emit(ApiStates.Success(data))
            }
        } else {
            response.errorBody()?.let { error ->
                error.close()

                emit(ApiStates.Failure(error.string()))

            }
        }
    } catch (e: Exception) {
        emit(ApiStates.Failure(e.message.toString()))
    }

}.flowOn(Dispatchers.IO)