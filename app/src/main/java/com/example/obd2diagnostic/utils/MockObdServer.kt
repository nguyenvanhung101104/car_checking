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
            else -> "NO DATA"
        }
    }
}
