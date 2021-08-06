package by.masarnovsky

import mu.KotlinLogging
import org.jsoup.Jsoup

private val logger = KotlinLogging.logger {}

val USER_AGENT =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"


class InstagramMediaRetriever : MediaRetriever {
    override fun retrieveMedia(url: String): List<Content> {
        logger.info { "trying to retrieve instagram media from:$url" }

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