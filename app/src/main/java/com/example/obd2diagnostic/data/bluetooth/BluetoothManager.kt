package com.example.obd2diagnostic.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager as AndroidBluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import com.example.obd2diagnostic.data.obd.ObdCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class BluetoothManager private constructor(context: Context) {

    private val sppUuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var socket: BluetoothSocket? = null
    
    private val androidBluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as AndroidBluetoothManager
    private val adapter: BluetoothAdapter? = androidBluetoothManager.adapter

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
    suspend fun connect(deviceAddress: String): Result<Boolean> = withContext(Dispatchers.IO) {
        if (deviceAddress == "MOCK_DEVICE") {
            isConnected = true
            return@withContext Result.success(true)
        }
        
        try {
            // Rất quan trọng: Phải dừng quét thiết bị trước khi kết nối
            if (adapter?.isDiscovering == true) {
                adapter.cancelDiscovery()
            }
            delay(500) // Tăng delay cho Samsung

            val device: BluetoothDevice = adapter?.getRemoteDevice(deviceAddress) 
                ?: return@withContext Result.failure(Exception("Device not found"))
            
            var lastException: Exception? = null
            
            // Thử các cách kết nối khác nhau
            val connectionMethods = listOf(
                { device.createInsecureRfcommSocketToServiceRecord(sppUuid) },
                { device.createRfcommSocketToServiceRecord(sppUuid) },
                { 
                    val m = device.javaClass.getMethod("createRfcommSocket", Int::class.javaPrimitiveType)
                    m.invoke(device, 1) as BluetoothSocket
                }
            )

            for (method in connectionMethods) {
                try {
                    socket?.close()
                    socket = method()
                    socket?.connect()
                    isConnected = true
                    return@withContext Result.success(true)
                } catch (e: Exception) {
                    lastException = e
                    socket?.close()
                    socket = null
                    delay(200)
                }
            }
            
            isConnected = false
            Result.failure(lastException ?: Exception("Connection failed after all attempts"))
        } catch (e: Exception) {
            e.printStackTrace()
            socket?.close()
            socket = null
            isConnected = false
            Result.failure(e)
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

        fun getInstance(context: Context): BluetoothManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BluetoothManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
