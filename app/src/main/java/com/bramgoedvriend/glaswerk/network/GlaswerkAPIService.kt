package com.bramgoedvriend.glaswerk.network

import com.bramgoedvriend.glaswerk.domain.Item
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface GlaswerkAPIService {
    @GET("item")
    fun getItems(): Observable<List<Item>>

    @GET("item")
    fun getItems(@Query("id") id: String): Observable<List<Item>>

}