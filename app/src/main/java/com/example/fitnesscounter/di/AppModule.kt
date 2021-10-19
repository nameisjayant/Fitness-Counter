package com.example.fitnesscounter.di

import com.example.fitnesscounter.data.network.ApiService
import com.example.fitnesscounter.data.network.OAuthInterceptor
import com.example.fitnesscounter.utils.Constants
import com.example.fitnesscounter.utils.Preferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun providesTokenType() = "Bearer"

    @Provides
    @Singleton
    @AccessToken
    fun providesAccessToken(preferences: Preferences) = "ssdssd"
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenType

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AccessToken