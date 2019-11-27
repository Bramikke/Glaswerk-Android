package com.bramgoedvriend.glaswerk.network

import kotlinx.coroutines.Deferred
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

    @POST("addLeerling")
    fun postAddStudent(@Body body: com.bramgoedvriend.glaswerk.network.Student): Deferred<Any>

    @POST("editLeerling")
    fun postEditStudent(@Body body: com.bramgoedvriend.glaswerk.network.Student): Deferred<Any>

    @POST("deleteLeerling")
    fun postRemoveStudent(@Body body: StudentId): Deferred<Any>

    @POST("addLokaal")
    fun postAddRoom(@Body body: RoomClassName): Deferred<Any>

    @POST("addKlas")
    fun postAddClass(@Body body: RoomClassName): Deferred<Any>
}