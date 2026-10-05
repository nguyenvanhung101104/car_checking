package com.example.obd2diagnostic.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obd2diagnostic.R
import com.example.obd2diagnostic.ui.adapters.SensorAdapter
import com.example.obd2diagnostic.ui.adapters.SensorItem
import com.example.obd2diagnostic.ui.viewmodels.DiagnosticViewModel

class LiveDataActivity : AppCompatActivity() {

    private val viewModel: DiagnosticViewModel by viewModels()
    private lateinit var adapter: SensorAdapter
    private var moduleName: String = "Live Data"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_live_data)
        setSupportActionBar(toolbar)
        moduleName = intent.getStringExtra("MODULE_NAME") ?: "Live Data"
        supportActionBar?.title = moduleName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val rvSensors = findViewById<RecyclerView>(R.id.rv_live_data)
        rvSensors.layoutManager = LinearLayoutManager(this)
        adapter = SensorAdapter(emptyList())
        rvSensors.adapter = adapter

        viewModel.sensors.observe(this) { sensorMap ->
            val filteredItems = when (moduleName) {
                getString(R.string.menu_live_data) -> {
                    sensorMap.filter { it.key in listOf("RPM", "Speed", "Coolant Temp", "Engine Load", "Voltage", "Throttle") }
                        .map { SensorItem(it.key, it.value) }
                }
                getString(R.string.menu_all_sensors) -> {
                    sensorMap.map { SensorItem(it.key, it.value) }
                }
                getString(R.string.menu_emission) -> {
                    sensorMap.filter { it.key in listOf("Intake Pressure", "Coolant Temp", "MAF Flow", "ST Fuel Trim", "LT Fuel Trim", "O2 Sensor 1", "O2 Sensor 2", "Catalyst Temp") }
                        .map { SensorItem(it.key, it.value) }
                }
                getString(R.string.menu_acceleration) -> {
                    sensorMap.filter { it.key in listOf("Speed", "RPM", "Timing Advance", "Throttle", "Engine Load") }
                        .map { SensorItem(it.key, it.value) }
                }
                getString(R.string.menu_monitors) -> {
                    sensorMap.filter { it.key in listOf("Voltage", "Fuel Level", "Barometric", "Runtime", "Dist since clear", "Fuel Sys Status") }
                        .map { SensorItem(it.key, it.value) }
                }
                else -> sensorMap.map { SensorItem(it.key, it.value) }
            }
            adapter.updateData(filteredItems)
        }
    }
}
