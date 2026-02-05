package com.farmers.retailers.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.farmers.retailers.app.ui.screens.CommunityScreen
import com.farmers.retailers.app.ui.screens.CropDetailsScreen
import com.farmers.retailers.app.ui.screens.FarmerContactScreen
import com.farmers.retailers.app.ui.screens.GalleryScreen
import com.farmers.retailers.app.ui.screens.HomeScreen
import com.farmers.retailers.app.ui.screens.LoginScreen
import com.farmers.retailers.app.ui.screens.ProfileScreen
import com.farmers.retailers.app.ui.screens.SignupScreen
import com.farmers.retailers.app.ui.screens.SplashScreen
import com.farmers.retailers.app.ui.screens.UserDetailsScreen

@Composable
fun MainApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("farmer_home") { HomeScreen(navController, isFarmer = true) }
        composable("retailer_home") { HomeScreen(navController, isFarmer = false) }
        composable(
            "crop_details/{cropId}",
            arguments = listOf(navArgument("cropId") { type = NavType.StringType })
        ) { backStackEntry ->
            val cropId = backStackEntry.arguments?.getString("cropId")
            CropDetailsScreen(navController, cropId)
        }
        // Fallback for crop details without ID
        composable("crop_details") { 
            CropDetailsScreen(navController, null) 
        }
        composable(
            "profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            ProfileScreen(navController, userId)
        }
        // Fallback for profile without user ID
        composable("profile") { 
            ProfileScreen(navController, null) 
        }
        composable("gallery") { 
            GalleryScreen(navController) 
        }
        composable("community") { 
            CommunityScreen(navController) 
        }
        composable(
            "user_details/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            UserDetailsScreen(navController, userId)
        }
        composable(
            "farmer_contact/{farmerId}",
            arguments = listOf(navArgument("farmerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val farmerId = backStackEntry.arguments?.getString("farmerId")
            if (farmerId != null) {
                FarmerContactScreen(navController, farmerId)
            }
        }
    }
}