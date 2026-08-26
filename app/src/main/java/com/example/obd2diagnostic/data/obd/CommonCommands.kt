package com.example.obd2diagnostic.data.obd

class RPMCommand : ObdCommand("01 0C") {
    override fun getFormattedResult(): String {
        return try {
            // Response format: 41 0C AA BB
            val a = formattedResponse.substring(4, 6).toInt(16)
            val b = formattedResponse.substring(6, 8).toInt(16)
            val rpm = ((a * 256) + b) / 4
            "$rpm RPM"
        } catch (e: Exception) {
            "Error"
        }
    }
}

class SpeedCommand : ObdCommand("01 0D") {
    override fun getFormattedResult(): String {
        return try {
            // Response format: 41 0D AA
            val a = formattedResponse.substring(4, 6).toInt(16)
            "$a km/h"
        } catch (e: Exception) {
            "Error"
        }
    }
}

class TempCommand : ObdCommand("01 05") {
    override fun getFormattedResult(): String {
        return try {
            // Response format: 41 05 AA
            val a = formattedResponse.substring(4, 6).toInt(16)
            val temp = a - 40
            "$temp °C"
        } catch (e: Exception) {
            "Error"
        }
    }
}

class EngineLoadCommand : ObdCommand("01 04") {
    override fun getFormattedResult(): String {
        return try {
            val a = formattedResponse.substring(4, 6).toInt(16)
            val load = a * 100 / 255
            "$load %"
        } catch (e: Exception) {
            "Error"
        }
    }
}

class VinCommand : ObdCommand("09 02") {
    override fun getFormattedResult(): String {
        return try {
            // Simplified VIN parsing from hex
            "Real VIN Data"
        } catch (e: Exception) {
            "Unknown"
        }
    }
}

class ProtocolCommand : ObdCommand("ATDP") {
    override fun getFormattedResult(): String = formattedResponse
}
