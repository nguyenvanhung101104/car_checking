package com.example.obd2diagnostic.`data`.db

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _diagnosticDao: Lazy<DiagnosticDao> = lazy {
    DiagnosticDao_Impl(this)
  }


  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "f76520898f9b0a125c8e0decae388621", "70331f82c14f0a13cf6ddc11e17b7543") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `diagnostic_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `vin` TEXT, `dtcCount` INTEGER NOT NULL, `maxRpm` INTEGER NOT NULL, `maxSpeed` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'f76520898f9b0a125c8e0decae388621')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `diagnostic_sessions`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsDiagnosticSessions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsDiagnosticSessions.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDiagnosticSessions.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDiagnosticSessions.put("vin", TableInfo.Column("vin", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDiagnosticSessions.put("dtcCount", TableInfo.Column("dtcCount", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDiagnosticSessions.put("maxRpm", TableInfo.Column("maxRpm", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDiagnosticSessions.put("maxSpeed", TableInfo.Column("maxSpeed", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysDiagnosticSessions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesDiagnosticSessions: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoDiagnosticSessions: TableInfo = TableInfo("diagnostic_sessions",
            _columnsDiagnosticSessions, _foreignKeysDiagnosticSessions, _indicesDiagnosticSessions)
        val _existingDiagnosticSessions: TableInfo = read(connection, "diagnostic_sessions")
        if (!_infoDiagnosticSessions.equals(_existingDiagnosticSessions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |diagnostic_sessions(com.example.obd2diagnostic.data.db.DiagnosticSession).
              | Expected:
              |""".trimMargin() + _infoDiagnosticSessions + """
              |
              | Found:
              |""".trimMargin() + _existingDiagnosticSessions)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "diagnostic_sessions")
  }

  public override fun clearAllTables() {
    super.performClear(false, "diagnostic_sessions")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(DiagnosticDao::class, DiagnosticDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun diagnosticDao(): DiagnosticDao = _diagnosticDao.value
}
