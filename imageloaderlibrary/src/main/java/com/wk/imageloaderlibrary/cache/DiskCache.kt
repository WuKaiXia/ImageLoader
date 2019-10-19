package com.wk.imageloaderlibrary.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.jakewharton.disklrucache.DiskLruCache
import com.wk.imageloaderlibrary.util.CheckUtil
import com.wk.imageloaderlibrary.util.CloseableUtil
import com.wk.imageloaderlibrary.util.DiskLruCacheUtil
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class DiskCache(var mContext: Context) : ImageCache {
    companion object {
        const val CACHE_PATH = "sdcard/cache/"
        const val DISK_CACHE_SIZE = 1024 * 1024 * 50L
    }

    var mDiskCache: DiskLruCache? = null


    init {
        initDiskCache()
    }

    override fun getCache(url: String): Bitmap? {
        return getDataFromDisk(url)
    }

    private fun getDataFromDisk(url: String): Bitmap? {
        var fileDescriptor: FileDescriptor? = null
        var fileInputStream: FileInputStream? = null
        var snapshot: DiskLruCache.Snapshot? = null
        try {
            val md5Str = DiskLruCacheUtil.getMd5Str(url)
            snapshot = mDiskCache?.get(md5Str)
            if (snapshot != null) {
                fileInputStream = snapshot.getInputStream(0) as FileInputStream
                fileDescriptor = fileInputStream.fd
            }

            var bitmap: Bitmap? = null
            if (CheckUtil.checkOb(fileDescriptor)) {
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            }
            return bitmap

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            CloseableUtil.closeQuietly(fileInputStream)
        }
        return null
    }


    override fun putCache(url: String, bitmap: Bitmap) {
        var fileOutputStream: FileOutputStream? = null
        try {
            val md5Str = DiskLruCacheUtil.getMd5Str(url)
            val edit = mDiskCache!!.edit(md5Str)
            if (CheckUtil.checkOb(edit)) {
                val outputStream = edit.newOutputStream(0)

                val ifSuccess = download(url, outputStream)
                if (ifSuccess) {
                    edit.commit()
                } else {
                    edit.abort()
                }
                mDiskCache?.flush()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            CloseableUtil.closeQuietly(fileOutputStream)
        }
    }


    /**
     * 缓存图片到disk
     */
    private fun download(urlStr: String, outputStream: OutputStream): Boolean {
        var result = false
        val urlConnection: HttpURLConnection
        var output: BufferedOutputStream? = null
        var input: BufferedInputStream? = null

        try {
            val url = URL(urlStr)
            urlConnection = url.openConnection() as HttpURLConnection
            input = BufferedInputStream(urlConnection.inputStream, 8 * 1024)
            output = BufferedOutputStream(outputStream, 8 * 1024)
            var b: Int = input.read()
            while (b != -1) {
                output.write(b)
                b = input.read()
            }
            urlConnection.disconnect()
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                CloseableUtil.closeQuietly(output)
                CloseableUtil.closeQuietly(input)
            } catch (e: IOException) {
                result = false
                e.printStackTrace()
            }
            return result
        }
    }

    /**
     * 初始化 disklrucache
     */
    private fun initDiskCache() {
        val cacheDir = DiskLruCacheUtil.getDiskCacheDir(mContext, CACHE_PATH)
        if (!cacheDir.exists()) {
            cacheDir.mkdir()
        }
        val appVersion = DiskLruCacheUtil.getAppVersionCode(this.mContext)
        mDiskCache = DiskLruCache.open(cacheDir, appVersion, 1, DISK_CACHE_SIZE)
    }

}