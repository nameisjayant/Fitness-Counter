package com.example.fitnesscounter.data.network

import com.example.fitnesscounter.data.UserResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    companion object {
        const val BASE_URL = "https://fitnesscounter.herokuapp.com/v1/"
    }

    @POST("create")
    @FormUrlEncoded
    suspend fun createUser(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<UserResponse>

    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<UserResponse>

    @POST("profile")
    @FormUrlEncoded
    suspend fun createProfile(
        @Field("id") id: String,
        @Field("goal") goal: String,
        @Field("active") active: String,
        @Field("gender") gender: String,
        @Field("dob") dob: String,
        @Field("height") height: String,
        @Field("weight") weight: String,
        @Field("goalweight") goalWeight: String,
        @Field("weeklygoal") weeklyGoal: String
    ): Response<UserResponse>
}