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
    fun getItemsAsync(): Deferred<NetworkItemsContainer>

    @GET("student")
    fun getStudentsAsync(): Deferred<NetworkStudentContainer>

    @GET("lokaal")
    fun getRoomAsync(): Deferred<NetworkRoomContainer>

    @GET("klas")
    fun getClassAsync(): Deferred<NetworkClassContainer>

    @GET("studentItem")
    fun getStudentItemAsync(): Deferred<NetworkStudentItemContainer>

    @GET("studentByClassByItem")
    fun getStudentsByClassByItemAsync(@Query("klasid") classid: Int,
                                 @Query("itemid") itemid: Int): Deferred<List<Student>>

    @POST("studentItemBroken")
    fun postStudentItemBrokenAsync(@Body body : StudentItem): Deferred<Any>

    @POST("reduceItem")
    fun postReduceItemAsync(@Body body: ItemId): Deferred<Any>

    @POST("orderItem")
    fun postOrderItemAsync(@Body body: OrderItem): Deferred<Any>

    @POST("addItem")
    fun postAddItem(@Body body: com.bramgoedvriend.glaswerk.network.Item): Deferred<Any>

    @POST("editItem")
    fun postEditItem(@Body body: com.bramgoedvriend.glaswerk.network.Item): Deferred<Any>

    @POST("deleteItem")
    fun postRemoveItem(@Body body: ItemId): Deferred<Any>

}