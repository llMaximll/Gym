package com.github.llmaximll.gym.retrofit

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ServerApi {
    @POST("signin")
    fun signIn(@Query("username") username: String,
               @Query("password") password: String): Call<Map<String, Map<String, Int>>>
}