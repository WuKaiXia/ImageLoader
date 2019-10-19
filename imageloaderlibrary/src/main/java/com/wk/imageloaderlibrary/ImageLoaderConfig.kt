package com.wk.imageloaderlibrary

import com.wk.imageloaderlibrary.cache.CacheManager.Companion.ALL_CACHE

class ImageLoaderConfig {
    // 缓存策略
    var mCacheMode: Int = ALL_CACHE
        private set
    var threadCount = 0
        private set
    //加载错误时显示的图片ID
    var errorImgId: Int = -1
        private set
    var url: String = "" // 要加载的网络图片链接
        private set
    var imgId: Int = -1 // 要加载的本地图片 ID
        private set


    private constructor()

    class Builder {
        var mCacheMode: Int = ALL_CACHE
        // 线程池
        var threadCount = Runtime.getRuntime().availableProcessors() + 1
        var errorImgId: Int = -1
        var url: String = "" // 要加载的网络图片链接
        var imgId: Int = -1 // 要加载的本地图片 ID

        fun setCache(cache: Int): Builder {
            this.mCacheMode = cache
            return this
        }

        fun setThreadCount(threadCount: Int): Builder {
            this.threadCount = threadCount
            return this
        }

        fun setError(errorId: Int): Builder {
            this.errorImgId = errorId
            return this
        }

        fun setUrl(url: String?): Builder {
            this.url = url ?: ""
            return this
        }

        fun setSrc(imgId: Int): Builder {
            this.imgId = imgId
            return this
        }

        fun build(): ImageLoaderConfig{
            val config = ImageLoaderConfig()
            config.errorImgId = this.errorImgId
            config.threadCount = this.threadCount
            config.imgId = this.imgId
            config.url = this.url
            config.mCacheMode = this.mCacheMode
            return config
        }
    }

}