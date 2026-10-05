package com.example.obd2diagnostic.data.obd

import java.io.InputStream
import java.io.OutputStream

abstract class ObdCommand(val command: String) {

    var rawResponse: String = ""
    var formattedResponse: String = ""

    @Throws(Exception::class)
    fun run(inputStream: InputStream, outputStream: OutputStream) {
        // Clear input buffer before sending
        while (inputStream.available() > 0) {
            inputStream.read()
        }
        
        sendCommand(outputStream)
        readResponse(inputStream)
    }

    private fun sendCommand(outputStream: OutputStream) {
        outputStream.write((command + "\r").toByteArray())
        outputStream.flush()
    }

    private fun readResponse(inputStream: InputStream) {
        val res = StringBuilder()
        var b: Int
        val startTime = System.currentTimeMillis()
        val timeout = 2000L // 2 seconds timeout

        while (true) {
            if (System.currentTimeMillis() - startTime > timeout) {
                break
            }
            
            if (inputStream.available() > 0) {
                b = inputStream.read()
                if (b == -1 || b.toChar() == '>') break
                res.append(b.toChar())
            } else {
                Thread.sleep(10)
            }
        }
        rawResponse = res.toString().trim()
        cleanResponse()
    }

    fun cleanResponse() {
        // Loại bỏ khoảng trắng, dấu nhắc lệnh và các ký tự điều khiển
        formattedResponse = rawResponse.replace("\\s".toRegex(), "")
            .replace(">".toRegex(), "")
            .uppercase()
    }

    abstract fun getFormattedResult(): String
}
