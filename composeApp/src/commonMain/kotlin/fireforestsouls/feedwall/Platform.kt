package fireforestsouls.feedwall

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform