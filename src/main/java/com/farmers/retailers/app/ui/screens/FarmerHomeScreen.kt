package com.farmers.retailers.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun FarmerHomeScreen(navController: NavController) {
    HomeScreen(navController, isFarmer = true)
}
