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
    private val repository = ObdRepository.getInstance(
        BluetoothManager.getInstance(application), 
        database.diagnosticDao()
    )

    private val _connectionStatus = MutableLiveData<String>("Disconnected")
    val connectionStatus: LiveData<String> = _connectionStatus

    // Expose repository flows as LiveData for activities
    val rpm: LiveData<String> = repository.rpm.asLiveData()
    val speed: LiveData<String> = repository.speed.asLiveData()
    val coolantTemp: LiveData<String> = repository.coolantTemp.asLiveData()
    val engineLoad: LiveData<String> = repository.engineLoad.asLiveData()
    val vin: LiveData<String> = repository.vin.asLiveData()
    val protocol: LiveData<String> = repository.protocol.asLiveData()
    val sensors: LiveData<Map<String, String>> = repository.sensors.asLiveData()

    fun getPairedDevices() = repository.getPairedDevices()
    fun isBluetoothEnabled() = repository.isBluetoothEnabled()
    fun isConnected() = repository.isConnected()

    fun connectToDevice(address: String) {
        viewModelScope.launch {
            _connectionStatus.postValue("Connecting...")
            val result = repository.connect(address)
            
            if (result.isSuccess) {
                // Initialize OBD
                if (address != "MOCK_DEVICE") {
                    delay(500)
                    repository.runCommand(ResetCommand())
                    delay(500)
                    repository.runCommand(EchoOffCommand())
                    delay(200)
                    repository.runCommand(LineFeedOffCommand())
                    delay(200)
                    repository.runCommand(SelectProtocolAutoCommand())
                    delay(500)
                }
                _connectionStatus.postValue("Connected")
                repository.startReadingData()
            } else {
                val error = result.exceptionOrNull()?.message ?: "Unknown Error"
                _connectionStatus.postValue("Failed: $error")
            }
        }
    }

    fun stopReadingData() {
        repository.disconnect()
        _connectionStatus.postValue("Disconnected")
    }

    override fun onCleared() {
        super.onCleared()
    }
}
