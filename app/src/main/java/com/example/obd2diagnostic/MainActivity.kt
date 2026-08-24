package com.example.obd2diagnostic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupDashboard()
    }

    private fun setupDashboard() {
        // Dashboard
        findViewById<View>(R.id.item_dashboard).apply {
            findViewById<TextView>(R.id.item_text).text = getString(R.string.menu_dashboard)
            // icon remains default for now or can be set
        }

        // Live Data
        findViewById<View>(R.id.item_live_data).apply {
            findViewById<TextView>(R.id.item_text).text = getString(R.string.menu_live_data)
        }

        // All Sensors
        findViewById<View>(R.id.item_sensors).apply {
            findViewById<TextView>(R.id.item_text).text = getString(R.string.menu_all_sensors)
        }

        // DTC
        findViewById<View>(R.id.item_dtc).apply {
            findViewById<TextView>(R.id.item_text).text = getString(R.string.menu_dtc)
        }

        // Freeze Frame
        findViewById<View>(R.id.item_freeze_frame).apply {
            findViewById<TextView>(R.id.item_text).text = getString(R.string.menu_freeze_frame)
        }

        // Monitors
        findViewById<View>(R.id.item_monitors).apply {
            findViewById<TextView>(R.id.item_text).text = getString(R.string.menu_monitors)
        }

        // Acceleration
        findViewById<View>(R.id.item_acceleration).apply {
            findViewById<TextView>(R.id.item_text).text = getString(R.string.menu_acceleration)
        }

        // Emission
        findViewById<View>(R.id.item_emission).apply {
            findViewById<TextView>(R.id.item_text).text = getString(R.string.menu_emission)
        }

        // Terminal
        findViewById<View>(R.id.item_terminal).apply {
            findViewById<TextView>(R.id.item_text).text = getString(R.string.menu_terminal)
        }
    }
}
