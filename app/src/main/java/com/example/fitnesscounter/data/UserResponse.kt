package com.example.fitnesscounter.data

data class UserResponse(
    val status:Boolean,
    val message:String,
    val data:Data
)
data class Data(
    val id:Int,
    val username:String,
    val email:String,
    val token:String
)
