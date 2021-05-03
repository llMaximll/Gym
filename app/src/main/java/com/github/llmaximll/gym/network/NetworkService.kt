package com.github.llmaximll.gym.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://gym.areas.su/"

class NetworkService private constructor() {
    private var mRetrofit: Retrofit? = null
    init {
        mRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun getJSONApi(): ServerApi {
        return mRetrofit!!.create(ServerApi::class.java)
    }

    companion object {
        private var mInstance: NetworkService? = null
        val instance: NetworkService?
            get() {
                if (mInstance == null) {
                    mInstance = NetworkService()
                }
                return mInstance
            }
    }
}