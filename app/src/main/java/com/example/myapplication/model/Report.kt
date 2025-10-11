package com.example.myapplication.model

data class Report(
    val id: String = "",
    val type: ReportType = ReportType.SEGURIDAD,
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val photoUrl: String = "",
    val status: ReportStatus = ReportStatus.PENDIENTE,
    val userId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

enum class ReportType(val displayName: String) {
    SEGURIDAD("Seguridad"),
    INFRAESTRUCTURA("Infraestructura"),
    ANIMAL_PERDIDO("Animal Perdido"),
    ACCIDENTE("Accidente"),
    OTRO("Otro")
}

enum class ReportStatus(val displayName: String) {
    PENDIENTE("Pendiente"),
    RESUELTO("Resuelto")
}

