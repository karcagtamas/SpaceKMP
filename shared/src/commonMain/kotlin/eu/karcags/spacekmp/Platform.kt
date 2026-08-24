package eu.karcags.spacekmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform