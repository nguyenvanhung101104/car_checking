package com.example.obd2diagnostic.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.obd2diagnostic.data.bluetooth.BluetoothManager
import com.example.obd2diagnostic.data.db.AppDatabase
import com.example.obd2diagnostic.data.obd.*
import com.example.obd2diagnostic.data.repository.ObdRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DiagnosticViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = ObdRepository(BluetoothManager.getInstance(), database.diagnosticDao())

    private val _connectionStatus = MutableLiveData<String>("Disconnected")
    val connectionStatus: LiveData<String> = _connectionStatus

    private val _rpm = MutableLiveData<String>("0 RPM")
    val rpm: LiveData<String> = _rpm

    private val _speed = MutableLiveData<String>("0 km/h")
    val speed: LiveData<String> = _speed

    private val _coolantTemp = MutableLiveData<String>("0 °C")
    val coolantTemp: LiveData<String> = _coolantTemp

    private val _engineLoad = MutableLiveData<String>("0 %")
    val engineLoad: LiveData<String> = _engineLoad

    private val _vin = MutableLiveData<String>("---")
    val vin: LiveData<String> = _vin

    private val _protocol = MutableLiveData<String>("---")
    val protocol: LiveData<String> = _protocol

    private var isReading = false

    fun getPairedDevices() = repository.getPairedDevices()

    fun isBluetoothEnabled() = repository.isBluetoothEnabled()

    fun connectToDevice(address: String) {
        viewModelScope.launch {
            _connectionStatus.postValue("Connecting...")
            if (repository.connect(address)) {
                // Initialize OBD
                if (address != "MOCK_DEVICE") {
                    repository.runCommand(ResetCommand())
                    repository.runCommand(EchoOffCommand())
                    repository.runCommand(LineFeedOffCommand())
                    repository.runCommand(SelectProtocolAutoCommand())
                    
                    _vin.postValue(repository.runCommand(VinCommand()))
                    _protocol.postValue(repository.runCommand(ProtocolCommand()))
                }
                _connectionStatus.postValue("Connected")
                startReadingData()
            } else {
                _connectionStatus.postValue("Connection Failed")
            }
        }
    }

    private fun startReadingData() {
        isReading = true
        viewModelScope.launch {
            while (isReading) {
                _rpm.postValue(repository.runCommand(RPMCommand()))
                _speed.postValue(repository.runCommand(SpeedCommand()))
                _coolantTemp.postValue(repository.runCommand(TempCommand()))
                _engineLoad.postValue(repository.runCommand(EngineLoadCommand()))
                delay(1000)
            }
        }
    }

    fun stopReadingData() {
        isReading = false
        repository.disconnect()
        _connectionStatus.postValue("Disconnected")
        clearData()
    }

    private fun clearData() {
        _rpm.postValue("0 RPM")
        _speed.postValue("0 km/h")
        _coolantTemp.postValue("0 °C")
        _engineLoad.postValue("0 %")
    }

    override fun onCleared() {
        super.onCleared()
        stopReadingData()
    }
}
