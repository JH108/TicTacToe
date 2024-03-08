package me.jessehill

class JSPlatform : Platform {
    override val name: String = "JS"
}

actual fun getPlatform(): Platform {
    return JSPlatform()
}