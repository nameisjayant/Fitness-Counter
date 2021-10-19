package com.example.fitnesscounter.utils

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.lang.Exception
import javax.inject.Inject

class Preferences @Inject constructor(@ApplicationContext private val context: Context) {


    private val Context.datastore by preferencesDataStore("preferences")


    suspend fun setPref(key: Preferences.Key<String>, value: String) = context
        .datastore.edit { preference ->
            preference[key] = value
        }

    fun getPref(key: Preferences.Key<String>): Flow<String> = context
        .datastore.data
        .catch {
            if (this is Exception) {
                emit(emptyPreferences())
            }
        }.map { preference ->
            val data = preference[key] ?: ""
            data
        }

}