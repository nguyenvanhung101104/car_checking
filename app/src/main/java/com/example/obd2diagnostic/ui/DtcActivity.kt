package com.example.obd2diagnostic.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obd2diagnostic.R
import com.example.obd2diagnostic.data.bluetooth.BluetoothManager
import com.example.obd2diagnostic.data.obd.ClearDtcCommand
import com.example.obd2diagnostic.data.obd.ReadDtcCommand
import com.example.obd2diagnostic.ui.adapters.DtcAdapter
import com.example.obd2diagnostic.ui.adapters.DtcItem
import kotlinx.coroutines.launch

class DtcActivity : AppCompatActivity() {

    private lateinit var rvDtc: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dtc)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_dtc)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        rvDtc = findViewById(R.id.rv_dtc_list)
        rvDtc.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btn_read_dtc).setOnClickListener {
            readCodes()
        }

        findViewById<Button>(R.id.btn_clear_dtc).setOnClickListener {
            clearCodes()
        }
    }

    private fun readCodes() {
        lifecycleScope.launch {
            val allDtcItems = mutableListOf<DtcItem>()
            
            // 1. Read Confirmed DTCs (Mode 03)
            val confirmedResult = BluetoothManager.getInstance(this@DtcActivity).sendCommand(ReadDtcCommand("03"))
            if (confirmedResult.contains("P") || confirmedResult.contains("C") || confirmedResult.contains("B") || confirmedResult.contains("U")) {
                confirmedResult.split(", ").forEach { 
                    allDtcItems.add(DtcItem(it, "Confirmed"))
                }
            }

            // 2. Read Pending DTCs (Mode 07)
            val pendingResult = BluetoothManager.getInstance(this@DtcActivity).sendCommand(ReadDtcCommand("07"))
            if (pendingResult.contains("P") || pendingResult.contains("C") || pendingResult.contains("B") || pendingResult.contains("U")) {
                pendingResult.split(", ").forEach {
                    allDtcItems.add(DtcItem(it, "Pending/History"))
                }
            }

            if (allDtcItems.isNotEmpty()) {
                rvDtc.adapter = DtcAdapter(allDtcItems)
            } else {
                Toast.makeText(this@DtcActivity, "No DTCs found", Toast.LENGTH_SHORT).show()
                rvDtc.adapter = DtcAdapter(emptyList())
            }
        }
    }

    private fun clearCodes() {
        lifecycleScope.launch {
            val result = BluetoothManager.getInstance(this@DtcActivity).sendCommand(ClearDtcCommand())
            Toast.makeText(this@DtcActivity, result, Toast.LENGTH_LONG).show()
            // Auto refresh list
            readCodes()
        }
    }
}
