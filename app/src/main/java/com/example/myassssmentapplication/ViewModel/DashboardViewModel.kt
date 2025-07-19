package com.example.myassssmentapplication.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myassssmentapplication.data.model.DashboardResponse
import com.example.myassssmentapplication.data.network.ApiService
import com.example.myassssmentapplication.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * Exposes a list of entities and any error message via LiveData.
 * ViewModel for fetching dashboard data.
 *
 */
class DashboardViewModel(
    private val api: ApiService = RetrofitClient.instance
) : ViewModel() {

    private val _entities = MutableLiveData<List<Map<String, Any>>>()
    val entities: LiveData<List<Map<String, Any>>> get() = _entities

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    /**
     * Fetch dashboard data using the provided keypass.
     * Clears previous data and error state before making the request.
     *
     * @param keypass Authorization token from login response.
     */
    fun fetchDashboardData(keypass: String) {
        // Reset any existing data or error
        _entities.value = emptyList()
        _error.value = null

        api.getDashboardData(keypass).enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(
                call: Call<DashboardResponse>,
                response: Response<DashboardResponse>
            ) {
                if (response.isSuccessful) {
                    // Successful response: update entities
                    val result = response.body()?.entities.orEmpty()
                    Log.d("DashboardViewModel", "Fetched ${result.size} entities")
                    _entities.value = result
                } else {
                    // Map HTTP error codes to user-friendly messages
                    val code = response.code()
                    val message = when {
                        code == 401 -> "Unauthorized. Please log in again."
                        code == 404 -> "No dashboard data found."
                        code in 500..599 -> "Server error ($code). Please try later."
                        else -> "Error fetching data: $code"
                    }
                    Log.e("DashboardViewModel", message)
                    _error.value = message
                }
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                // Network or unexpected error
                val msg = t.localizedMessage ?: "Unknown network error"
                Log.e("DashboardViewModel", "Network error: $msg")
                _error.value = "Network error: $msg"
            }
        })
    }
}