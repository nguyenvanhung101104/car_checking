package com.example.obd2diagnostic.data.repository

import com.example.obd2diagnostic.data.bluetooth.BluetoothManager
import com.example.obd2diagnostic.data.db.DiagnosticDao
import com.example.obd2diagnostic.data.db.DiagnosticSession
import com.example.obd2diagnostic.data.obd.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ObdRepository private constructor(
    private val bluetoothManager: BluetoothManager,
    private val diagnosticDao: DiagnosticDao
) {
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var readingJob: Job? = null

    private val _rpm = MutableStateFlow("0 RPM")
    val rpm = _rpm.asStateFlow()

    private val _speed = MutableStateFlow("0 km/h")
    val speed = _speed.asStateFlow()

    private val _coolantTemp = MutableStateFlow("0 °C")
    val coolantTemp = _coolantTemp.asStateFlow()

    private val _engineLoad = MutableStateFlow("0 %")
    val engineLoad = _engineLoad.asStateFlow()

    private val _vin = MutableStateFlow("---")
    val vin = _vin.asStateFlow()

    private val _protocol = MutableStateFlow("---")
    val protocol = _protocol.asStateFlow()

    private val _sensors = MutableStateFlow<Map<String, String>>(emptyMap())
    val sensors = _sensors.asStateFlow()

    suspend fun connect(address: String): Result<Boolean> {
        val result = bluetoothManager.connect(address)
        if (result.isSuccess) {
            startReadingData()
        }
        return result
    }

    fun getPairedDevices() = bluetoothManager.getPairedDevices()

    fun isConnected() = bluetoothManager.isConnected

    fun isBluetoothEnabled() = bluetoothManager.isBluetoothEnabled()

    fun disconnect() {
        stopReadingData()
        bluetoothManager.disconnect()
    }

    fun startReadingData() {
        if (readingJob?.isActive == true) return
        
        readingJob = repositoryScope.launch {
            // First get static info
            _vin.value = runCommand(VinCommand())
            _protocol.value = runCommand(ProtocolCommand())
            
            while (isActive && isConnected()) {
                val rpmVal = runCommand(RPMCommand())
                val speedVal = runCommand(SpeedCommand())
                val tempVal = runCommand(TempCommand())
                val loadVal = runCommand(EngineLoadCommand())
                val voltVal = runCommand(VoltageCommand())
                val throttleVal = runCommand(ThrottleCommand())
                val intakeVal = runCommand(IntakePressureCommand())
                val fuelVal = runCommand(FuelLevelCommand())
                val timingVal = runCommand(TimingAdvanceCommand())
                val iatVal = runCommand(IntakeAirTempCommand())
                val mafVal = runCommand(MafFlowCommand())
                val stftVal = runCommand(FuelTrimCommand(true))
                val ltftVal = runCommand(FuelTrimCommand(false))
                val baroVal = runCommand(BarometricPressureCommand())
                val runtimeVal = runCommand(RuntimeCommand())
                val distVal = runCommand(DistanceSinceClearCommand())
                val o2v1Val = runCommand(O2SensorVoltageCommand(1))
                val o2v2Val = runCommand(O2SensorVoltageCommand(2))
                val fuelSysVal = runCommand(FuelSystemStatusCommand())
                val catTempVal = runCommand(CatalystTempCommand())

                _rpm.value = rpmVal
                _speed.value = speedVal
                _coolantTemp.value = tempVal
                _engineLoad.value = loadVal

                _sensors.value = linkedMapOf(
                    "RPM" to rpmVal,
                    "Speed" to speedVal,
                    "Coolant Temp" to tempVal,
                    "Engine Load" to loadVal,
                    "Voltage" to voltVal,
                    "Throttle" to throttleVal,
                    "Intake Pressure" to intakeVal,
                    "Intake Air Temp" to iatVal,
                    "MAF Flow" to mafVal,
                    "Fuel Level" to fuelVal,
                    "ST Fuel Trim" to stftVal,
                    "LT Fuel Trim" to ltftVal,
                    "Timing Advance" to timingVal,
                    "Barometric" to baroVal,
                    "Fuel Sys Status" to fuelSysVal,
                    "O2 Sensor 1" to o2v1Val,
                    "O2 Sensor 2" to o2v2Val,
                    "Catalyst Temp" to catTempVal,
                    "Runtime" to runtimeVal,
                    "Dist since clear" to distVal
                )
                
                delay(1000)
            }
        }
    }

    fun stopReadingData() {
        readingJob?.cancel()
        _rpm.value = "0 RPM"
        _speed.value = "0 km/h"
        _coolantTemp.value = "0 °C"
        _engineLoad.value = "0 %"
        _vin.value = "---"
        _protocol.value = "---"
        _sensors.value = emptyMap()
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

    companion object {
        @Volatile
        private var INSTANCE: ObdRepository? = null

        fun getInstance(bluetoothManager: BluetoothManager, diagnosticDao: DiagnosticDao): ObdRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ObdRepository(bluetoothManager, diagnosticDao).also { INSTANCE = it }
            }
        }
    }
}
