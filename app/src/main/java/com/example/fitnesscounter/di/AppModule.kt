package com.example.fitnesscounter.di

import androidx.lifecycle.asLiveData
import com.example.fitnesscounter.data.network.ApiService
import com.example.fitnesscounter.data.network.OAuthInterceptor
import com.example.fitnesscounter.ui.LoginActivity
import com.example.fitnesscounter.ui.fragments.LoginFragment
import com.example.fitnesscounter.ui.fragments.ProfileFragment
import com.example.fitnesscounter.utils.Constants
import com.example.fitnesscounter.utils.Preferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    var data = ""

    @Provides
    @Singleton
    suspend fun getToken(preferences: Preferences) = preferences.getPref(Constants.token).collect {
        data = it
    }

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi
        .Builder()
        .run {
            add(KotlinJsonAdapterFactory())
            build()
        }

    @Provides
    @Singleton
    fun providesApiService(moshi: Moshi, okHttpClient: OkHttpClient): ApiService =
        Retrofit
            .Builder()
            .run {
                baseUrl(ApiService.BASE_URL)
                addConverterFactory(MoshiConverterFactory.create(moshi))
                client(okHttpClient)
                build()
            }.create(ApiService::class.java)

    @Provides
    @Singleton
    fun providesOKHttpClient(oAuthInterceptor: OAuthInterceptor): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(oAuthInterceptor)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    @TokenType
    fun providesTokenType(): String = "Bearer"

    @Provides
    @Singleton
    @AccessToken
    fun providesAccessToken(preferences: Preferences): String = runBlocking { preferences.getPref(Constants.token).first() }


//    @DelicateCoroutinesApi
//    @Provides
//    @Singleton
//    @AccessToken
//    fun getToken1(preferences: Preferences):String {
//        var token = ""
//        GlobalScope.launch {
//            token = preferences.getPref(Constants.token).first()
//        }
//        return token
//    }

}

// jks/feature/di
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenType

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AccessToken