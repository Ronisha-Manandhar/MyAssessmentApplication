package com.example.myassssmentapplication.di

import com.example.myassssmentapplication.ViewModel.DashboardViewModel
import com.example.myassssmentapplication.data.network.ApiService
import com.example.myassssmentapplication.data.network.RetrofitClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module definitions for dependency injection.
 * Provides network and ViewModel dependencies.
 */
val appModule = module {
    // Provide a singleton ApiService using RetrofitClient
    single<ApiService> { RetrofitClient.instance }

    // Provide DashboardViewModel with injected ApiService
    viewModel { DashboardViewModel(get()) }
}
