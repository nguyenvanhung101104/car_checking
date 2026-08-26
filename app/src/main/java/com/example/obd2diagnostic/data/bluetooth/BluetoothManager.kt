package com.example.obd2diagnostic.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.example.obd2diagnostic.data.obd.ObdCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class BluetoothManager {

    private val sppUuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var socket: BluetoothSocket? = null
    private val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    var isConnected = false
        private set

    fun isBluetoothEnabled(): Boolean {
        return adapter?.isEnabled == true
    }

    @SuppressLint("MissingPermission")
    fun getPairedDevices(): List<BluetoothDevice> {
        return adapter?.bondedDevices?.toList() ?: emptyList()
    }

    @SuppressLint("MissingPermission")
    suspend fun connect(deviceAddress: String): Boolean = withContext(Dispatchers.IO) {
        if (deviceAddress == "MOCK_DEVICE") {
            isConnected = true
            return@withContext true
        }
        try {
            val device: BluetoothDevice = adapter?.getRemoteDevice(deviceAddress) ?: return@withContext false
            socket = device.createRfcommSocketToServiceRecord(sppUuid)
            socket?.connect()
            isConnected = true
            true
        } catch (e: IOException) {
            e.printStackTrace()
            isConnected = false
            false
        }
    }

    suspend fun sendCommand(command: ObdCommand): String = withContext(Dispatchers.IO) {
        if (!isConnected) return@withContext "Not Connected"
        
        // Nếu là Emulator hoặc Mock Mode
        if (socket == null) {
            val mockRes = com.example.obd2diagnostic.utils.MockObdServer.getMockResponse(command.command)
            command.rawResponse = mockRes
            // Giả lập việc clean response
            command.cleanResponse()
            return@withContext command.getFormattedResult()
        }

        try {
            val socket = socket ?: return@withContext "Socket Null"
            command.run(socket.inputStream, socket.outputStream)
            command.getFormattedResult()
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    fun disconnect() {
        try {
            socket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            isConnected = false
            socket = null
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: BluetoothManager? = null

        fun getInstance(): BluetoothManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BluetoothManager().also { INSTANCE = it }
            }
        }
    }
}
