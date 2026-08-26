package com.example.obd2diagnostic

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
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
        val btnDisconnect = findViewById<Button>(R.id.btn_disconnect)

        // Observe ViewModel
        viewModel.connectionStatus.observe(this) { status ->
            tvElmStatus.text = status
            tvEcuStatus.text = status
            
            if (status == "Connected") {
                tvElmStatus.setTextColor(ContextCompat.getColor(this, R.color.status_green))
                tvEcuStatus.setTextColor(ContextCompat.getColor(this, R.color.status_green))
                btnDisconnect.setText(R.string.btn_disconnect)
                btnDisconnect.setBackgroundColor(ContextCompat.getColor(this, R.color.disconnect_red))
            } else {
                tvElmStatus.setTextColor(ContextCompat.getColor(this, R.color.disconnect_red))
                tvEcuStatus.setTextColor(ContextCompat.getColor(this, R.color.disconnect_red))
                btnDisconnect.setText(R.string.btn_connect)
                btnDisconnect.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_blue))
            }
        }

        btnDisconnect.setOnClickListener {
            if (viewModel.connectionStatus.value == "Connected") {
                viewModel.stopReadingData()
            } else {
                viewModel.connectToDevice("MOCK_DEVICE")
            }
        }

        setupDashboard()
        
        // Tự động kết nối giả lập để kiểm tra UI
        viewModel.connectToDevice("MOCK_DEVICE")
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
            Toast.makeText(this, "Opening ${tv.text}", Toast.LENGTH_SHORT).show()
        }
    }
}
