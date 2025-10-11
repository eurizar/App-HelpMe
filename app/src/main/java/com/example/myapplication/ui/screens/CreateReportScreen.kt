package com.example.myapplication.ui.screens

import android.Manifest
import android.location.Geocoder
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.model.ReportType
import com.example.myapplication.viewmodel.CreateReportState
import com.example.myapplication.viewmodel.ReportViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReportScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReportViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedType by remember { mutableStateOf(ReportType.SEGURIDAD) }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var currentLatitude by remember { mutableStateOf(0.0) }
    var currentLongitude by remember { mutableStateOf(0.0) }
    var locationAddress by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var locationObtained by remember { mutableStateOf(false) }

    val createReportState by viewModel.createReportState.collectAsState()

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Función para obtener la dirección desde coordenadas
    fun getAddressFromLocation(latitude: Double, longitude: Double) {
        scope.launch {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val addressParts = mutableListOf<String>()

                    address.thoroughfare?.let { addressParts.add(it) } // Calle
                    address.subLocality?.let { addressParts.add(it) } // Barrio
                    address.locality?.let { addressParts.add(it) } // Municipio
                    address.adminArea?.let { addressParts.add(it) } // Departamento

                    locationAddress = addressParts.joinToString(", ")
                } else {
                    locationAddress = "Dirección no disponible"
                }
            } catch (_: Exception) {
                locationAddress = "Error al obtener dirección"
            }
        }
    }

    // Crear archivo temporal para la cámara
    val photoFile = remember {
        File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
    }

    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }

    // Launcher para permisos de ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            // Obtener ubicación actual en tiempo real
            scope.launch {
                try {
                    val cancellationTokenSource = CancellationTokenSource()
                    val location = fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        cancellationTokenSource.token
                    ).await()

                    location?.let {
                        currentLatitude = it.latitude
                        currentLongitude = it.longitude
                        locationObtained = true
                        getAddressFromLocation(it.latitude, it.longitude)
                        Toast.makeText(context, "Ubicación obtenida", Toast.LENGTH_SHORT).show()
                    } ?: run {
                        Toast.makeText(
                            context,
                            "No se pudo obtener la ubicación actual",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (_: SecurityException) {
                    Toast.makeText(context, "Error de permisos", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Error al obtener ubicación: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Permisos de ubicación denegados", Toast.LENGTH_SHORT).show()
        }
    }

    // Launcher para seleccionar imagen de galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    // Launcher para tomar foto con cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = photoUri
        }
    }

    // Launcher para permisos de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(photoUri)
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(createReportState) {
        when (createReportState) {
            is CreateReportState.Success -> {
                Toast.makeText(context, "Reporte creado exitosamente", Toast.LENGTH_SHORT).show()
                viewModel.resetCreateReportState()
                onNavigateBack()
            }
            is CreateReportState.Error -> {
                Toast.makeText(
                    context,
                    (createReportState as CreateReportState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetCreateReportState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Reporte") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
            // Selector de tipo
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedType.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Reporte") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ReportType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.displayName) },
                            onClick = {
                                selectedType = type
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de descripción
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de ubicación actual con icono
            Button(
                onClick = {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (locationObtained)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    if (locationObtained) Icons.Default.Check else Icons.Default.LocationOn,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (locationObtained) "Ubicación Obtenida" else "Obtener Mi Ubicación")
            }

            // Mostrar información de ubicación
            if (locationObtained) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "📍 Ubicación",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (locationAddress.isNotEmpty()) {
                            Text(
                                text = locationAddress,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        Text(
                            text = "Lat: ${"%.6f".format(currentLatitude)}, Lng: ${"%.6f".format(currentLongitude)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones para foto
            Text(
                text = "Agregar Foto",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón seleccionar de galería
                OutlinedButton(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Galería")
                }

                // Botón tomar foto
                OutlinedButton(
                    onClick = {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Cámara")
                }
            }

            // Mostrar imagen seleccionada
            imageUri?.let { uri ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        AsyncImage(
                            model = uri,
                            contentDescription = "Foto seleccionada",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { imageUri = null }) {
                                Icon(Icons.Default.Delete, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Quitar")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de crear
            if (createReportState is CreateReportState.Loading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Button(
                    onClick = {
                        if (!locationObtained) {
                            Toast.makeText(
                                context,
                                "Por favor obtén tu ubicación primero",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        if (description.isBlank()) {
                            Toast.makeText(
                                context,
                                "Por favor ingresa una descripción",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        viewModel.createReport(
                            type = selectedType,
                            description = description,
                            latitude = currentLatitude,
                            longitude = currentLongitude,
                            imageUri = imageUri
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = description.isNotBlank() && locationObtained
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Crear Reporte")
                }
            }
        }
    }
}
