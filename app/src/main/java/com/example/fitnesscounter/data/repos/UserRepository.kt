package com.example.fitnesscounter.data.repos

import com.example.fitnesscounter.data.network.ApiService
import com.example.fitnesscounter.utils.toResultFlow
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiService: ApiService) {

    fun createUser(username: String, email: String, password: String) = toResultFlow {
        apiService.createUser(username, email, password)
    }

    fun login(username: String, password: String) = toResultFlow {
        apiService.login(username, password)
    }
}