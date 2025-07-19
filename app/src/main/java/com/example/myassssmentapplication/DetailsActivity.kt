package com.example.myassssmentapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
/**
 *
 * DetailsActivity shows all fields of the selected entity.
 * It reads a JSON string from the intent, parses it to a Map,
 * and inflates a TextView for each key/value pair.
 */
class DetailsActivity : BaseActivity(R.layout.activity_details) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val backButton: Button = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            finish() // This goes back to DashboardActivity
        }
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


        // Retrieve the JSON string passed from DashboardActivity
        val json = intent.getStringExtra("entityJson")
        if (json.isNullOrEmpty()) {
            // No data provided: show a toast and finish
            Toast.makeText(this, "No details available", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Attempt to parse JSON into a Map
        val entityMap: Map<String, Any> = try {
            @Suppress("UNCHECKED_CAST")
            Gson().fromJson(json, Map::class.java) as Map<String, Any>
        } catch (e: JsonSyntaxException) {
            // Invalid JSON: notify user and exit
            Toast.makeText(this, "Error reading entity details", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Find the container to hold our dynamic fields
        val container = findViewById<LinearLayout>(R.id.detailsContainer)
        container.removeAllViews()

        // For each key/value, add a labeled TextView
        for ((key, value) in entityMap) {
            // Field name
            val label = TextView(this).apply {
                text = key.replaceFirstChar { it.uppercase() }
                textSize = 16f
                setPadding(0, 12, 0, 4)
            }
            // Field value
            val content = TextView(this).apply {
                text = value.toString()
                textSize = 14f
                setPadding(0, 0, 0, 12)
            }
            container.addView(label)
            container.addView(content)
        }
    }

    /** Sets up full‑screen edge‑to‑edge padding adjustments. */
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
    }

    /** Shortcut to enable edge‑to‑edge (status/nav bar transparency). */
    private fun enableEdgeToEdge() {
        // Layout content under system bars
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }
}

