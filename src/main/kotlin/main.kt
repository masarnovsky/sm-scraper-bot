fun main(args: Array<String>) {
    downloadVideo("https://www.instagram.com/p/CSE66pJDQbd/")

    //https://twitter.com/williamapan/status/1395466035617280000?s=09 | https://video.twimg.com/tweet_video/E12w5xvWYAQxK8K.mp4
}

fun validateURL(url: String): Boolean {
    return !(url.startsWith("www.") ||
            url.startsWith("instagram.com") ||
            !url.startsWith("https://") ||
            url.startsWith("http://") ||
            (!url.contains("instagram.com/p/") && !url.contains("instagram.com/tv/")))
}

fun mediaType(fileName: String): MediaType {
    return if (fileName.endsWith(".mp4")) {
        MediaType.VIDEO
    } else if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
        MediaType.IMAGE
    } else {
        MediaType.UNKNOWN
    }
}

enum class MediaType {
    VIDEO, IMAGE, UNKNOWN
}

