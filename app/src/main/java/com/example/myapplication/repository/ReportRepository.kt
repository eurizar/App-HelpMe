package com.example.myapplication.repository

import android.net.Uri
import com.example.myapplication.model.Report
import com.example.myapplication.model.ReportStatus
import com.example.myapplication.model.ReportType
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ReportRepository {
    private val database = FirebaseDatabase.getInstance().reference
    private val storage = FirebaseStorage.getInstance().reference
    private val reportsRef = database.child("reports")

    suspend fun createReport(
        type: ReportType,
        description: String,
        latitude: Double,
        longitude: Double,
        imageUri: Uri?,
        userId: String
    ): Result<String> {
        return try {
            val reportId = reportsRef.push().key ?: UUID.randomUUID().toString()

            val photoUrl = if (imageUri != null) {
                uploadImage(reportId, imageUri)
            } else {
                ""
            }

            val report = Report(
                id = reportId,
                type = type,
                description = description,
                latitude = latitude,
                longitude = longitude,
                photoUrl = photoUrl,
                status = ReportStatus.PENDIENTE,
                userId = userId,
                timestamp = System.currentTimeMillis()
            )

            reportsRef.child(reportId).setValue(report).await()
            Result.success(reportId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun uploadImage(reportId: String, imageUri: Uri): String {
        val imageRef = storage.child("reports/$reportId.jpg")
        imageRef.putFile(imageUri).await()
        return imageRef.downloadUrl.await().toString()
    }

    fun getAllReports(): Flow<List<Report>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reports = mutableListOf<Report>()
                for (child in snapshot.children) {
                    child.getValue(Report::class.java)?.let { reports.add(it) }
                }
                trySend(reports)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        reportsRef.addValueEventListener(listener)
        awaitClose { reportsRef.removeEventListener(listener) }
    }

    suspend fun updateReportStatus(reportId: String, status: ReportStatus): Result<Unit> {
        return try {
            reportsRef.child(reportId).child("status").setValue(status).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

