package com.example.fitnesscounter.utils

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {

    val username: Preferences.Key<String> =
        stringPreferencesKey("username")
    val token: Preferences.Key<String> =
        stringPreferencesKey("token")
    val email: Preferences.Key<String> =
        stringPreferencesKey("email")
    val userId: Preferences.Key<String> = stringPreferencesKey("userId")
    var passToken = ""

}