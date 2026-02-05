package com.farmers.retailers.app.data

data class Crop(
    val id: String = "",
    val farmerId: String = "",
    val farmerName: String = "",
    val state: String = "",
    val district: String = "",
    val taluk: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val predictedPrices: Map<String, Double> = emptyMap() // Monthly predictions: month -> predicted price
)