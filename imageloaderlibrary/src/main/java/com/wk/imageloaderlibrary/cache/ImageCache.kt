package com.wk.imageloaderlibrary.cache

import android.graphics.Bitmap

interface ImageCache {

    fun getCache(url: String?) : Bitmap?
    fun putCache(url: String?, bitmap: Bitmap?)
}