package com.example.obd2diagnostic.utils

import kotlin.random.Random

/**
 * Lớp hỗ trợ tạo dữ liệu giả lập cho Emulator
 */
object MockObdServer {

    fun getMockResponse(command: String): String {
        return when (command) {
            "01 0C" -> "41 0C ${generateHex(2)}" // RPM
            "01 0D" -> "41 0D ${generateHex(1)}" // Speed
            "01 05" -> "41 05 ${generateHex(1)}" // Temp
            "01 04" -> "41 04 ${generateHex(1)}" // Load
            else -> "NO DATA"
        }
    }

    private fun generateHex(bytes: Int): String {
        val sb = StringBuilder()
        for (i in 0 until bytes) {
            val h = Random.nextInt(0, 255).toString(16).uppercase().padStart(2, '0')
            sb.append(h).append(" ")
        }
        return sb.toString().trim()
    }
}
