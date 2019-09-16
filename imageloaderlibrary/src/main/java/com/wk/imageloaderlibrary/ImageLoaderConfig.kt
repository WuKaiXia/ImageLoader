package com.wk.imageloaderlibrary

import com.wk.imageloaderlibrary.cache.ImageCache
import com.wk.imageloaderlibrary.cache.MemoryCache

class ImageLoaderConfig {
    // 缓存策略
    var mCache: ImageCache = MemoryCache()
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
        var mCache: ImageCache = MemoryCache()
        // 线程池
        var threadCount = Runtime.getRuntime().availableProcessors() + 1
        var errorImgId: Int = -1
        var url: String = "" // 要加载的网络图片链接
        var imgId: Int = -1 // 要加载的本地图片 ID

        fun setCache(cache: ImageCache): Builder {
            this.mCache = cache
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

        fun build() {
            val config = ImageLoaderConfig()
            config.errorImgId = this.errorImgId
            config.threadCount = this.threadCount
            config.imgId = this.imgId
            config.url = this.url
            config.mCache = this.mCache
        }
    }

}