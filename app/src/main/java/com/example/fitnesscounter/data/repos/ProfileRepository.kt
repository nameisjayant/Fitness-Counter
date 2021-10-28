package com.example.fitnesscounter.data.repos

import com.example.fitnesscounter.data.network.ApiService
import com.example.fitnesscounter.utils.toResultFlow
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val apiService: ApiService) {

    fun createProfile(
        id: String,
        goal: String,
        active: String,
        gender: String,
        dob: String,
        height: String,
        weight: String,
        goalWeight: String,
        weeklyGoal: String
    ) = toResultFlow {
        apiService.createProfile(
            id,
            goal,
            active,
            gender,
            dob,
            height,
            weight,
            goalWeight,
            weeklyGoal
        )
    }

}