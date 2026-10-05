package com.example.obd2diagnostic.utils

import kotlin.random.Random

/**
 * Lớp hỗ trợ tạo dữ liệu giả lập cho Emulator
 */
object MockObdServer {

    fun getMockResponse(command: String): String {
        return when (command) {
            "01 0C" -> {
                // RPM: 800 - 3000. (RPM * 4) = 3200 - 12000
                val value = Random.nextInt(3200, 12000)
                val a = (value / 256).toString(16).uppercase().padStart(2, '0')
                val b = (value % 256).toString(16).uppercase().padStart(2, '0')
                "41 0C $a $b"
            }
            "01 0D" -> {
                // Speed: 0 - 120 km/h
                val value = Random.nextInt(0, 120).toString(16).uppercase().padStart(2, '0')
                "41 0D $value"
            }
            "01 05" -> {
                // Temp: 80 - 105 C. (Temp + 40) = 120 - 145
                val value = Random.nextInt(120, 145).toString(16).uppercase().padStart(2, '0')
                "41 05 $value"
            }
            "01 04" -> {
                // Load: 10 - 80 %. (Load * 255 / 100) = 25 - 204
                val value = Random.nextInt(25, 204).toString(16).uppercase().padStart(2, '0')
                "41 04 $value"
            }
            "01 0F" -> "41 0F ${Random.nextInt(50, 100).toString(16).uppercase().padStart(2, '0')}" // IAT
            "01 10" -> "41 10 01 ${Random.nextInt(10, 50).toString(16).uppercase().padStart(2, '0')}" // MAF
            "01 06" -> "41 06 ${Random.nextInt(100, 150).toString(16).uppercase().padStart(2, '0')}" // STFT
            "01 07" -> "41 07 ${Random.nextInt(110, 140).toString(16).uppercase().padStart(2, '0')}" // LTFT
            "01 33" -> "41 33 ${Random.nextInt(90, 110).toString(16).uppercase().padStart(2, '0')}" // Baro
            "01 1F" -> "41 1F 0A ${Random.nextInt(0, 255).toString(16).uppercase().padStart(2, '0')}" // Runtime
            "01 31" -> "41 31 02 ${Random.nextInt(0, 255).toString(16).uppercase().padStart(2, '0')}" // Distance
            "01 11" -> "41 11 ${Random.nextInt(20, 100).toString(16).uppercase().padStart(2, '0')}" // Throttle
            "01 0B" -> "41 0B ${Random.nextInt(30, 150).toString(16).uppercase().padStart(2, '0')}" // Intake Map
            "01 2F" -> "41 2F ${Random.nextInt(20, 255).toString(16).uppercase().padStart(2, '0')}" // Fuel Level
            "01 0E" -> "41 0E ${Random.nextInt(50, 150).toString(16).uppercase().padStart(2, '0')}" // Timing
            "01 14" -> "41 14 ${Random.nextInt(0, 200).toString(16).uppercase().padStart(2, '0')}" // O2 V1
            "01 15" -> "41 15 ${Random.nextInt(0, 200).toString(16).uppercase().padStart(2, '0')}" // O2 V2
            "01 03" -> "41 03 02" // Fuel Status: Closed Loop
            "01 3C" -> "41 3C 0A ${Random.nextInt(50, 255).toString(16).uppercase().padStart(2, '0')}" // Cat Temp
            "ATRV" -> "14.2V"
            "03" -> "43 01 01 01 13" // Mock DTCs: P0101 and P0113
            "07" -> "47 00 43" // Mock Pending DTC: P0043
            "04" -> "44" // Success clearing
            else -> "NO DATA"
        }
    }
}
