package com.example.myapplication.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("HelpMePrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_REMEMBER_SESSION = "remember_session"
        private const val KEY_USER_EMAIL = "user_email"
    }

    fun setRememberSession(remember: Boolean) {
        prefs.edit().putBoolean(KEY_REMEMBER_SESSION, remember).apply()
    }

    fun getRememberSession(): Boolean {
        return prefs.getBoolean(KEY_REMEMBER_SESSION, false)
    }

    fun setUserEmail(email: String) {
        prefs.edit().putString(KEY_USER_EMAIL, email).apply()
    }

    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    // Solo limpia las preferencias cuando el usuario cierra sesión explícitamente
    fun clearAllPreferences() {
        prefs.edit()
            .remove(KEY_REMEMBER_SESSION)
            .remove(KEY_USER_EMAIL)
            .apply()
    }
}
