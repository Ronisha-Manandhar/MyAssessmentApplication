// LoginRequest.kt
package com.example.myassssmentapplication

/**
 * Request body for the login API.
 *
 * @property username The user’s first name.
 * @property password The student’s ID (format: s12345678).
 */
data class LoginRequest(
    val username: String,
    val password: String
)
