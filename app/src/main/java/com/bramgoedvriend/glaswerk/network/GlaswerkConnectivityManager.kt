package com.bramgoedvriend.glaswerk.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class GlaswerkConnectivityManager private constructor(private val manager: ConnectivityManager) {
    @Suppress("DEPRECATION")
    fun hasInternet(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = manager.activeNetwork
            val capabilities = manager.getNetworkCapabilities(network)
            capabilities != null && (
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
        } else {
            val networktype = manager.activeNetworkInfo!!.type
            networktype == ConnectivityManager.TYPE_WIFI ||
                    networktype == ConnectivityManager.TYPE_MOBILE
        }
    }

    companion object {
        @Volatile private var instance: GlaswerkConnectivityManager? = null

        /**
         * Retrieves a Singleton of the Connectivity manager
         */
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: GlaswerkConnectivityManager(
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ).also { instance = it }
            }
    }
}
