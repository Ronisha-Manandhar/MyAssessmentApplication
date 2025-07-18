// DashboardResponse.kt
package com.example.myassssmentapplication

/**
 * Response from the dashboard API.
 *
 * @property entities List of entities, each a map of key/value pairs.
 * @property entityTotal Total number of entities available.
 */
data class DashboardResponse(
    val entities: List<Map<String, Any>>,
    val entityTotal: Int
)
