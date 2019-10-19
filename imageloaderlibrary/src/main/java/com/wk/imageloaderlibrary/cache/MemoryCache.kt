package com.wk.imageloaderlibrary.cache

import android.graphics.Bitmap
import androidx.collection.LruCache
import com.wk.imageloaderlibrary.util.CheckUtil

class MemoryCache : ImageCache {

    lateinit var lruCache: LruCache<String, Bitmap>

   init {
        initCache()
    }

    private fun initCache() {
        val maxMemory = Runtime.getRuntime().maxMemory() / 1024
        val cacheSize = maxMemory / 8
        lruCache = object : LruCache<String, Bitmap>(cacheSize.toInt()) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                return value.rowBytes * value.height / 1024
            }
        }
    }

    override fun getCache(url: String): Bitmap? {
        return lruCache.get(url)
    }

    override fun putCache(url: String, bitmap: Bitmap) {
        lruCache.put(url, bitmap)
    }
}