package com.farmers.retailers.app.service

import com.farmers.retailers.app.data.Crop
import java.text.SimpleDateFormat
import java.util.*

class PredictionService {
    companion object {
        /**
         * Generates monthly price predictions for the next 6 months
         * Uses a custom hybrid algorithm named 'Seasonal Trend-Based Price Prediction Algorithm'
         * that combines linear trend analysis, seasonal adjustment factors by crop type,
         * inflation modeling, and regional market trends for monthly agricultural commodity
         * price variation forecasting.
         */
        fun generatePricePredictions(crop: Crop, allCrops: List<Crop>): Map<String, Double> {
            val predictions = mutableMapOf<String, Double>()
            
            // Get historical prices for this crop type in the same region
            val historicalPrices = getHistoricalPrices(crop, allCrops)
            
            // Calculate trend based on historical data
            val trend = calculateTrend(historicalPrices, crop.price)
            
            // Generate predictions for next 6 months
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
            
            for (i in 1..6) {
                calendar.add(Calendar.MONTH, 1)
                val monthKey = dateFormat.format(calendar.time)
                
                // Apply trend and seasonal adjustments
                val seasonalFactor = getSeasonalFactor(crop.name, calendar.get(Calendar.MONTH) + 1)
                val inflationFactor = 1.0 + (0.02 * i) // 2% inflation per month
                
                val predictedPrice = crop.price * (1 + trend * i / 100.0) * seasonalFactor * inflationFactor
                predictions[monthKey] = predictedPrice.coerceAtLeast(0.0) // Ensure non-negative prices
            }
            
            return predictions
        }
        
        /**
         * Gets historical prices for similar crops in the same region
         */
        private fun getHistoricalPrices(currentCrop: Crop, allCrops: List<Crop>): List<Double> {
            return allCrops.filter { 
                it.name.equals(currentCrop.name, ignoreCase = true) && 
                it.district.equals(currentCrop.district, ignoreCase = true) &&
                it.state.equals(currentCrop.state, ignoreCase = true) &&
                it.id != currentCrop.id // Exclude current crop
            }.map { it.price }
        }
        
        /**
         * Calculates price trend based on historical data
         */
        private fun calculateTrend(historicalPrices: List<Double>, currentPrice: Double): Double {
            if (historicalPrices.isEmpty()) {
                // If no historical data, use a small random trend (-5% to +5%)
                return (Math.random() * 10 - 5)
            }
            
            val avgHistoricalPrice = historicalPrices.average()
            return ((currentPrice - avgHistoricalPrice) / avgHistoricalPrice) * 100
        }
        
        /**
         * Gets seasonal factor based on crop type and month
         * This is a simplified model - in reality, this would be much more complex
         */
        private fun getSeasonalFactor(cropName: String, month: Int): Double {
            val cropType = cropName.lowercase()
            
            return when {
                // Wheat typically harvested in spring/summer
                cropType.contains("wheat") -> {
                    when (month) {
                        in 3..6 -> 1.1 // Harvest season - higher prices
                        in 7..9 -> 0.9 // Post-harvest - lower prices
                        else -> 1.0
                    }
                }
                // Rice typically harvested in fall
                cropType.contains("rice") -> {
                    when (month) {
                        in 9..11 -> 1.1 // Harvest season - higher prices
                        in 12..2 -> 0.9 // Post-harvest - lower prices
                        else -> 1.0
                    }
                }
                // Corn typically harvested in late summer/fall
                cropType.contains("corn") || cropType.contains("maize") -> {
                    when (month) {
                        in 8..10 -> 1.1 // Harvest season - higher prices
                        in 11..1 -> 0.9 // Post-harvest - lower prices
                        else -> 1.0
                    }
                }
                // Default case - minor seasonal variations
                else -> {
                    when (month) {
                        3, 4, 9, 10 -> 1.05 // Slight increase during typical harvest months
                        6, 7, 12, 1 -> 0.95 // Slight decrease during off-season
                        else -> 1.0
                    }
                }
            }
        }
        
        /**
         * Calculates the percentage variation from current price to predicted price
         */
        fun getPriceVariationPercentage(currentPrice: Double, predictedPrice: Double): Double {
            if (currentPrice <= 0) return 0.0
            return ((predictedPrice - currentPrice) / currentPrice) * 100
        }
    }
}