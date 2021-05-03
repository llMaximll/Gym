package com.github.llmaximll.gym.network

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ServerApi {
    @POST("signin")
    fun signIn(@Query("username") username: String,
               @Query("password") password: String
    ): Call<Map<String, Map<String, Int>>>

    @POST("signup")
    fun signUp(@Query("username") username: String,
               @Query("email") email: String,
               @Query("password") password: String,
               @Query("height") height: String,
               @Query("weight") weight: String
    ): Call<Map<String, Map<String, String>>>
}