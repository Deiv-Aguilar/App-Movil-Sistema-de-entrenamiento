package com.example.sistemaentrenamientocorporalypreparacinfisica

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant

class HealthConnectManager(private val context: Context) {

    val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }

    fun isAvailable(): Boolean {
        return HealthConnectClient.getSdkStatus(context) == HealthConnectClient.SDK_AVAILABLE
    }

    suspend fun hasAllPermissions(permissions: Set<String>): Boolean {
        if (!isAvailable()) return false
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        return granted.containsAll(permissions)
    }

    suspend fun getUltimoPeso(): Double? {
        if (!isAvailable()) return null

        val request = ReadRecordsRequest(
            recordType = WeightRecord::class,
            timeRangeFilter = TimeRangeFilter.after(Instant.EPOCH)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records.lastOrNull()?.weight?.inKilograms
    }

    suspend fun getUltimaAltura(): Double? {
        if (!isAvailable()) return null

        val request = ReadRecordsRequest(
            recordType = HeightRecord::class,
            timeRangeFilter = TimeRangeFilter.after(Instant.EPOCH)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records.lastOrNull()?.height?.inMeters
    }

    suspend fun getUltimaGrasaCorporal(): Double? {
        if (!isAvailable()) return null

        val request = ReadRecordsRequest(
            recordType = BodyFatRecord::class,
            timeRangeFilter = TimeRangeFilter.after(Instant.EPOCH)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records.lastOrNull()?.percentage?.value
    }

    suspend fun getUltimaMasaMuscular(): Double? {
        if (!isAvailable()) return null

        // 1. Intentar obtener el registro directo de LeanBodyMass (Masa Magra en KG)
        val request = ReadRecordsRequest(
            recordType = LeanBodyMassRecord::class,
            timeRangeFilter = TimeRangeFilter.after(Instant.EPOCH)
        )
        val response = healthConnectClient.readRecords(request)
        val masaMagraLectura = response.records.lastOrNull()?.mass?.inKilograms

        if (masaMagraLectura != null) {
            return masaMagraLectura
        }

        // 2. Si FitDays no mandó LeanBodyMassRecord, se deduce dinámicamente con su Peso y Grasa
        val peso = getUltimoPeso()
        val grasa = getUltimaGrasaCorporal()

        return if (peso != null && grasa != null && peso > 0) {
            // Regresa los kg de masa muscular aproximados para CUALQUIER usuario que se pese
            val masaMagraKg = peso * (1.0 - (grasa / 100.0))
            masaMagraKg * 0.932
        } else {
            null
        }
    }
}