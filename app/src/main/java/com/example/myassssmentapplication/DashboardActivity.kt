package com.example.myassssmentapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * DashboardActivity:
 * 1. Reads keypass from SharedPreferences
 * 2. Requests dashboard data via ViewModel
 * 3. Displays error / empty states
 * 4. Shows entities in RecyclerView
 * 5. Navigates to DetailsActivity on item tap
 */
class DashboardActivity : AppCompatActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModel()

    private lateinit var keypassTextView: TextView
    private lateinit var tvDashboardError: TextView
    private lateinit var tvEmpty: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Bind UI components
        keypassTextView  = findViewById(R.id.tvKeypass)
        tvDashboardError = findViewById(R.id.tvDashboardError)
        tvEmpty          = findViewById(R.id.tvEmpty)
        recyclerView     = findViewById(R.id.recyclerView)

        tvDashboardError.visibility = View.GONE
        tvEmpty.visibility = View.GONE

        // Retrieve stored keypass
        val keypass = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getString("KEYPASS", null)

        if (keypass.isNullOrEmpty()) {
            Toast.makeText(
                this,
                "Missing keypass, please log in again",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }

        // Display keypass and configure list
        keypassTextView.text = "Welcome! Your keypass is: $keypass"
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observe data
        dashboardViewModel.entities.observe(this, Observer { list ->
            if (list.isEmpty()) {
                tvEmpty.visibility = View.VISIBLE
                recyclerView.adapter = null
            } else {
                tvEmpty.visibility = View.GONE
                tvDashboardError.visibility = View.GONE
                recyclerView.adapter = EntityAdapter(list) { entity ->
                    // On click, serialize and start DetailsActivity
                    val json = Gson().toJson(entity)
                    val intent = Intent(this, DetailsActivity::class.java)
                    intent.putExtra("entityJson", json)
                    startActivity(intent)
                }
            }
        })

        // Observe errors
        dashboardViewModel.error.observe(this, Observer { msg ->
            if (!msg.isNullOrEmpty()) {
                tvDashboardError.text = msg
                tvDashboardError.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                tvDashboardError.visibility = View.GONE
            }
        })

        // Fetch data once keypass is confirmed
        dashboardViewModel.fetchDashboardData(keypass)
    }
}
