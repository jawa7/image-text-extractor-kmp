package com.jawa.ite.util

import platform.Foundation.NSLog

actual object AppLogger {
    actual fun error(tag: String, message: String, throwable: Throwable?) {
        throwable?.let {
            NSLog("ERROR: [$tag] $message. Throwable: $throwable CAUSE ${throwable.cause}")
        } ?: NSLog("ERROR: [$tag] $message")
    }

    actual fun info(tag: String, message: String) {
        NSLog("INFO: [$tag] $message")
    }

    actual fun warn(tag: String, message: String, throwable: Throwable?) {
        throwable?.let {
            NSLog("WARN: [$tag] $message. Throwable: $throwable CAUSE ${throwable.cause}")
        } ?: NSLog("WARN: [$tag] $message")
    }
}