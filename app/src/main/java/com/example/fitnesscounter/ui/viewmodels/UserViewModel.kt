package com.example.fitnesscounter.ui.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesscounter.data.repos.UserRepository
import com.example.fitnesscounter.utils.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel
@Inject constructor(
    private val preferences: Preferences,
    private val userRepository: UserRepository
) : ViewModel() {

    fun setPref(name: androidx.datastore.preferences.core.Preferences.Key<String>, value: String) =
        viewModelScope.launch {
            preferences.setPref(name, value)
        }

    fun getPref(key: androidx.datastore.preferences.core.Preferences.Key<String>) =
        preferences.getPref(key)

    fun createUser(username: String, email: String, password: String) =
        userRepository.createUser(username, email, password)


    fun login(email: String, password: String) = userRepository.login(email, password)

    fun getContacts(activity:Activity) = userRepository.getContact(activity)

    fun getCurrentSms(activity: Activity) = userRepository.getCurrentSMS(activity)

}