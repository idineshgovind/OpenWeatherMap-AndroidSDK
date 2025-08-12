package com.dineshdev.openweathermap.sdk.utils

import com.dineshdev.openweathermap.sdk.data.models.RateLimitInfo
import kotlinx.coroutines.delay
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference

/**
 * Manages API rate limiting for the SDK.
 * Tracks rate limit status and provides throttling mechanisms.
 */
object RateLimitManager {
    
    private val currentRateLimitInfo = AtomicReference<RateLimitInfo?>(null)
    private val requestQueue = mutableListOf<Long>() // Timestamps of recent requests
    private val queueLock = Any()
    
    /**
     * Update the current rate limit information.
     */
    fun updateRateLimitInfo(info: RateLimitInfo) {
        currentRateLimitInfo.set(info)
        Timber.d("Rate limit updated: ${info.remaining}/${info.limit} remaining, resets at ${info.reset}")
    }
    
    /**
     * Get current rate limit information.
     */
    fun getCurrentRateLimitInfo(): RateLimitInfo? = currentRateLimitInfo.get()
    
    /**
     * Check if we should throttle requests based on rate limit.
     */
    fun shouldThrottle(): Boolean {
        val info = currentRateLimitInfo.get() ?: return false
        
        // If we're below 20% of limit remaining, start throttling
        val threshold = info.limit * 0.2
        return info.remaining < threshold
    }
    
    /**
     * Get delay in milliseconds before next request should be made.
     * Returns 0 if no delay needed.
     */
    fun getThrottleDelay(): Long {
        val info = currentRateLimitInfo.get() ?: return 0
        
        if (!shouldThrottle()) return 0
        
        // Calculate delay based on remaining calls and time until reset
        val secondsUntilReset = info.getSecondsUntilReset()
        if (secondsUntilReset <= 0 || info.remaining <= 0) {
            // Rate limit exceeded or about to reset
            return if (info.remaining <= 0) secondsUntilReset * 1000 else 0
        }
        
        // Distribute remaining calls evenly over the remaining time
        val delaySeconds = secondsUntilReset / info.remaining.coerceAtLeast(1)
        return (delaySeconds * 1000).coerceAtLeast(100) // At least 100ms between calls
    }
    
    /**
     * Apply rate limiting delay if necessary.
     */
    suspend fun applyRateLimit() {
        val delay = getThrottleDelay()
        if (delay > 0) {
            Timber.d("Applying rate limit delay: ${delay}ms")
            delay(delay)
        }
        
        // Track request timestamp
        synchronized(queueLock) {
            val now = System.currentTimeMillis()
            requestQueue.add(now)
            
            // Clean old entries (older than 1 minute)
            val cutoff = now - 60_000
            requestQueue.removeAll { it < cutoff }
        }
    }
    
    /**
     * Get requests per minute based on recent activity.
     */
    fun getRequestsPerMinute(): Int {
        synchronized(queueLock) {
            val now = System.currentTimeMillis()
            val cutoff = now - 60_000
            return requestQueue.count { it >= cutoff }
        }
    }
    
    /**
     * Check if rate limit is likely exceeded.
     */
    fun isRateLimitExceeded(): Boolean {
        val info = currentRateLimitInfo.get()
        return info?.isExceeded() == true
    }
    
    /**
     * Reset rate limit tracking.
     */
    fun reset() {
        currentRateLimitInfo.set(null)
        synchronized(queueLock) {
            requestQueue.clear()
        }
    }
    
    /**
     * Get a formatted string describing current rate limit status.
     */
    fun getStatusString(): String {
        val info = currentRateLimitInfo.get() ?: return "No rate limit info available"
        
        return buildString {
            append("Rate Limit: ${info.remaining}/${info.limit} calls remaining")
            if (info.isExceeded()) {
                append(" [EXCEEDED]")
            }
            val secondsUntilReset = info.getSecondsUntilReset()
            if (secondsUntilReset > 0) {
                append(", resets in ${secondsUntilReset}s")
            }
        }
    }
}