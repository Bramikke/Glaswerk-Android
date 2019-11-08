package com.bramgoedvriend.glaswerk.network

import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.domain.Student
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import retrofit2.http.*

interface GlaswerkAPIService {
    @GET("item")
    fun getItemsAsync(): Deferred<List<Item>>

    @GET("item")
    fun getItemsAsync(@Query("id") id: String): Deferred<List<Item>>

    @GET("lokaal")
    fun getRoomAsync(): Deferred<List<Lokaal>>

    @GET("klas")
    fun getClassAsync(): Deferred<List<Klas>>

    @GET("itemOrders")
    fun getItemOrdersAsync(): Deferred<List<Item>>

    @GET("studentByClass")
    fun getStudentsByClassAsync(@Query("id") id: Int): Deferred<List<Student>>


    @GET("studentByClassByItem")
    fun getStudentsByClassByItemAsync(@Query("klasid") classid: Int,
                                 @Query("itemid") itemid: Int): Deferred<List<Student>>

    @POST("studentItemBroken")
    fun postStudentItemBrokenAsync(@Body body : StudentItem): Deferred<Any>

    @POST("reduceItem")
    fun postReduceItemAsync(@Body body: ReduceItem): Deferred<Any>

}