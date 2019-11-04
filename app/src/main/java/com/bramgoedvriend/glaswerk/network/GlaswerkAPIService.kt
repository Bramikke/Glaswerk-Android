package com.bramgoedvriend.glaswerk.network

import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.domain.Student
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface GlaswerkAPIService {
    @GET("item")
    fun getItems(): Observable<List<Item>>

    @GET("item")
    fun getItems(@Query("id") id: String): Observable<List<Item>>

    @GET("itemOrders")
    fun getItemOrders(): Observable<List<Item>>

    @GET("studentByClass")
    fun getStudentsByClass(@Query("id") id: Int): Observable<List<Student>>

}