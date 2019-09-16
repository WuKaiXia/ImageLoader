package com.wk.imageloaderlibrary.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.jakewharton.disklrucache.DiskLruCache
import com.wk.imageloaderlibrary.util.CloseableUtil
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class DiskCache : ImageCache {
    companion object {
        const val CACHE_PATH = "sdcard/cache/"
        const val DISK_CACHE_SIZE = 1024 * 1024 * 50
    }

    var diskCache: DiskLruCache? = null

    constructor() {
//        diskCache = DiskLruCache.open(1,1,DISK_CACHE_SIZE)
    }

    override fun getCache(url: String?): Bitmap? {
        return BitmapFactory.decodeFile(CACHE_PATH + url)
    }

    override fun putCache(url: String?, bitmap: Bitmap?) {
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(CACHE_PATH + url)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            CloseableUtil.closeQuietly(fileOutputStream)
        }
    }

}