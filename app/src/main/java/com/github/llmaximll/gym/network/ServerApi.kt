package com.github.llmaximll.gym.network

import com.github.llmaximll.gym.dataclasses.Lessons
import com.github.llmaximll.gym.dataclasses.Profile
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ServerApi {
    @POST("signin")
    fun signIn(@Query("username") username: String,
               @Query("password") password: String):
            Call<Map<String, Map<String, Int>>>

    @POST("signup")
    fun signUp(@Query("username") username: String,
               @Query("email") email: String,
               @Query("password") password: String,
               @Query("height") height: String,
               @Query("weight") weight: String):
            Call<Map<String, Map<String, String>>>

    @GET("lessons")
    fun getLessons(): Call<List<Lessons>>

    @POST("profile")
    fun getProfile(@Query("token") token: String):
            Call<List<Profile>>

    @POST("signout")
    fun signOut(@Query("username") username: String):
            Call<Map<String, Map<String, String>>>

    @PUT("editeprofile")
    fun editProfile(@Query("token") token: String,
                    @Query("weight") weight: String,
                    @Query("height") height: String):
            Call<Map<String, Map<String, String>>>

    @GET
    @Streaming
    fun downloadImage(@Url url: String):
            Call<ResponseBody>
}