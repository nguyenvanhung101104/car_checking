package com.example.obd2diagnostic.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DiagnosticDao {
    @Insert
    suspend fun insertSession(session: DiagnosticSession)

    @Query("SELECT * FROM diagnostic_sessions ORDER BY timestamp DESC")
    suspend fun getAllSessions(): List<DiagnosticSession>
}
