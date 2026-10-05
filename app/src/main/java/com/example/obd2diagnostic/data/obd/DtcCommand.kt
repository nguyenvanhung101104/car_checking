package com.example.obd2diagnostic.data.obd

class ReadDtcCommand(val mode: String = "03") : ObdCommand(mode) {
    override fun getFormattedResult(): String {
        // Clean response usually looks like "430101" for mode 03 or "470101" for mode 07
        val expectedHeader = if (mode == "03") "43" else "47"
        
        if (formattedResponse.startsWith(expectedHeader)) {
            val codes = mutableListOf<String>()
            val hexCodes = formattedResponse.substring(2)
            
            // Each DTC is 4 hex characters
            for (i in 0 until hexCodes.length step 4) {
                if (i + 4 <= hexCodes.length) {
                    val code = hexCodes.substring(i, i + 4)
                    if (code != "0000" && code.length == 4) {
                        // First character mapping
                        val firstChar = when (code[0]) {
                            '0' -> "P0"
                            '1' -> "P1"
                            '2' -> "P2"
                            '3' -> "P3"
                            '4' -> "C0"
                            '5' -> "C1"
                            '6' -> "C2"
                            '7' -> "C3"
                            '8' -> "B0"
                            '9' -> "B1"
                            'A' -> "B2"
                            'B' -> "B3"
                            'C' -> "U0"
                            'D' -> "U1"
                            'E' -> "U2"
                            'F' -> "U3"
                            else -> "P"
                        }
                        codes.add(firstChar + code.substring(1))
                    }
                }
            }
            return if (codes.isEmpty()) "No DTCs found" else codes.joinToString(", ")
        }
        return "No DTCs found"
    }
}

class ClearDtcCommand : ObdCommand("04") {
    override fun getFormattedResult(): String {
        return if (formattedResponse.contains("44") || formattedResponse.contains("OK")) {
            "Codes Cleared Successfully"
        } else {
            "Clear Failed. Make sure Engine is OFF and Ignition is ON."
        }
    }
}
