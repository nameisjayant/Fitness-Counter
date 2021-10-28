package com.example.fitnesscounter.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.fitnesscounter.data.repos.ProfileRepository
import com.example.fitnesscounter.utils.toResultFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

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
    ) =
        profileRepository.createProfile(
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