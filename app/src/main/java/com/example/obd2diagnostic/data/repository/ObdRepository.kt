package com.example.obd2diagnostic.data.repository

import com.example.obd2diagnostic.data.bluetooth.BluetoothManager
import com.example.obd2diagnostic.data.db.DiagnosticDao
import com.example.obd2diagnostic.data.db.DiagnosticSession
import com.example.obd2diagnostic.data.obd.ObdCommand

class ObdRepository(
    private val bluetoothManager: BluetoothManager,
    private val diagnosticDao: DiagnosticDao
) {

    suspend fun connect(address: String): Boolean {
        return bluetoothManager.connect(address)
    }

    fun getPairedDevices() = bluetoothManager.getPairedDevices()

    fun isBluetoothEnabled() = bluetoothManager.isBluetoothEnabled()

    fun disconnect() {
        bluetoothManager.disconnect()
    }

    suspend fun runCommand(command: ObdCommand): String {
        return bluetoothManager.sendCommand(command)
    }

    suspend fun saveSession(session: DiagnosticSession) {
        diagnosticDao.insertSession(session)
    }

    suspend fun getHistory(): List<DiagnosticSession> {
        return diagnosticDao.getAllSessions()
    }
}
