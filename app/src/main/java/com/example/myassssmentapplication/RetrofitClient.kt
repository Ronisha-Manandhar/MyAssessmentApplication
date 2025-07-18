package com.example.myassssmentapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton provider for Retrofit and ApiService.
 * Sets up the base URL and JSON converter once, then
 * exposes a single ApiService instance for the app.
 */
object RetrofitClient {

    // Base endpoint for all API calls
    private const val BASE_URL = "https://nit3213api.onrender.com/"

    // Lazily initialized ApiService using Retrofit
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
