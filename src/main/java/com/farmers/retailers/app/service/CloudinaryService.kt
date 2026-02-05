package com.farmers.retailers.app.service

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import java.util.HashMap

class CloudinaryService(context: Context) {
    
    init {
        initializeCloudinary(context)
    }
    
    private fun initializeCloudinary(context: Context) {
        try {
            // Check if MediaManager is already initialized
            try {
                MediaManager.get()
                // If we get here, it's already initialized
            } catch (e: Exception) {
                // Not initialized, so initialize it
                val config = HashMap<String, String>()
                config["cloud_name"] = "dpd0pjq49"
                config["api_key"] = "489273422249657"
                config["api_secret"] = "Cr9ca9EfZt2jxmDm_MVS5NlZ718"
                MediaManager.init(context, config)
            }
        } catch (e: Exception) {
            // MediaManager might already be initialized, which is fine
        }
    }
    
    fun uploadImage(imageUri: Uri, callback: (Result<String>) -> Unit) {
        try {
            MediaManager.get()
                .upload(imageUri)
                .option("resource_type", "image")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String?) {
                        // Upload started
                    }
                    
                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                        // Progress update
                    }
                    
                    override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                        val url = resultData?.get("secure_url")?.toString() ?: ""
                        callback(Result.success(url))
                    }
                    
                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        callback(Result.failure(Exception(error?.description ?: "Upload failed")))
                    }
                    
                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                        // Reschedule if needed
                    }
                })
                .dispatch()
        } catch (e: Exception) {
            callback(Result.failure(Exception("Cloudinary not properly initialized: ${e.message}")))
        }
    }
}