package com.wk.imageloaderlibrary.util

import java.io.Closeable
import java.io.IOException

object CloseableUtil {
    fun closeQuietly(closeable: Closeable?) {
            try {
                closeable?.close()
            } catch (e : IOException) {
                e.printStackTrace()
            }
    }
}