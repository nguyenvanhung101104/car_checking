package com.example.obd2diagnostic.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.obd2diagnostic.R
import com.example.obd2diagnostic.data.bluetooth.BluetoothManager
import com.example.obd2diagnostic.data.obd.ClearDtcCommand
import com.example.obd2diagnostic.data.obd.ReadDtcCommand
import kotlinx.coroutines.launch

class DtcActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dtc)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_dtc)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        findViewById<Button>(R.id.btn_read_dtc).setOnClickListener {
            readCodes()
        }

        findViewById<Button>(R.id.btn_clear_dtc).setOnClickListener {
            clearCodes()
        }
    }

    private fun readCodes() {
        lifecycleScope.launch {
            val result = BluetoothManager.getInstance().sendCommand(ReadDtcCommand())
            Toast.makeText(this@DtcActivity, "Codes: $result", Toast.LENGTH_LONG).show()
        }
    }

    private fun clearCodes() {
        lifecycleScope.launch {
            val result = BluetoothManager.getInstance().sendCommand(ClearDtcCommand())
            Toast.makeText(this@DtcActivity, result, Toast.LENGTH_SHORT).show()
        }
    }
}
