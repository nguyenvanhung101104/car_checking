package com.example.obd2diagnostic.`data`.db

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class DiagnosticDao_Impl(
  __db: RoomDatabase,
) : DiagnosticDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfDiagnosticSession: EntityInsertAdapter<DiagnosticSession>
  init {
    this.__db = __db
    this.__insertAdapterOfDiagnosticSession = object : EntityInsertAdapter<DiagnosticSession>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `diagnostic_sessions` (`id`,`timestamp`,`vin`,`dtcCount`,`maxRpm`,`maxSpeed`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DiagnosticSession) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindLong(2, entity.timestamp)
        val _tmpVin: String? = entity.vin
        if (_tmpVin == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpVin)
        }
        statement.bindLong(4, entity.dtcCount.toLong())
        statement.bindLong(5, entity.maxRpm.toLong())
        statement.bindLong(6, entity.maxSpeed.toLong())
      }
    }
  }

  public override suspend fun insertSession(session: DiagnosticSession): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfDiagnosticSession.insert(_connection, session)
  }

  public override suspend fun getAllSessions(): List<DiagnosticSession> {
    val _sql: String = "SELECT * FROM diagnostic_sessions ORDER BY timestamp DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _cursorIndexOfVin: Int = getColumnIndexOrThrow(_stmt, "vin")
        val _cursorIndexOfDtcCount: Int = getColumnIndexOrThrow(_stmt, "dtcCount")
        val _cursorIndexOfMaxRpm: Int = getColumnIndexOrThrow(_stmt, "maxRpm")
        val _cursorIndexOfMaxSpeed: Int = getColumnIndexOrThrow(_stmt, "maxSpeed")
        val _result: MutableList<DiagnosticSession> = mutableListOf()
        while (_stmt.step()) {
          val _item: DiagnosticSession
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_cursorIndexOfTimestamp)
          val _tmpVin: String?
          if (_stmt.isNull(_cursorIndexOfVin)) {
            _tmpVin = null
          } else {
            _tmpVin = _stmt.getText(_cursorIndexOfVin)
          }
          val _tmpDtcCount: Int
          _tmpDtcCount = _stmt.getLong(_cursorIndexOfDtcCount).toInt()
          val _tmpMaxRpm: Int
          _tmpMaxRpm = _stmt.getLong(_cursorIndexOfMaxRpm).toInt()
          val _tmpMaxSpeed: Int
          _tmpMaxSpeed = _stmt.getLong(_cursorIndexOfMaxSpeed).toInt()
          _item =
              DiagnosticSession(_tmpId,_tmpTimestamp,_tmpVin,_tmpDtcCount,_tmpMaxRpm,_tmpMaxSpeed)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
