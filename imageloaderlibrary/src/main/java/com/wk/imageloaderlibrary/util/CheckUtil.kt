package com.wk.imageloaderlibrary.util

import android.text.TextUtils

object CheckUtil {

    fun <T> checkOb(ob: T?): Boolean {
        if (ob == null) {
            return false
        }
        return true
    }

}