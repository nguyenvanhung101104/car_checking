package com.example.obd2diagnostic.data.obd

import java.io.InputStream
import java.io.OutputStream

abstract class ObdCommand(val command: String) {

    var rawResponse: String = ""
    var formattedResponse: String = ""

    @Throws(Exception::class)
    fun run(inputStream: InputStream, outputStream: OutputStream) {
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
        while (true) {
            b = inputStream.read()
            if (b == -1 || b.toChar() == '>') break
            res.append(b.toChar())
        }
        rawResponse = res.toString().trim()
        cleanResponse()
    }

    fun cleanResponse() {
        // Loại bỏ echo và khoảng trắng
        formattedResponse = rawResponse.replace("\\s".toRegex(), "")
            .replace(command.replace("\\s".toRegex(), ""), "")
    }

    abstract fun getFormattedResult(): String
}
