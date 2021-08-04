import org.jsoup.Jsoup


class InstagramMediaRetriever : MediaRetriever {
    override fun retrieveMedia(url: String): List<Content> {
        val page = Jsoup.connect(url).userAgent(USER_AGENT).get()
        return listOf("meta[property=og:video]", "meta[property=og:image]")
            .mapNotNull {
                page.select(it).first()?.attr("content")
            }
            .map {
                val tempName = it.split("/")
                val filename = tempName[tempName.lastIndex].split("?")[0]
                Content(it, filename, mediaType(filename))
            }
            .take(1)
    }

}