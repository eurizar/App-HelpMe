package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.utils.PreferencesManager
import com.example.myapplication.viewmodel.AuthState
import com.example.myapplication.viewmodel.AuthViewModel
import com.example.myapplication.viewmodel.ResetPasswordState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val prefsManager = remember { PreferencesManager(context) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }
    var rememberSession by remember { mutableStateOf(prefsManager.getRememberSession()) }
    var showResetPasswordDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()
    val resetPasswordState by viewModel.resetPasswordState.collectAsState()

    // Cargar el email guardado si existe
    LaunchedEffect(Unit) {
        prefsManager.getUserEmail()?.let { savedEmail ->
            email = savedEmail
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                onLoginSuccess()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    // Dialog para recuperación de contraseña
    if (showResetPasswordDialog) {
        AlertDialog(
            onDismissRequest = {
                showResetPasswordDialog = false
                viewModel.resetPasswordResetState()
            },
            title = { Text("Recuperar Contraseña") },
            text = {
                Column {
                    Text("Ingresa tu correo electrónico y te enviaremos un enlace para restablecer tu contraseña.")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = resetEmail,
                        onValueChange = { resetEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    when (resetPasswordState) {
                        is ResetPasswordState.Success -> {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Correo enviado exitosamente. Revisa tu bandeja de entrada.",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        is ResetPasswordState.Error -> {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                (resetPasswordState as ResetPasswordState.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        else -> {}
                    }
                }
            },
            confirmButton = {
                if (resetPasswordState is ResetPasswordState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    TextButton(
                        onClick = {
                            viewModel.resetPassword(resetEmail)
                        }
                    ) {
                        Text("Enviar")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showResetPasswordDialog = false
                        viewModel.resetPasswordResetState()
                        resetEmail = ""
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isRegistering) "Registro" else "Iniciar Sesión") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "HelpMe",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Text(
                text = "Red de Ayuda Ciudadana",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            // Checkbox de "Recordar sesión" (solo en modo login)
            if (!isRegistering) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Checkbox(
                            checked = rememberSession,
                            onCheckedChange = { rememberSession = it }
                        )
                        Text(
                            text = "Mantener sesión abierta",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    TextButton(
                        onClick = {
                            resetEmail = email
                            showResetPasswordDialog = true
                        }
                    ) {
                        Text("¿Olvidaste tu contraseña?")
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (authState is AuthState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        if (isRegistering) {
                            viewModel.signUp(email, password)
                        } else {
                            viewModel.signIn(email, password, rememberSession)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isRegistering) "Registrarse" else "Iniciar Sesión")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { isRegistering = !isRegistering }
                ) {
                    Text(
                        if (isRegistering) "¿Ya tienes cuenta? Inicia sesión"
                        else "¿No tienes cuenta? Regístrate"
                    )
                }
            }

            if (authState is AuthState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
