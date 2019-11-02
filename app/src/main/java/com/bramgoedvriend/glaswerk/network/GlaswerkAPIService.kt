package com.bramgoedvriend.glaswerk.network

import com.bramgoedvriend.glaswerk.domain.Item
import io.reactivex.Observable
import retrofit2.http.GET

interface GlaswerkAPI {
    @get:GET("item")
    val item: Observable<List<Item>>
}