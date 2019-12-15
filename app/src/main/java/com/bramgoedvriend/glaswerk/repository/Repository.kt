package com.bramgoedvriend.glaswerk.repository

import androidx.lifecycle.LiveData

interface Repository <T> {
    val data: LiveData<List<T>>
    suspend fun refresh(): Boolean
}
