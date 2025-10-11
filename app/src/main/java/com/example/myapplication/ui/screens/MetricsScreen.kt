package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.model.ReportStatus
import com.example.myapplication.model.ReportType
import com.example.myapplication.viewmodel.ReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetricsScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReportViewModel = viewModel()
) {
    val reports by viewModel.reports.collectAsState()
    val metrics = viewModel.getReportMetrics()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Métricas y Estadísticas") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Total de reportes
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total de Reportes",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${metrics.total}",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Reportes por tipo
            Text(
                text = "Reportes por Tipo",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ReportType.values().forEach { type ->
                val count = metrics.byType[type] ?: 0
                MetricCard(
                    title = type.displayName,
                    count = count,
                    total = metrics.total
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Reportes por estado
            Text(
                text = "Reportes por Estado",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ReportStatus.values().forEach { status ->
                val count = metrics.byStatus[status] ?: 0
                val color = if (status == ReportStatus.PENDIENTE)
                    MaterialTheme.colorScheme.errorContainer
                else
                    MaterialTheme.colorScheme.tertiaryContainer

                MetricCard(
                    title = status.displayName,
                    count = count,
                    total = metrics.total,
                    containerColor = color
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Porcentaje de resolución
            if (metrics.total > 0) {
                val resueltos = metrics.byStatus[ReportStatus.RESUELTO] ?: 0
                val porcentaje = (resueltos.toFloat() / metrics.total.toFloat() * 100).toInt()

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Tasa de Resolución",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "$porcentaje%",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        LinearProgressIndicator(
                            progress = porcentaje / 100f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    count: Int,
    total: Int,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                if (total > 0) {
                    val percentage = (count.toFloat() / total.toFloat() * 100).toInt()
                    Text(
                        text = "$percentage% del total",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

