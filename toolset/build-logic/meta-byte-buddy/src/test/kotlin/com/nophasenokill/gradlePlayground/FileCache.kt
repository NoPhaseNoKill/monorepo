package com.nophasenokill.gradlePlayground


import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import java.time.Duration
import java.time.temporal.TemporalAmount
import java.util.concurrent.TimeUnit
import java.util.function.Function

/*
    This replaces gradle's CacheAccessSerializer.java file.

    It makes two clear improvements:
        1. This code awaits the result of a pending load rather than starting a redundant one.
            This means that it will wait for another thread that is also loading/calculating the value.

        2. By doing step 1, eliminates a whole class of bugs or issues with cache invalidation.
            These have large flow on effects when invalidating gradle's cache, particularly
            because when these are stored, any invalidation 'explodes' to all other files
            that the code being invalidated links to.

    Important notes:
        - K and V must extend 'Any'. Normally you would consider them equal (without extending Any),
         however, to nullness checkers these are different.

         <K: Any, V: Any> -> means "non-null types," (aka non-nullable)
         <K, V>           -> means "all types." (aka nullable)
 */

data class OptimizedCacheAccessSerializer<K: Any, V: Any>(
    val cache: Cache<K, V> = CacheBuilder
        .newBuilder()
        .maximumSize(1000)                               // Evict based on size when the cache reaches 1000 entries
        .expireAfterWrite(10, TimeUnit.MINUTES)  // Expire entries 10 minutes after creation
        .build()
) {
        /*
            Taken from the notes:
                "
                    Note that the cache may evict an entry before this limit is exceeded.
                    For example, in the current implementation, when concurrencyLevel is greater
                    than 1, each resulting segment inside the cache independently limits its own
                    size to approximately maximumSize / concurrencyLevel.
                "
         */

    fun get(key: K, factory: Function<in K, out V>): V {
        try {
            // Use Guava's atomic get operation to ensure thread safety.
            return cache.get(key, { factory.apply(key) })
        } catch (e: Exception) {
            throw RuntimeException("Cache loading failed", e)
        }
    }

    fun getIfPresent(key: K): V? {
        // Guava provides a thread-safe way to retrieve an existing entry without computing a new value.
        return cache.getIfPresent(key)
    }

    // Directly use the put method provided by Guava, which handles concurrency under the hood.
    fun put(key: K, value: V) {
        cache.put(key, value)
    }
}

