package com.wk.imageloaderlibrary

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.wk.imageloaderlibrary.cache.ImageCache
import com.wk.imageloaderlibrary.cache.MemoryCache
import com.wk.imageloaderlibrary.util.CheckUtil
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageLoader {

    companion object {
        var mImageLoader: ImageLoader? = null
        fun getInastance(): ImageLoader {
            if (mImageLoader == null) {
                synchronized(ImageLoader::class) {
                    if (mImageLoader == null)
                        mImageLoader = ImageLoader()
                }
            }
            return mImageLoader!!
        }
    }

    // 线程池
    private lateinit var mExecutorService: ExecutorService

    private lateinit var config: ImageLoaderConfig

    private constructor()

    fun initConfig(config: ImageLoaderConfig) {
        this.config = config
        mExecutorService = Executors.newFixedThreadPool(config.threadCount)
    }

    fun displayImage(imgUrl: String?, imageView: ImageView) {
        val bitmap = config.mCache.getCache(imgUrl)
        // 有缓存，从缓存取
        if (CheckUtil.checkOb(bitmap)) {
            imageView.setImageBitmap(bitmap)
            return
        }
        // 无缓存，提交到线程池下载图片
        submitLoadRequest(imgUrl, imageView)
    }

    private fun submitLoadRequest(imgUrl: String?, imageView: ImageView) {
        imageView.tag = imgUrl
        mExecutorService.submit(object : Runnable {
            override fun run() {
                val bitmap = downloadImg(imgUrl)
                //加载成功后的图片
                if (CheckUtil.checkOb(bitmap) && imageView.tag == imgUrl) {
                    imageView.setImageBitmap(bitmap)
                    config.mCache.putCache(imgUrl, bitmap)
                } else { // 加载失败图片

                }
            }

        })
    }

    /**
     *  网络下载图片
     */
    private fun downloadImg(imgUrl: String?): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val url = URL(imgUrl)
            val connection = url.openConnection()
            bitmap = BitmapFactory.decodeStream(connection.inputStream)
            if (connection is HttpURLConnection)
                connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }
}