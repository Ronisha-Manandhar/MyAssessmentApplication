package com.example.myassssmentapplication

// Imports necessary Android and Jetpack components for UI and system insets handling
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// Abstract base activity to simplify layout inflation and edge-to-edge setup for child activities
abstract class BaseActivity(@LayoutRes private val layoutRes: Int) : AppCompatActivity() {

    // Called when the activity is starting
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()         // Allows UI elements to extend behind system bars
        setContentView(layoutRes) // Inflates the layout resource provided by subclasses
        applyWindowInsets()       // Applies padding based on system bar insets
    }

    // Enables edge-to-edge layout behavior by adjusting system UI flags
    private fun enableEdgeToEdge() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    // Applies window insets to prevent content from being obscured by system bars
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets // Return the insets to continue propagation if needed
        }
    }
}