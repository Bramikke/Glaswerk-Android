package com.bramgoedvriend.glaswerk.network

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.Exception


object RetrofitClient {

    //private const val BASE_URL = "http://ec2-35-180-181-246.eu-west-3.compute.amazonaws.com:8080/"
    private const val BASE_URL = "http://192.168.0.122:8080/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val instance : GlaswerkAPIService by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build().create(GlaswerkAPIService::class.java)
    }
}