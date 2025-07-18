package com.example.myassssmentapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myassssmentapplication.data.model.LoginRequest
import com.example.myassssmentapplication.data.model.LoginResponse
import com.example.myassssmentapplication.data.network.ApiService
import com.example.myassssmentapplication.util.AppConfig
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

