package com.example.myassssmentapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * MainActivity handles:
 * 1. Validating username & password inputs
 * 2. Displaying UI error messages
 * 3. Performing the login API call
 * 4. Persisting the keypass in SharedPreferences
 * 5. Navigating to DashboardActivity
 */
class MainActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var tvLoginError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bind UI elements
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton      = findViewById(R.id.loginButton)
        tvLoginError     = findViewById(R.id.tvLoginError)

        // Hide error TextView at start
        tvLoginError.visibility = View.GONE

        loginButton.setOnClickListener {
            // Clear any prior error
            tvLoginError.visibility = View.GONE

            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Simple input validation
            if (username.isEmpty() || password.isEmpty()) {
                tvLoginError.text = "Please enter both first name and student ID"
                tvLoginError.visibility = View.VISIBLE
            } else {
                performLogin(username, password)
            }
        }
    }

    /**
     * Executes the login call, handles responses, and on success
     * saves the keypass to SharedPreferences and opens Dashboard.
     */
    private fun performLogin(username: String, password: String) {
        // Prevent double‑taps
        loginButton.isEnabled = false

        val request = LoginRequest(username, password)
        RetrofitClient.instance.login(request)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    // Re-enable button
                    loginButton.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        // Extract keypass
                        val keypass = response.body()!!.keypass

                        // Persist keypass for the rest of the app
                        getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                            .edit()
                            .putString("KEYPASS", keypass)
                            .apply()

                        // Navigate to DashboardActivity
                        val dashboardIntent = Intent(
                            this@MainActivity,
                            DashboardActivity::class.java
                        )
                        startActivity(dashboardIntent)
                        finish()
                    } else {
                        // Show HTTP error code
                        tvLoginError.text = "Login failed: ${response.code()}"
                        tvLoginError.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Re-enable button
                    loginButton.isEnabled = true

                    // Show network error
                    tvLoginError.text = "Network error: ${t.localizedMessage}"
                    tvLoginError.visibility = View.VISIBLE
                }
            })
    }
}
