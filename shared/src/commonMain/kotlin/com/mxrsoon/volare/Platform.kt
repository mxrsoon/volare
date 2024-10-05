package com.mxrsoon.volare

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform