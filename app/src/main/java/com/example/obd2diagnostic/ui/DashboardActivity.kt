package com.example.obd2diagnostic.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.obd2diagnostic.R
import com.example.obd2diagnostic.ui.viewmodels.DiagnosticViewModel

class DashboardActivity : AppCompatActivity() {

    private val viewModel: DiagnosticViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_dashboard)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val tvRpm = findViewById<TextView>(R.id.tv_dash_rpm)
        val tvSpeed = findViewById<TextView>(R.id.tv_dash_speed)
        val tvTemp = findViewById<TextView>(R.id.tv_dash_temp)
        val tvLoad = findViewById<TextView>(R.id.tv_dash_load)

        viewModel.rpm.observe(this) { tvRpm.text = it }
        viewModel.speed.observe(this) { tvSpeed.text = it }
        viewModel.coolantTemp.observe(this) { tvTemp.text = it }
        viewModel.engineLoad.observe(this) { tvLoad.text = it }
    }
}
