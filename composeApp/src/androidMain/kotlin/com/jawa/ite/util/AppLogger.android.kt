package com.jawa.ite.util

import android.util.Log

actual object AppLogger {
    actual fun error(tag: String, message: String, throwable: Throwable?) {
        throwable?.let {
            Log.e(tag, message, it)
        } ?: Log.e(tag, message)

    }

    actual fun warn(tag: String, message: String, throwable: Throwable?) {
        throwable?.let {
            Log.w(tag, message, it)
        } ?: Log.e(tag, message)
    }

    actual fun info(tag: String, message: String) {
        Log.d(tag, message)
    }
}