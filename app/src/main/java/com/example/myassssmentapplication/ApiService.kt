package com.example.myassssmentapplication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Defines API endpoints for authentication and dashboard data.
 * Uses Retrofit annotations to declare HTTP methods and URL paths.
 */
interface ApiService {

    /**
     * Performs user login with provided credentials.
     * @param request LoginRequest containing username and password.
     * @return Call<LoginResponse> with keypass on success.
     */
    @POST("sydney/auth")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    /**
     * Fetches dashboard entities using the keypass.
     * @param keypass Authentication token from login response.
     * @return Call<DashboardResponse> containing the list of entities.
     */
    @GET("dashboard/{keypass}")
    fun getDashboardData(@Path("keypass") keypass: String): Call<DashboardResponse>
}