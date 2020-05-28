package com.example.lastprojectmade.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private var retrofit: Retrofit
    fun getService(): EndPoint = retrofit.create(EndPoint::class.java)

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(GITHUB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {
        const val GITHUB_BASE_URL = "https://api.github.com/"
    }
}