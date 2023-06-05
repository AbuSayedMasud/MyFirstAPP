package com.example.loginapp.Api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val Base_Url = "https://dummyjson.com"
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Base_Url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

}