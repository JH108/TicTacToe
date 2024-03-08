package me.jessehill

class JVMPlatform : Platform {
    override val name: String = "JVM"
}

actual fun getPlatform(): Platform {
    return JVMPlatform()
}