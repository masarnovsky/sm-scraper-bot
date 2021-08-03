import org.jsoup.Jsoup

fun downloadInstagramVideo(url: String) {
    val page = Jsoup.connect(url).userAgent(USER_AGENT).get()
    val videoUrl = page.select("meta[property=og:video]").first()?.attr("content")!!
    download(videoUrl)
}

fun downloadInstagramImage(url: String) {
    val page = Jsoup.connect(url).userAgent(USER_AGENT).get()
    val imageUrl = page.select("meta[property=og:image]").first()?.attr("content")!!
    download(imageUrl)
}