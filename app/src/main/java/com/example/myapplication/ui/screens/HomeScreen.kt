package com.example.myapplication.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCreateReport: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToList: () -> Unit,
    onNavigateToMetrics: () -> Unit,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }

    // Interceptar el botón de retroceso
    BackHandler {
        showExitDialog = true
    }

    // Diálogo de confirmación para salir de la app
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Salir de HelpMe") },
            text = { Text("¿Estás seguro de que deseas salir de la aplicación?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        (context as? Activity)?.finish()
                    }
                ) {
                    Text("Salir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExitDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo de confirmación para cerrar sesión
    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSignOutDialog = false
                        onSignOut()
                    }
                ) {
                    Text("Cerrar Sesión")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSignOutDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HelpMe - Inicio") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { showSignOutDialog = true }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bienvenido a HelpMe",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Text(
                text = "Red de Ayuda Ciudadana",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Botón Crear Reporte
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                onClick = onNavigateToCreateReport
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Crear Reporte",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Reporta una emergencia o problema",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Botón Ver Mapa
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                onClick = onNavigateToMap
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Place,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Ver Mapa",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Visualiza reportes activos en el mapa",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Botón Lista de Reportes
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                onClick = onNavigateToList
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Lista de Reportes",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Ver y filtrar todos los reportes",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Botón Métricas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                onClick = onNavigateToMetrics
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Métricas",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Estadísticas de reportes",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Salir de la App (en la parte inferior)
            OutlinedButton(
                onClick = { showExitDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Salir de la Aplicación")
            }
        }
    }
}
