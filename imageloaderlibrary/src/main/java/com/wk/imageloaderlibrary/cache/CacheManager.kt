package com.wk.imageloaderlibrary.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import com.wk.imageloaderlibrary.util.CheckUtil
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

/**
 *Time: 2019-10-19
 *Author: wukai
 *Description:
 */
class CacheManager(context: Context) {

    companion object {
        const val ALL_CACHE = 0
        const val ONLY_MEMORY_CACHE = 1
        const val ONLY_DISK_CACHE = 2
    }

    var mMemoryCache: MemoryCache = MemoryCache()
    var mDiskCache: DiskCache = DiskCache(context)

    var cacheMode: Int = 0

    fun getFromCacheBitmap(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        when (cacheMode) {
            ALL_CACHE -> {
                bitmap = getFromMemory(url)
                if (bitmap == null) {
                    bitmap = getFromDisk(url)
                    if (bitmap != null) {
                        Log.e("CacheManager", "From Disk")
                        mMemoryCache.putCache(url, bitmap)
                    }
                } else{
                    Log.e("CacheManager", "From Memory")
                }
            }
            ONLY_MEMORY_CACHE -> {
                bitmap = getFromMemory(url)
            }
            ONLY_DISK_CACHE -> {
                bitmap = getFromDisk(url)
            }
        }

        return bitmap
    }

    private fun getFromMemory(url: String): Bitmap? {
        return mMemoryCache.getCache(url)
    }

    private fun getFromDisk(url: String): Bitmap? {
        return mDiskCache.getCache(url)
    }

    fun getFromNetwork(url: String): Bitmap? {
        Log.e("CacheManager", "From Network")
        val bitmap = downloadImg(url)
        when (cacheMode) {
            ALL_CACHE -> {
                mMemoryCache.putCache(url, bitmap!!)
                mDiskCache.putCache(url, bitmap)
            }
            ONLY_MEMORY_CACHE -> {
                mMemoryCache.putCache(url, bitmap!!)
            }
            ONLY_DISK_CACHE -> {
                mDiskCache.putCache(url, bitmap!!)
            }
        }
        return bitmap
    }

    /**
     *  网络下载图片
     */
    private fun downloadImg(imgUrl: String?): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val url = URL(imgUrl)
            val connection = url.openConnection() as HttpURLConnection
            bitmap = BitmapFactory.decodeStream(connection.inputStream)
            if (connection is HttpURLConnection)
                connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

}