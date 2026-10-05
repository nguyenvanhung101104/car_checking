package com.example.obd2diagnostic.utils

object DtcDescriptionMap {
    private val dtcMap = mapOf(
        "P0101" to "Mass Air Flow (MAF) Circuit Range/Performance Problem",
        "P0113" to "Intake Air Temperature (IAT) Sensor 1 Circuit High Input",
        "P0171" to "System Too Lean (Bank 1)",
        "P0300" to "Random or Multiple Cylinder Misfire Detected",
        "P0301" to "Cylinder 1 Misfire Detected",
        "P0420" to "Catalyst System Efficiency Below Threshold (Bank 1)",
        "P0442" to "Evaporative Emission (EVAP) System Leak Detected (small leak)",
        "P0500" to "Vehicle Speed Sensor (VSS) Malfunction",
        "P0700" to "Transmission Control System (MIL Request)",
        "P0043" to "Turbocharger/Supercharger Bypass Valve Control Circuit Low"
    )

    fun getDescription(code: String): String {
        return dtcMap[code] ?: "Generic Fault Code - Consult vehicle service manual for details."
    }
}
