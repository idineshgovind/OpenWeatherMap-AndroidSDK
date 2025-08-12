package com.dineshdev.openweathermap.sdk.utils

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import timber.log.Timber
import java.lang.reflect.Type

/**
 * Simple cache manager using SharedPreferences for caching API responses.
 */
class CacheManager(context: Context, private val moshi: Moshi) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "openweather_cache",
        Context.MODE_PRIVATE
    )
    
    /**
     * Store data in cache with expiration time.
     */
    inline fun <reified T> put(key: String, data: T, expirationMinutes: Int) {
        if (expirationMinutes <= 0) return
        
        try {
            val adapter = moshi.adapter(T::class.java)
            val json = adapter.toJson(data)
            val expirationTime = System.currentTimeMillis() + (expirationMinutes * 60 * 1000)
            
            prefs.edit()
                .putString(key, json)
                .putLong("${key}_expiry", expirationTime)
                .apply()
            
            Timber.d("Cached data for key: $key")
        } catch (e: Exception) {
            Timber.e(e, "Failed to cache data for key: $key")
        }
    }
    
    /**
     * Retrieve data from cache if not expired.
     */
    inline fun <reified T> get(key: String): T? {
        try {
            val expirationTime = prefs.getLong("${key}_expiry", 0)
            
            if (expirationTime == 0L || System.currentTimeMillis() > expirationTime) {
                // Cache expired or doesn't exist
                clearEntry(key)
                return null
            }
            
            val json = prefs.getString(key, null) ?: return null
            val adapter = moshi.adapter(T::class.java)
            
            return adapter.fromJson(json).also {
                Timber.d("Retrieved cached data for key: $key")
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to retrieve cached data for key: $key")
            clearEntry(key)
            return null
        }
    }
    
    /**
     * Clear specific cache entry.
     */
    fun clearEntry(key: String) {
        prefs.edit()
            .remove(key)
            .remove("${key}_expiry")
            .apply()
    }
    
    /**
     * Clear all cache.
     */
    fun clearAll() {
        prefs.edit().clear().apply()
        Timber.d("Cleared all cache")
    }
    
    /**
     * Get cache size in bytes.
     */
    fun getCacheSize(): Long {
        var size = 0L
        prefs.all.forEach { (_, value) ->
            size += when (value) {
                is String -> value.toByteArray().size
                is Long -> 8
                else -> 0
            }
        }
        return size
    }
    
    /**
     * Clean expired cache entries.
     */
    fun cleanExpiredEntries() {
        val currentTime = System.currentTimeMillis()
        val editor = prefs.edit()
        val keysToRemove = mutableListOf<String>()
        
        prefs.all.forEach { (key, _) ->
            if (key.endsWith("_expiry")) {
                val expirationTime = prefs.getLong(key, 0)
                if (expirationTime > 0 && currentTime > expirationTime) {
                    val dataKey = key.removeSuffix("_expiry")
                    keysToRemove.add(dataKey)
                    keysToRemove.add(key)
                }
            }
        }
        
        keysToRemove.forEach { key ->
            editor.remove(key)
        }
        
        if (keysToRemove.isNotEmpty()) {
            editor.apply()
            Timber.d("Cleaned ${keysToRemove.size / 2} expired cache entries")
        }
    }
}