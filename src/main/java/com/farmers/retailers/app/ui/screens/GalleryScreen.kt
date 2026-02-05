@file:OptIn(ExperimentalMaterial3Api::class)

package com.farmers.retailers.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
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
import kotlinx.coroutines.launch

@Composable
fun GalleryScreen(navController: NavController) {
    val firebaseService = FirebaseService()
    val coroutineScope = rememberCoroutineScope()
    var crops by remember { mutableStateOf<List<Crop>>(emptyList()) }

    // Load all crops
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val result = firebaseService.getAllCrops()
            if (result.isSuccess) {
                crops = result.getOrNull() ?: emptyList()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crop Gallery") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (crops.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No crops available in gallery",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(crops) { crop ->
                    GalleryCropItem(
                        crop = crop,
                        onClick = { navController.navigate("crop_details/${crop.id}") }
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GalleryCropItem(crop: Crop, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Display crop image if available
            if (crop.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = crop.imageUrl,
                    contentDescription = crop.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = "No image",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            
            Text(
                text = crop.name,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                maxLines = 1
            )
        }
    }
}