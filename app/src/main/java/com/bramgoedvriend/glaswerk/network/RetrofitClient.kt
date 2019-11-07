package com.bramgoedvriend.glaswerk.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://ec2-35-180-181-246.eu-west-3.compute.amazonaws.com:8080/"
    //private const val BASE_URL = "http://192.168.0.124:8080/"

    val instance: GlaswerkAPIService by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(GlaswerkAPIService::class.java)
    }
}