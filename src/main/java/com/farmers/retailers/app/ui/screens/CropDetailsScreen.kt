package com.farmers.retailers.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.farmers.retailers.app.data.Crop
import com.farmers.retailers.app.service.FirebaseService
import com.farmers.retailers.app.service.PredictionService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropDetailsScreen(navController: NavController, cropId: String?) {
    var crop by remember { mutableStateOf<Crop?>(null) }
    val firebaseService = FirebaseService()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Load crop details from Firebase
    LaunchedEffect(cropId) {
        if (cropId != null) {
            coroutineScope.launch {
                val result = firebaseService.getCropByIdWithPredictions(cropId)
                if (result.isSuccess) {
                    crop = result.getOrNull()
                }
            }
        } else {
            // Fallback to sample data if no ID is provided
            val sampleCrop = Crop(
                id = "1",
                farmerId = "farmer123",
                farmerName = "John Doe",
                state = "Punjab",
                district = "Amritsar",
                taluk = "Ajnala",
                name = "Organic Wheat",
                imageUrl = "https://res.cloudinary.com/dpd0pjq49/image/upload/v1/sample.jpg",
                price = 2500.0,
                description = "High-quality organic wheat grown without pesticides. Harvested fresh last week. Perfect for making healthy bread and other baked goods."
            )
            
            // Generate sample predictions
            val allCrops = listOf(sampleCrop)
            val predictions = PredictionService.generatePricePredictions(sampleCrop, allCrops)
            crop = sampleCrop.copy(predictedPrices = predictions)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crop Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (crop == null) {
            // Loading state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val currentCrop = crop!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(scrollState), // Make the content scrollable
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display crop image from Cloudinary
                if (currentCrop.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = currentCrop.imageUrl,
                        contentDescription = currentCrop.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder for crop image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxSize(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No Image Available", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Crop information
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = currentCrop.name,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Location: ${currentCrop.taluk}, ${currentCrop.district}, ${currentCrop.state}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "Current Price: ₹${currentCrop.price} per quintal",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Description:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = currentCrop.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Price Predictions Section
                if (currentCrop.predictedPrices.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Price Predictions",
                                style = MaterialTheme.typography.titleMedium
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Expected monthly price variations:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            currentCrop.predictedPrices.toList().take(6).forEach { (month, predictedPrice) ->
                                val variation = PredictionService.getPriceVariationPercentage(
                                    currentCrop.price, predictedPrice
                                )
                                
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = month,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    
                                    Text(
                                        text = "₹${String.format("%.0f", predictedPrice)}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    
                                    Text(
                                        text = if (variation >= 0) "+${String.format("%.1f", variation)}%" else "${String.format("%.1f", variation)}%",
                                        color = if (variation >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Farmer information
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Farmer Information",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Name: ${currentCrop.farmerName}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "Location: ${currentCrop.taluk}, ${currentCrop.district}, ${currentCrop.state}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Contact Farmer Button with improved visibility
                Button(
                    onClick = { navController.navigate("farmer_contact/${currentCrop.farmerId}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), // Increased height for better visibility
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Contact Farmer",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}