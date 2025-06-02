package com.jawa.ite.util

expect object AppLogger {
    fun error(tag: String, message: String, throwable: Throwable? = null)
    fun warn(tag: String, message: String, throwable: Throwable? = null)
    fun info(tag: String, message: String)
}