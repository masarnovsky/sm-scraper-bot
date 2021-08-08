package by.masarnovsky

fun validateURL(url: String): Boolean {
    return Regex(URL_PATTERN) matches url
}

fun validateMediaType(url: String): Boolean {
    return url.endsWith(".mp4").or(url.endsWith(".jpg")).or(url.endsWith(".png")).or(url.endsWith(".jpeg"))
}

fun mediaSource(url: String): MediaSource {
    if (url.contains("instagram")) return MediaSource.INSTAGRAM
    if (url.contains("twitter")) return MediaSource.TWITTER
    return MediaSource.OTHER
}

fun mediaType(fileName: String): MediaType {
    val temp = fileName.split(".")
    val extension = temp[temp.lastIndex]
    return when {
        listOf("mp4", "m3u8").contains(extension) -> {
            MediaType.VIDEO
        }
        listOf("png", "jpg").contains(extension) -> {
            MediaType.IMAGE
        }
        else -> {
            MediaType.UNKNOWN
        }
    }
}

fun getLastPathValue(path: String): String {
    val tempName = path.split("/")
    return tempName[tempName.lastIndex].split("?")[0]
}

enum class MediaType {
    VIDEO, IMAGE, UNKNOWN
}

enum class MediaSource {
    INSTAGRAM, TWITTER, OTHER
}