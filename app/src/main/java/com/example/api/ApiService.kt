// ApiService.kt
package com.example.api

import com.example.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.Body

interface ApiService {
    @GET("users")
    fun loginUser(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<List<User>>

    @POST("users")
    fun registerUser(@Body user: User): Call<User>
}

