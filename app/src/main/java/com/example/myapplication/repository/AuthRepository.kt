package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.utils.PreferencesManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepository(context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private val prefsManager = PreferencesManager(context)

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    fun shouldRememberSession(): Boolean {
        return prefsManager.getRememberSession() && currentUser != null
    }

    suspend fun signIn(email: String, password: String, rememberSession: Boolean): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()

            // Guardar preferencia de sesión
            prefsManager.setRememberSession(rememberSession)
            if (rememberSession) {
                prefsManager.setUserEmail(email)
            }

            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
        prefsManager.clearAllPreferences()
    }
}
