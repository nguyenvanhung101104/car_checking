package com.example.obd2diagnostic

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.obd2diagnostic.ui.viewmodels.DiagnosticViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: DiagnosticViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvElmStatus = findViewById<TextView>(R.id.tv_elm_status)
        val tvEcuStatus = findViewById<TextView>(R.id.tv_ecu_status)
        val tvVin = findViewById<TextView>(R.id.tv_vin)
        val tvProtocol = findViewById<TextView>(R.id.tv_protocol)
        val btnDisconnect = findViewById<Button>(R.id.btn_disconnect)

        // Observe ViewModel
        viewModel.connectionStatus.observe(this) { status ->
            tvElmStatus.text = status
            tvEcuStatus.text = status
            
            when (status) {
                "Connected" -> {
                    tvElmStatus.setTextColor(ContextCompat.getColor(this, R.color.status_green))
                    tvEcuStatus.setTextColor(ContextCompat.getColor(this, R.color.status_green))
                    btnDisconnect.setText(R.string.btn_disconnect)
                    btnDisconnect.setBackgroundColor(ContextCompat.getColor(this, R.color.disconnect_red))
                    btnDisconnect.isEnabled = true
                }
                "Connecting..." -> {
                    tvElmStatus.setTextColor(ContextCompat.getColor(this, R.color.text_grey))
                    tvEcuStatus.setTextColor(ContextCompat.getColor(this, R.color.text_grey))
                    btnDisconnect.setText(R.string.btn_connecting)
                    btnDisconnect.setBackgroundColor(ContextCompat.getColor(this, R.color.text_grey))
                    btnDisconnect.isEnabled = false
                }
                else -> {
                    if (status != "Disconnected" && status.contains("Failed")) {
                        Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
                    }
                    tvElmStatus.setTextColor(ContextCompat.getColor(this, R.color.disconnect_red))
                    tvEcuStatus.setTextColor(ContextCompat.getColor(this, R.color.disconnect_red))
                    btnDisconnect.setText(R.string.btn_connect)
                    btnDisconnect.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_blue))
                    btnDisconnect.isEnabled = true
                }
            }
        }

        viewModel.vin.observe(this) { vin -> tvVin.text = vin }
        viewModel.protocol.observe(this) { protocol -> tvProtocol.text = protocol }

        btnDisconnect.setOnClickListener {
            if (viewModel.connectionStatus.value == "Connected") {
                viewModel.stopReadingData()
            } else {
                checkPermissionsAndConnect()
            }
        }

        setupDashboard()
    }

    private fun checkPermissionsAndConnect() {
        if (!viewModel.isBluetoothEnabled()) {
            Toast.makeText(this, "Please turn on Bluetooth", Toast.LENGTH_SHORT).show()
            return
        }

        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN)
        } else {
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            showDevicePicker()
        } else {
            requestPermissionLauncher.launch(permissions)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        if (results.all { it.value }) {
            showDevicePicker()
        } else {
            Toast.makeText(this, "Permissions required for Bluetooth", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun showDevicePicker() {
        val devices = viewModel.getPairedDevices()
        if (devices.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("No Devices Found")
                .setMessage("No paired Bluetooth devices found. Please pair your OBD2 adapter in Android Settings first.")
                .setPositiveButton("Settings") { _, _ ->
                    startActivity(Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS))
                }
                .setNegativeButton("Mock Mode") { _, _ ->
                    viewModel.connectToDevice("MOCK_DEVICE")
                }
                .setNeutralButton("Cancel", null)
                .show()
            return
        }

        val deviceNames = devices.map { it.name ?: "Unknown" }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Select OBD2 Adapter")
            .setItems(deviceNames) { _, which ->
                viewModel.connectToDevice(devices[which].address)
            }
            .setNeutralButton("Mock Mode") { _, _ ->
                viewModel.connectToDevice("MOCK_DEVICE")
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupDashboard() {
        setupGridItem(findViewById(R.id.item_dashboard), R.string.menu_dashboard, R.drawable.ic_dashboard)
        setupGridItem(findViewById(R.id.item_live_data), R.string.menu_live_data, R.drawable.ic_live_data)
        setupGridItem(findViewById(R.id.item_sensors), R.string.menu_all_sensors, R.drawable.ic_sensors)
        
        setupGridItem(findViewById(R.id.item_dtc), R.string.menu_dtc, R.drawable.ic_dtc)
        setupGridItem(findViewById(R.id.item_freeze_frame), R.string.menu_freeze_frame, R.drawable.ic_freeze_frame)
        setupGridItem(findViewById(R.id.item_monitors), R.string.menu_monitors, R.drawable.ic_monitors)
        
        setupGridItem(findViewById(R.id.item_acceleration), R.string.menu_acceleration, R.drawable.ic_acceleration)
        setupGridItem(findViewById(R.id.item_emission), R.string.menu_emission, R.drawable.ic_emission)
        setupGridItem(findViewById(R.id.item_terminal), R.string.menu_terminal, R.drawable.ic_terminal)
    }

    private fun setupGridItem(view: View?, textRes: Int, iconRes: Int) {
        if (view == null) return
        val tv = view.findViewById<TextView>(R.id.item_text) ?: return
        val iv = view.findViewById<ImageView>(R.id.item_icon) ?: return
        
        tv.text = getString(textRes)
        iv.setImageResource(iconRes)
        
        view.setOnClickListener {
            handleModuleClick(textRes)
        }
    }

    private fun handleModuleClick(textRes: Int) {
        val moduleName = getString(textRes)
        when (textRes) {
            R.string.menu_terminal -> {
                startActivity(Intent(this, com.example.obd2diagnostic.ui.TerminalActivity::class.java))
            }
            R.string.menu_dtc -> {
                startActivity(Intent(this, com.example.obd2diagnostic.ui.DtcActivity::class.java))
            }
            else -> {
                Toast.makeText(this, "Module $moduleName coming soon", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
