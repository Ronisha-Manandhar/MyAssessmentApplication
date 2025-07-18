// LoginResponse.kt
package com.example.myassssmentapplication

/**
 * Response from the login API.
 *
 * @property keypass Authentication token for subsequent dashboard requests.
 */
data class LoginResponse(
    val keypass: String
)
