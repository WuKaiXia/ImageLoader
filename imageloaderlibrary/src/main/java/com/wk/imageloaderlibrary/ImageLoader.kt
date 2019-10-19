package com.wk.imageloaderlibrary

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Message
import android.widget.ImageView
import com.wk.imageloaderlibrary.cache.CacheManager
import com.wk.imageloaderlibrary.util.CheckUtil
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageLoader private constructor(context: Context) {

    companion object {
        var mImageLoader: ImageLoader? = null

        fun with(context: Context): ImageLoader {
            if (mImageLoader == null) {
                synchronized(ImageLoader::class) {
                    if (mImageLoader == null)
                        mImageLoader = ImageLoader(context)
                }
            }
            return mImageLoader!!
        }
    }

    @SuppressLint("HandlerLeak")
    private val mUiHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.obj is Bitmap) {
                mImageView.setImageBitmap(msg.obj as Bitmap)
            } else if (msg.obj is Int) {
                mImageView.setImageResource(msg.obj as Int)
            }

        }
    }

    // 线程池
    private lateinit var mExecutorService: ExecutorService

    private var mCacheManager: CacheManager = CacheManager(context)

    private lateinit var config: ImageLoaderConfig
    private var mImageView: ImageView = ImageView(context)

    private var mContext: Context = context
    fun initConfig(config: ImageLoaderConfig): ImageLoader {
        this.config = config
        mCacheManager.cacheMode = config.mCacheMode
        mExecutorService = Executors.newFixedThreadPool(config.threadCount)
        return this
    }

    fun displayImage(imgUrl: String?, imageView: ImageView?) {
        if (!CheckUtil.checkOb(imageView) || !CheckUtil.checkOb(imgUrl)) {
            throw Throwable("The params cannot empty!")
        }
        mImageView = imageView!!
        val bitmap = mCacheManager.getFromCacheBitmap(imgUrl!!)
        // 有缓存，从缓存取
        if (CheckUtil.checkOb(bitmap)) {
            mImageView.setImageBitmap(bitmap)
            return
        }
        // 无缓存，提交到线程池下载图片
        submitLoadRequest(imgUrl)
    }

    private fun submitLoadRequest(imgUrl: String) {
        mImageView.tag = imgUrl
        mExecutorService.submit {
            val bitmap = mCacheManager.getFromNetwork(imgUrl)
            val message = mUiHandler.obtainMessage()
            //加载成功后的图片
            if (CheckUtil.checkOb(bitmap) && mImageView.tag == imgUrl) {
                message.obj = bitmap
            } else { // 加载失败图片
                message.obj = config.errorImgId
            }
            mUiHandler.sendMessage(message)
        }
    }


    fun releaseResource() {

    }
}