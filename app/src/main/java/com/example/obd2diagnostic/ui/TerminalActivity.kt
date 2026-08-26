package com.example.obd2diagnostic.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.obd2diagnostic.R
import com.example.obd2diagnostic.data.bluetooth.BluetoothManager
import com.example.obd2diagnostic.data.obd.ObdCommand
import kotlinx.coroutines.launch

class TerminalActivity : AppCompatActivity() {

    private lateinit var tvOutput: TextView
    private lateinit var etInput: EditText
    private lateinit var btnSend: Button
    private lateinit var scrollTerminal: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terminal)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_terminal)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        tvOutput = findViewById(R.id.tv_terminal_output)
        etInput = findViewById(R.id.et_terminal_input)
        btnSend = findViewById(R.id.btn_terminal_send)
        scrollTerminal = findViewById(R.id.scroll_terminal)

        btnSend.setOnClickListener {
            val commandText = etInput.text.toString().trim()
            if (commandText.isNotEmpty()) {
                sendCustomCommand(commandText)
                etInput.text.clear()
            }
        }
    }

    private fun sendCustomCommand(cmd: String) {
        tvOutput.append("\n> $cmd")
        
        lifecycleScope.launch {
            val response = BluetoothManager.getInstance().sendCommand(object : ObdCommand(cmd) {
                override fun getFormattedResult(): String = rawResponse
            })
            tvOutput.append("\n$response")
            scrollTerminal.post { scrollTerminal.fullScroll(ScrollView.FOCUS_DOWN) }
        }
    }
}
