package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.screens.*
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.AuthViewModel
import com.example.myapplication.viewmodel.ReportViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                HelpMeApp()
            }
        }
    }
}

@Composable
fun HelpMeApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val reportViewModel: ReportViewModel = viewModel()

    val startDestination = if (authViewModel.isUserLoggedIn) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }


    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                viewModel = authViewModel
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCreateReport = {
                    navController.navigate(Screen.CreateReport.route)
                },
                onNavigateToMap = {
                    navController.navigate(Screen.MapView.route)
                },
                onNavigateToList = {
                    navController.navigate(Screen.ReportList.route)
                },
                onNavigateToMetrics = {
                    navController.navigate(Screen.Metrics.route)
                },
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.CreateReport.route) {
            CreateReportScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = reportViewModel
            )
        }

        composable(Screen.MapView.route) {
            MapViewScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = reportViewModel
            )
        }

        composable(Screen.ReportList.route) {
            ReportListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = reportViewModel
            )
        }

        composable(Screen.Metrics.route) {
            MetricsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = reportViewModel
            )
        }
    }
}