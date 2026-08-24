package com.example.obd2diagnostic

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.obd2diagnostic.ui.viewmodels.DiagnosticViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: DiagnosticViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvStatus = findViewById<TextView>(R.id.tv_connection_status)
        val tvRpm = findViewById<TextView>(R.id.tv_rpm)
        val tvSpeed = findViewById<TextView>(R.id.tv_speed)
        val tvTemp = findViewById<TextView>(R.id.tv_temp)
        val tvLoad = findViewById<TextView>(R.id.tv_load)
        val btnDisconnect = findViewById<Button>(R.id.btn_disconnect)

        // Observe ViewModel
        viewModel.connectionStatus.observe(this) { status ->
            tvStatus.text = "ELM Status: $status"
        }

        viewModel.rpm.observe(this) { rpm -> tvRpm.text = "RPM: $rpm" }
        viewModel.speed.observe(this) { speed -> tvSpeed.text = "Speed: $speed" }
        viewModel.coolantTemp.observe(this) { temp -> tvTemp.text = "Temp: $temp" }
        viewModel.engineLoad.observe(this) { load -> tvLoad.text = "Load: $load" }

        btnDisconnect.setOnClickListener {
            viewModel.stopReadingData()
        }
    }
}
