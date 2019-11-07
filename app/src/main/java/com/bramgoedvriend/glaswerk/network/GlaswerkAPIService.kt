package com.bramgoedvriend.glaswerk.network

import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.domain.Student
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface GlaswerkAPIService {
    @GET("item")
    fun getItems(): Observable<List<Item>>

    @GET("item")
    fun getItems(@Query("id") id: String): Observable<List<Item>>

    @GET("lokaal")
    fun getRoom(): Observable<List<Lokaal>>

    @GET("klas")
    fun getClass(): Observable<List<Klas>>

    @GET("itemOrders")
    fun getItemOrders(): Observable<List<Item>>

    @GET("studentByClass")
    fun getStudentsByClass(@Query("id") id: Int): Observable<List<Student>>


    @GET("studentByClassByItem")
    fun getStudentsByClassByItem(@Query("klasid") classid: Int,
                                 @Query("itemid") itemid: Int): Observable<List<Student>>

    @POST("studentItemBroken")
    fun postStudentItemBroken(@Body body : HashMap<String, Any>): Observable<Any>

    @POST("reduceItem")
    fun postReduceItem(@Body body: HashMap<String, Int>): Observable<Any>

}