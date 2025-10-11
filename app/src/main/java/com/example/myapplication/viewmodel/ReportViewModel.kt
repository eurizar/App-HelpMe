package com.example.myapplication.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Report
import com.example.myapplication.model.ReportStatus
import com.example.myapplication.model.ReportType
import com.example.myapplication.repository.ReportRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportViewModel : ViewModel() {
    private val repository = ReportRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports

    private val _createReportState = MutableStateFlow<CreateReportState>(CreateReportState.Idle)
    val createReportState: StateFlow<CreateReportState> = _createReportState

    private val _updateReportState = MutableStateFlow<UpdateReportState>(UpdateReportState.Idle)
    val updateReportState: StateFlow<UpdateReportState> = _updateReportState

    init {
        loadReports()
    }

    private fun loadReports() {
        viewModelScope.launch {
            repository.getAllReports().collect { reportList ->
                _reports.value = reportList
            }
        }
    }

    fun createReport(
        type: ReportType,
        description: String,
        latitude: Double,
        longitude: Double,
        imageUri: Uri?
    ) {
        if (description.isBlank()) {
            _createReportState.value = CreateReportState.Error("Por favor ingrese una descripción")
            return
        }

        val userId = auth.currentUser?.uid ?: ""

        viewModelScope.launch {
            _createReportState.value = CreateReportState.Loading
            repository.createReport(type, description, latitude, longitude, imageUri, userId)
                .onSuccess {
                    _createReportState.value = CreateReportState.Success
                }
                .onFailure { error ->
                    _createReportState.value = CreateReportState.Error(
                        error.message ?: "Error al crear reporte"
                    )
                }
        }
    }

    fun updateReportStatus(reportId: String, status: ReportStatus) {
        viewModelScope.launch {
            _updateReportState.value = UpdateReportState.Loading
            repository.updateReportStatus(reportId, status)
                .onSuccess {
                    _updateReportState.value = UpdateReportState.Success
                }
                .onFailure { error ->
                    _updateReportState.value = UpdateReportState.Error(
                        error.message ?: "Error al actualizar reporte"
                    )
                }
        }
    }

    fun getFilteredReports(
        type: ReportType? = null,
        status: ReportStatus? = null
    ): List<Report> {
        var filtered = _reports.value

        type?.let {
            filtered = filtered.filter { report -> report.type == it }
        }

        status?.let {
            filtered = filtered.filter { report -> report.status == it }
        }

        return filtered.sortedByDescending { it.timestamp }
    }

    fun getActiveReports(): List<Report> {
        return _reports.value.filter { it.status == ReportStatus.PENDIENTE }
    }

    fun getReportMetrics(): ReportMetrics {
        val allReports = _reports.value
        val byType = ReportType.values().associateWith { type ->
            allReports.count { it.type == type }
        }
        val byStatus = ReportStatus.values().associateWith { status ->
            allReports.count { it.status == status }
        }

        return ReportMetrics(
            total = allReports.size,
            byType = byType,
            byStatus = byStatus
        )
    }

    fun resetCreateReportState() {
        _createReportState.value = CreateReportState.Idle
    }

    fun resetUpdateReportState() {
        _updateReportState.value = UpdateReportState.Idle
    }
}

sealed class CreateReportState {
    object Idle : CreateReportState()
    object Loading : CreateReportState()
    object Success : CreateReportState()
    data class Error(val message: String) : CreateReportState()
}

sealed class UpdateReportState {
    object Idle : UpdateReportState()
    object Loading : UpdateReportState()
    object Success : UpdateReportState()
    data class Error(val message: String) : UpdateReportState()
}

data class ReportMetrics(
    val total: Int,
    val byType: Map<ReportType, Int>,
    val byStatus: Map<ReportStatus, Int>
)

