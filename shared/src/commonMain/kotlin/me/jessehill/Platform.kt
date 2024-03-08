package me.jessehill

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform