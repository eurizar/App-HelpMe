package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application.applicationContext)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _resetPasswordState = MutableStateFlow<ResetPasswordState>(ResetPasswordState.Idle)
    val resetPasswordState: StateFlow<ResetPasswordState> = _resetPasswordState

    val isUserLoggedIn: Boolean
        get() = repository.currentUser != null

    fun signIn(email: String, password: String, rememberSession: Boolean = false) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Por favor complete todos los campos")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.signIn(email, password, rememberSession)
                .onSuccess {
                    _authState.value = AuthState.Success
                }
                .onFailure { error ->
                    _authState.value = AuthState.Error(error.message ?: "Error al iniciar sesión")
                }
        }
    }

    fun signUp(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Por favor complete todos los campos")
            return
        }

        if (password.length < 6) {
            _authState.value = AuthState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.signUp(email, password)
                .onSuccess {
                    _authState.value = AuthState.Success
                }
                .onFailure { error ->
                    _authState.value = AuthState.Error(error.message ?: "Error al registrarse")
                }
        }
    }

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _resetPasswordState.value = ResetPasswordState.Error("Por favor ingrese su correo electrónico")
            return
        }

        viewModelScope.launch {
            _resetPasswordState.value = ResetPasswordState.Loading
            repository.resetPassword(email)
                .onSuccess {
                    _resetPasswordState.value = ResetPasswordState.Success
                }
                .onFailure { error ->
                    _resetPasswordState.value = ResetPasswordState.Error(
                        error.message ?: "Error al enviar correo de recuperación"
                    )
                }
        }
    }

    fun signOut() {
        repository.signOut()
        _authState.value = AuthState.Idle
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun resetPasswordResetState() {
        _resetPasswordState.value = ResetPasswordState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

sealed class ResetPasswordState {
    object Idle : ResetPasswordState()
    object Loading : ResetPasswordState()
    object Success : ResetPasswordState()
    data class Error(val message: String) : ResetPasswordState()
}
