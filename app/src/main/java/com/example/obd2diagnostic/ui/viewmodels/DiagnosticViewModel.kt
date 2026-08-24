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

    private var isReading = false

    fun connectToDevice(address: String) {
        viewModelScope.launch {
            _connectionStatus.postValue("Connecting...")
            if (repository.connect(address)) {
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
    }

    override fun onCleared() {
        super.onCleared()
        stopReadingData()
    }
}
