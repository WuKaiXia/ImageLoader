@file:Suppress("DEPRECATION")

package com.wk.imageloaderlibrary.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import java.io.File
import java.lang.Exception
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object DiskLruCacheUtil {

    /**
     * 获取缓存的路径
     */
    fun getDiskCacheDir(context: Context, uniqueName: String): File {
        var cacheStr: String =
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
                context.externalCacheDir?.path ?: ""
            } else {
                context.cacheDir.path
            }
        // 判断是否由sd card

        return File(cacheStr + File.separator + uniqueName)
    }

    /**
     * 获取APP版本号
     */
    fun getAppVersionCode(context: Context): Int {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionCode
        } catch (ex: PackageManager.NameNotFoundException) {
            ex.printStackTrace()
        } finally {
            return 0
        }
    }

    /**
     * 获取MD5 编码
     */
    fun getMd5Str(key: String): String {
        return try {
            val mDigest = MessageDigest.getInstance("MD5")
            mDigest.update(key.toByte())
            bytesToHexStr(mDigest.digest())
        } catch (e: Exception) {
            java.lang.String.valueOf(key.hashCode())
        }
    }

    private fun bytesToHexStr(bytes: ByteArray): String {

        val strBuilder = StringBuilder()
        bytes.indices.forEach {
            val hex = Integer.toHexString(0xFF and it)
            if (hex.length == 1) {
                strBuilder.append('0')
            }
            strBuilder.append(hex)
        }

        return strBuilder.toString()
    }
}