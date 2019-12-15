package com.bramgoedvriend.glaswerk.network

import com.bramgoedvriend.glaswerk.network.DTO.NetworkClassContainer
import com.bramgoedvriend.glaswerk.network.DTO.NetworkItemsContainer
import com.bramgoedvriend.glaswerk.network.DTO.NetworkRoomContainer
import com.bramgoedvriend.glaswerk.network.DTO.NetworkStudentContainer
import com.bramgoedvriend.glaswerk.network.DTO.NetworkStudentItemContainer
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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
    fun postStudentItemBrokenAsync(@Body body: StudentItem): Deferred<Any>

    @POST("reduceItem")
    fun postReduceItemAsync(@Body body: ItemId): Deferred<Any>

    @POST("orderItem")
    fun postOrderItemAsync(@Body body: OrderItem): Deferred<Any>

    @POST("addItem")
    fun postAddItem(@Body body: Item): Deferred<Any>

    @POST("editItem")
    fun postEditItem(@Body body: Item): Deferred<Any>

    @POST("deleteItem")
    fun postRemoveItem(@Body body: ItemId): Deferred<Any>

    @POST("addLeerling")
    fun postAddStudent(@Body body: Student): Deferred<Any>

    @POST("editLeerling")
    fun postEditStudent(@Body body: Student): Deferred<Any>

    @POST("deleteLeerling")
    fun postRemoveStudent(@Body body: StudentId): Deferred<Any>

    @POST("addLokaal")
    fun postAddRoom(@Body body: RoomClassName): Deferred<Any>

    @POST("addKlas")
    fun postAddClass(@Body body: RoomClassName): Deferred<Any>
}
