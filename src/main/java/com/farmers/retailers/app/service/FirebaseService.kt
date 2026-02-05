package com.farmers.retailers.app.service

import android.net.Uri
import android.util.Log
import com.farmers.retailers.app.data.Crop
import com.farmers.retailers.app.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FirebaseService {
    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "FirebaseService"

    suspend fun signUp(email: String, password: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            Log.e(TAG, "signUp failed", e)
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            Log.e(TAG, "login failed", e)
            Result.failure(e)
        }
    }

    suspend fun saveUser(user: User): Result<Unit> {
        return try {
            Log.d(TAG, "Saving user: ${user.id}")
            db.collection("users").document(user.id).set(user).await()
            Log.d(TAG, "User saved successfully: ${user.id}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "saveUser failed", e)
            Result.failure(e)
        }
    }

    suspend fun getUser(userId: String): Result<User> {
        return try {
            val document = db.collection("users").document(userId).get().await()
            if (document.exists()) {
                Result.success(document.toObject<User>() ?: User())
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getUser failed", e)
            Result.failure(e)
        }
    }

    suspend fun getAllUsers(): Result<List<User>> {
        return try {
            Log.d(TAG, "Fetching all users")
            val snapshot = db.collection("users").get().await()
            val users = snapshot.documents.mapNotNull { it.toObject<User>() }
            Log.d(TAG, "Fetched ${users.size} users")
            Result.success(users)
        } catch (e: Exception) {
            Log.e(TAG, "getAllUsers failed", e)
            Result.failure(e)
        }
    }

    suspend fun saveCrop(crop: Crop): Result<Unit> {
        return try {
            Log.d(TAG, "Saving crop: ${crop.id}")
            db.collection("crops").document(crop.id).set(crop).await()
            Log.d(TAG, "Crop saved successfully: ${crop.id}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "saveCrop failed for crop: ${crop.id}", e)
            Result.failure(e)
        }
    }

    suspend fun deleteCrop(cropId: String): Result<Unit> {
        return try {
            Log.d(TAG, "Deleting crop: $cropId")
            db.collection("crops").document(cropId).delete().await()
            Log.d(TAG, "Crop deleted successfully: $cropId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "deleteCrop failed for crop: $cropId", e)
            Result.failure(e)
        }
    }

    suspend fun getAllCrops(): Result<List<Crop>> {
        return try {
            Log.d(TAG, "Fetching all crops")
            val snapshot = db.collection("crops").get().await()
            val crops = snapshot.documents.mapNotNull { 
                try {
                    val crop = it.toObject<Crop>()
                    crop
                } catch (e: Exception) {
                    Log.e(TAG, "Error converting document to Crop", e)
                    null
                }
            }
            Log.d(TAG, "Fetched ${crops.size} crops")
            Result.success(crops)
        } catch (e: Exception) {
            Log.e(TAG, "getAllCrops failed", e)
            Result.failure(e)
        }
    }

    suspend fun getAllCropsWithPredictions(): Result<List<Crop>> {
        return try {
            Log.d(TAG, "Fetching all crops with predictions")
            val snapshot = db.collection("crops").get().await()
            val crops = snapshot.documents.mapNotNull { 
                try {
                    val crop = it.toObject<Crop>()
                    crop
                } catch (e: Exception) {
                    Log.e(TAG, "Error converting document to Crop", e)
                    null
                }
            }
            
            // Generate predictions for each crop
            val cropsWithPredictions = crops.map { crop ->
                val predictions = PredictionService.generatePricePredictions(crop, crops)
                crop.copy(predictedPrices = predictions)
            }
            
            Log.d(TAG, "Fetched ${cropsWithPredictions.size} crops with predictions")
            Result.success(cropsWithPredictions)
        } catch (e: Exception) {
            Log.e(TAG, "getAllCropsWithPredictions failed", e)
            Result.failure(e)
        }
    }

    suspend fun getCropsByRegion(region: String): Result<List<Crop>> {
        return try {
            Log.d(TAG, "Fetching crops by region: $region")
            val snapshot = db.collection("crops")
                .whereEqualTo("region", region)
                .get().await()
            val crops = snapshot.documents.mapNotNull { 
                try {
                    val crop = it.toObject<Crop>()
                    crop
                } catch (e: Exception) {
                    Log.e(TAG, "Error converting document to Crop", e)
                    null
                }
            }
            Log.d(TAG, "Fetched ${crops.size} crops for region: $region")
            Result.success(crops)
        } catch (e: Exception) {
            Log.e(TAG, "getCropsByRegion failed", e)
            Result.failure(e)
        }
    }

    suspend fun getCropsByFarmer(farmerId: String): Result<List<Crop>> {
        return try {
            Log.d(TAG, "Fetching crops by farmer: $farmerId")
            val snapshot = db.collection("crops")
                .whereEqualTo("farmerId", farmerId)
                .get().await()
            val crops = snapshot.documents.mapNotNull { 
                try {
                    val crop = it.toObject<Crop>()
                    crop
                } catch (e: Exception) {
                    Log.e(TAG, "Error converting document to Crop", e)
                    null
                }
            }
            Log.d(TAG, "Fetched ${crops.size} crops for farmer: $farmerId")
            Result.success(crops)
        } catch (e: Exception) {
            Log.e(TAG, "getCropsByFarmer failed", e)
            Result.failure(e)
        }
    }

    suspend fun getCropById(cropId: String): Result<Crop?> {
        return try {
            Log.d(TAG, "Fetching crop by ID: $cropId")
            val document = db.collection("crops").document(cropId).get().await()
            if (document.exists()) {
                val crop = document.toObject<Crop>()
                Log.d(TAG, "Crop found: $cropId")
                Result.success(crop)
            } else {
                Log.d(TAG, "Crop not found: $cropId")
                Result.success(null)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getCropById failed", e)
            Result.failure(e)
        }
    }

    suspend fun getCropByIdWithPredictions(cropId: String): Result<Crop?> {
        return try {
            Log.d(TAG, "Fetching crop by ID with predictions: $cropId")
            val document = db.collection("crops").document(cropId).get().await()
            if (document.exists()) {
                val crop = document.toObject<Crop>()
                if (crop != null) {
                    val allCropsSnapshot = db.collection("crops").get().await()
                    val allCrops = allCropsSnapshot.documents.mapNotNull { 
                        try {
                            val c = it.toObject<Crop>()
                            c
                        } catch (e: Exception) {
                            Log.e(TAG, "Error converting document to Crop", e)
                            null
                        }
                    }
                    val predictions = PredictionService.generatePricePredictions(crop, allCrops)
                    Log.d(TAG, "Crop with predictions found: $cropId")
                    return Result.success(crop.copy(predictedPrices = predictions))
                }
            }
            Log.d(TAG, "Crop not found: $cropId")
            Result.success(null)
        } catch (e: Exception) {
            Log.e(TAG, "getCropByIdWithPredictions failed", e)
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }
}