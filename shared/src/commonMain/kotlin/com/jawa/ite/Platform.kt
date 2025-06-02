package com.jawa.ite

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform