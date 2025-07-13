package com.example.api

import com.example.model.LoginRequest
import com.example.model.LoginResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")  // Change if your backend login path is different
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResult>
}
