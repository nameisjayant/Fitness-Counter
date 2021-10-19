package com.example.fitnesscounter.data.network

import com.example.fitnesscounter.di.AccessToken
import com.example.fitnesscounter.di.TokenType
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class OAuthInterceptor @Inject constructor(
    @TokenType
    val tokenType: String,
    @AccessToken
    val accessToken: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", "$tokenType $accessToken")
            .build()
        return chain.proceed(request)
    }

}