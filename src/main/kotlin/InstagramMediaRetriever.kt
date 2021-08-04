import org.jsoup.Jsoup


class InstagramMediaRetriever : MediaRetriever {
    override fun retrieveMedia(url: String): List<Content> {
        val page = Jsoup.connect(url).userAgent(USER_AGENT).get()
        return listOf("meta[property=og:video]", "meta[property=og:image]")
            .mapNotNull {
                page.select(it).first()?.attr("content")
            }
            .map {
                convertUrlToContentObject(it)
            }
            .take(1)
    }

}