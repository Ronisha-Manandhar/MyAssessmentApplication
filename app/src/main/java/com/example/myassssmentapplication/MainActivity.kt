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

/**
 * MainActivity:
 * - Handles user login.
 * - Injects ApiService using Koin for dependency injection.
 * - Validates input and makes login network call.
 */
class MainActivity : AppCompatActivity() {

    // Inject ApiService instance from Koin container
    private val api: ApiService by inject()

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var tvLoginError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bind UI components
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        tvLoginError = findViewById(R.id.tvLoginError)
        tvLoginError.visibility = View.GONE

        // Set login button click behavior
        loginButton.setOnClickListener {
            tvLoginError.visibility = View.GONE
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Basic input validation
            if (username.isEmpty() || password.isEmpty()) {
                tvLoginError.text = "Please enter both first name and student ID"
                tvLoginError.visibility = View.VISIBLE
            } else {
                performLogin(username, password)
            }
        }
    }

    /**
     * Makes login network request via injected ApiService.
     */
    private fun performLogin(username: String, password: String) {
        loginButton.isEnabled = false

        val request = LoginRequest(username, password)

        api.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                loginButton.isEnabled = true
                if (response.isSuccessful && response.body() != null) {
                    val keypass = response.body()!!.keypass

                    // Store keypass in SharedPreferences
                    getSharedPreferences(AppConfig.PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putString(AppConfig.KEY_KEYPASS, keypass)

                        .apply()

                    // Navigate to dashboard screen
                    startActivity(Intent(this@MainActivity, DashboardActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                    finish()
                } else {
                    tvLoginError.text = "Login failed: ${response.code()}"
                    tvLoginError.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginButton.isEnabled = true
                tvLoginError.text = "Network error: ${t.localizedMessage}"
                tvLoginError.visibility = View.VISIBLE
            }
        })
    }
}
