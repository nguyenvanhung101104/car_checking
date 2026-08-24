package com.example.obd2diagnostic.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diagnostic_sessions")
data class DiagnosticSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val vin: String?,
    val dtcCount: Int,
    val maxRpm: Int,
    val maxSpeed: Int
)
