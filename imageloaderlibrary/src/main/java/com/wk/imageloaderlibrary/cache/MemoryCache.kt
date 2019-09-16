package com.wk.imageloaderlibrary.cache

import android.graphics.Bitmap
import androidx.collection.LruCache
import com.wk.imageloaderlibrary.util.CheckUtil

class MemoryCache : ImageCache {

    lateinit var lruCache: LruCache<String, Bitmap>

    constructor() {
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

    override fun getCache(url: String?): Bitmap? {
        var bitmap: Bitmap? = null
        if (CheckUtil.checkOb(url)) {
            bitmap =  lruCache.get(url!!)
        }
        return bitmap
    }

    override fun putCache(url: String?, bitmap: Bitmap?) {
        if (CheckUtil.checkOb(url) && CheckUtil.checkOb(bitmap)) {
            lruCache.put(url!!, bitmap!!)
        }
    }
}