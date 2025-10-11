package com.example.myapplication.ui.screens

import android.location.Geocoder
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.model.Report
import com.example.myapplication.model.ReportStatus
import com.example.myapplication.model.ReportType
import com.example.myapplication.viewmodel.ReportViewModel
import com.example.myapplication.viewmodel.UpdateReportState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportListScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReportViewModel = viewModel()
) {
    val context = LocalContext.current
    var selectedTypeFilter by remember { mutableStateOf<ReportType?>(null) }
    var selectedStatusFilter by remember { mutableStateOf<ReportStatus?>(null) }
    var showFilterDialog by remember { mutableStateOf(false) }

    val filteredReports = viewModel.getFilteredReports(
        type = selectedTypeFilter,
        status = selectedStatusFilter
    )

    val updateReportState by viewModel.updateReportState.collectAsState()

    LaunchedEffect(updateReportState) {
        when (updateReportState) {
            is UpdateReportState.Success -> {
                Toast.makeText(context, "Reporte actualizado", Toast.LENGTH_SHORT).show()
                viewModel.resetUpdateReportState()
            }
            is UpdateReportState.Error -> {
                Toast.makeText(
                    context,
                    (updateReportState as UpdateReportState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetUpdateReportState()
            }
            else -> {}
        }
    }

    if (showFilterDialog) {
        FilterDialog(
            selectedType = selectedTypeFilter,
            selectedStatus = selectedStatusFilter,
            onTypeSelected = { selectedTypeFilter = it },
            onStatusSelected = { selectedStatusFilter = it },
            onDismiss = { showFilterDialog = false },
            onClearFilters = {
                selectedTypeFilter = null
                selectedStatusFilter = null
                showFilterDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Reportes (${filteredReports.size})") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.Settings, contentDescription = "Filtros")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (filteredReports.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No hay reportes que mostrar")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                items(filteredReports) { report ->
                    ReportCard(
                        report = report,
                        onStatusChange = { newStatus ->
                            viewModel.updateReportStatus(report.id, newStatus)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ReportCard(
    report: Report,
    onStatusChange: (ReportStatus) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showStatusDialog by remember { mutableStateOf(false) }
    var locationAddress by remember { mutableStateOf<String?>(null) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    // Obtener la dirección cuando se carga el componente
    LaunchedEffect(report.id) {
        scope.launch(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(report.latitude, report.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val addressParts = mutableListOf<String>()

                    address.thoroughfare?.let { addressParts.add(it) } // Calle
                    address.subLocality?.let { addressParts.add(it) } // Barrio
                    address.locality?.let { addressParts.add(it) } // Municipio
                    address.adminArea?.let { addressParts.add(it) } // Departamento

                    withContext(Dispatchers.Main) {
                        locationAddress = addressParts.joinToString(", ")
                    }
                }
            } catch (_: Exception) {
                // Si falla, simplemente no mostramos la dirección
            }
        }
    }

    if (showStatusDialog) {
        AlertDialog(
            onDismissRequest = { showStatusDialog = false },
            title = { Text("Cambiar Estado") },
            text = {
                Column {
                    ReportStatus.values().forEach { status ->
                        TextButton(
                            onClick = {
                                onStatusChange(status)
                                showStatusDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(status.displayName)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showStatusDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = report.type.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                AssistChip(
                    onClick = { showStatusDialog = true },
                    label = { Text(report.status.displayName) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (report.status == ReportStatus.PENDIENTE)
                            MaterialTheme.colorScheme.errorContainer
                        else
                            MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = report.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Mostrar dirección si está disponible
                    if (locationAddress != null) {
                        Text(
                            text = "📍 $locationAddress",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Lat: ${"%.6f".format(report.latitude)}, Lng: ${"%.6f".format(report.longitude)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        // Mostrar solo coordenadas mientras se carga la dirección
                        Text(
                            text = "📍 Lat: ${"%.6f".format(report.latitude)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "📍 Lng: ${"%.6f".format(report.longitude)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Text(
                    text = dateFormat.format(Date(report.timestamp)),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (report.photoUrl.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                AsyncImage(
                    model = report.photoUrl,
                    contentDescription = "Foto del reporte",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }
        }
    }
}

@Composable
fun FilterDialog(
    selectedType: ReportType?,
    selectedStatus: ReportStatus?,
    onTypeSelected: (ReportType?) -> Unit,
    onStatusSelected: (ReportStatus?) -> Unit,
    onDismiss: () -> Unit,
    onClearFilters: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filtrar Reportes") },
        text = {
            Column {
                Text("Tipo de Reporte:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    FilterChip(
                        selected = selectedType == null,
                        onClick = { onTypeSelected(null) },
                        label = { Text("Todos") }
                    )
                }

                ReportType.values().forEach { type ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { onTypeSelected(type) },
                            label = { Text(type.displayName) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Estado:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    FilterChip(
                        selected = selectedStatus == null,
                        onClick = { onStatusSelected(null) },
                        label = { Text("Todos") }
                    )
                }

                ReportStatus.values().forEach { status ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        FilterChip(
                            selected = selectedStatus == status,
                            onClick = { onStatusSelected(status) },
                            label = { Text(status.displayName) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aplicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onClearFilters) {
                Text("Limpiar")
            }
        }
    )
}
