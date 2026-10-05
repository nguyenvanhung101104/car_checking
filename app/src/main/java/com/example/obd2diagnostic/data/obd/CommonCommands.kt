package com.example.obd2diagnostic.data.obd

class RPMCommand : ObdCommand("01 0C") {
    override fun getFormattedResult(): String {
        return try {
            // Tìm "410C" trong phản hồi
            val index = formattedResponse.indexOf("410C")
            if (index != -1) {
                val a = formattedResponse.substring(index + 4, index + 6).toInt(16)
                val b = formattedResponse.substring(index + 6, index + 8).toInt(16)
                val rpm = ((a * 256) + b) / 4
                "$rpm RPM"
            } else "---"
        } catch (e: Exception) { "Error" }
    }
}

class SpeedCommand : ObdCommand("01 0D") {
    override fun getFormattedResult(): String {
        return try {
            val index = formattedResponse.indexOf("410D")
            if (index != -1) {
                val a = formattedResponse.substring(index + 4, index + 6).toInt(16)
                "$a km/h"
            } else "---"
        } catch (e: Exception) { "Error" }
    }
}

class TempCommand : ObdCommand("01 05") {
    override fun getFormattedResult(): String {
        return try {
            val index = formattedResponse.indexOf("4105")
            if (index != -1) {
                val a = formattedResponse.substring(index + 4, index + 6).toInt(16)
                val temp = a - 40
                "$temp °C"
            } else "---"
        } catch (e: Exception) { "Error" }
    }
}

class EngineLoadCommand : ObdCommand("01 04") {
    override fun getFormattedResult(): String {
        return try {
            val index = formattedResponse.indexOf("4104")
            if (index != -1) {
                val a = formattedResponse.substring(index + 4, index + 6).toInt(16)
                val load = a * 100 / 255
                "$load %"
            } else "---"
        } catch (e: Exception) { "Error" }
    }
}

class VinCommand : ObdCommand("09 02") {
    override fun getFormattedResult(): String {
        return try {
            // Phản hồi Mode 09 02 thường dài và chia thành nhiều dòng
            // Ví dụ: 49 02 01 00 00 00 31 ... (Dạng ASCII hex)
            if (formattedResponse.length >= 20) {
                val hexData = formattedResponse.substring(6) // Bỏ qua mod/pid header
                val vin = StringBuilder()
                for (i in 0 until hexData.length step 2) {
                    if (i + 2 <= hexData.length) {
                        val part = hexData.substring(i, i + 2)
                        val char = part.toInt(16).toChar()
                        if (char.isLetterOrDigit()) vin.append(char)
                    }
                }
                vin.toString()
            } else {
                "Not Available"
            }
        } catch (e: Exception) {
            "---"
        }
    }
}

class ProtocolCommand : ObdCommand("ATDP") {
    override fun getFormattedResult(): String = formattedResponse
}

class VoltageCommand : ObdCommand("ATRV") {
    override fun getFormattedResult(): String = formattedResponse
}

class ThrottleCommand : ObdCommand("01 11") {
    override fun getFormattedResult(): String {
        return try {
            val a = formattedResponse.substring(4, 6).toInt(16)
            val pos = a * 100 / 255
            "$pos %"
        } catch (e: Exception) { "Error" }
    }
}

class IntakePressureCommand : ObdCommand("01 0B") {
    override fun getFormattedResult(): String {
        return try {
            val a = formattedResponse.substring(4, 6).toInt(16)
            "$a kPa"
        } catch (e: Exception) { "Error" }
    }
}

class FuelLevelCommand : ObdCommand("01 2F") {
    override fun getFormattedResult(): String {
        return try {
            val a = formattedResponse.substring(4, 6).toInt(16)
            val level = a * 100 / 255
            "$level %"
        } catch (e: Exception) { "Error" }
    }
}

class TimingAdvanceCommand : ObdCommand("01 0E") {
    override fun getFormattedResult(): String {
        return try {
            val index = formattedResponse.indexOf("410E")
            if (index != -1) {
                val a = formattedResponse.substring(index + 4, index + 6).toInt(16)
                val timing = (a / 2) - 64
                "$timing °"
            } else "---"
        } catch (e: Exception) { "Error" }
    }
}

class IntakeAirTempCommand : ObdCommand("01 0F") {
    override fun getFormattedResult(): String {
        return try {
            val index = formattedResponse.indexOf("410F")
            if (index != -1) {
                val a = formattedResponse.substring(index + 4, index + 6).toInt(16)
                val temp = a - 40
                "$temp °C"
            } else "---"
        } catch (e: Exception) { "Error" }
    }
}

class MafFlowCommand : ObdCommand("01 10") {
    override fun getFormattedResult(): String {
        return try {
            val index = formattedResponse.indexOf("4110")
            if (index != -1) {
                val a = formattedResponse.substring(index + 4, index + 6).toInt(16)
                val b = formattedResponse.substring(index + 6, index + 8).toInt(16)
                val flow = ((a * 256) + b) / 100.0
                "$flow g/s"
            } else "---"
        } catch (e: Exception) { "Error" }
    }
}

class FuelTrimCommand(val shortTerm: Boolean = true) : ObdCommand(if (shortTerm) "01 06" else "01 07") {
    override fun getFormattedResult(): String {
        return try {
            val pid = if (shortTerm) "4106" else "4107"
            val index = formattedResponse.indexOf(pid)
            if (index != -1) {
                val a = formattedResponse.substring(index + 4, index + 6).toInt(16)
                val trim = (a - 128) * 100 / 128.0
                String.format("%.1f %%", trim)
            } else "---"
        } catch (e: Exception) { "Error" }
    }
}

class BarometricPressureCommand : ObdCommand("01 33") {
    override fun getFormattedResult(): String {
        return try {
            val index = formattedResponse.indexOf("4133")
            if (index != -1) {
                val a = formattedResponse.substring(index + 4, index + 6).toInt(16)
                "$a kPa"
            } else "---"
        } catch (e: Exception) { "Error" }
    }
}

class RuntimeCommand : ObdCommand("01 1F") {
    override fun getFormattedResult(): String {
        return try {
            val index = formattedResponse.indexOf("411F")
            if (index != -1) {
                val a = formattedResponse.substring(index + 4, index + 6).toInt(16)
                val b = formattedResponse.substring(index + 6, index + 8).toInt(16)
                val seconds = (a * 256) + b
                val min = seconds / 60
                val sec = seconds % 60
                "$min m $sec s"
            } else "---"
        } catch (e: Exception) { "Error" }
    }
}

class DistanceSinceClearCommand : ObdCommand("01 31") {
    override fun getFormattedResult(): String {
        return try {
            val index = formattedResponse.indexOf("4131")
            if (index != -1) {
                val a = formattedResponse.substring(index + 4, index + 6).toInt(16)
                val b = formattedResponse.substring(index + 6, index + 8).toInt(16)
                val km = (a * 256) + b
                "$km km"
            } else "---"
        } catch (e: Exception) { "Error" }
    }
}

class O2SensorVoltageCommand(val sensor: Int = 1) : ObdCommand(if (sensor == 1) "01 14" else "01 15") {
    override fun getFormattedResult(): String {
        return try {
            val pid = if (sensor == 1) "4114" else "4115"
            val index = formattedResponse.indexOf(pid)
            if (index != -1) {
                val a = formattedResponse.substring(index + 4, index + 6).toInt(16)
                val volt = a / 200.0
                String.format("%.2f V", volt)
            } else "---"
        } catch (e: Exception) { "Error" }
    }
}

class FuelSystemStatusCommand : ObdCommand("01 03") {
    override fun getFormattedResult(): String {
        return try {
            val index = formattedResponse.indexOf("4103")
            if (index != -1) {
                val a = formattedResponse.substring(index + 4, index + 6).toInt(16)
                when (a) {
                    1 -> "Open Loop"
                    2 -> "Closed Loop"
                    4 -> "OL - Driving"
                    8 -> "OL - Fault"
                    16 -> "CL - Fault"
                    else -> "Unknown"
                }
            } else "---"
        } catch (e: Exception) { "Error" }
    }
}

class CatalystTempCommand : ObdCommand("01 3C") {
    override fun getFormattedResult(): String {
        return try {
            val index = formattedResponse.indexOf("413C")
            if (index != -1) {
                val a = formattedResponse.substring(index + 4, index + 6).toInt(16)
                val b = formattedResponse.substring(index + 6, index + 8).toInt(16)
                val temp = (((a * 256) + b) / 10.0) - 40
                String.format("%.1f °C", temp)
            } else "---"
        } catch (e: Exception) { "Error" }
    }
}
