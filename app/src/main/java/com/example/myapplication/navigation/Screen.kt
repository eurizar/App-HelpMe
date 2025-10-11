package com.example.myapplication.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object CreateReport : Screen("create_report")
    object MapView : Screen("map_view")
    object ReportList : Screen("report_list")
    object Metrics : Screen("metrics")
}

