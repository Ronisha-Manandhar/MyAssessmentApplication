package com.example.myassssmentapplication.data.model

/**
 *
 * Response from the login API.
 *
 * @property keypass Authentication token for subsequent dashboard requests.
 */
data class LoginResponse(
    val keypass: String
)