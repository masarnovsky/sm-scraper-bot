package by.masarnovsky

import mu.KotlinLogging
import org.jsoup.Jsoup

private val logger = KotlinLogging.logger {}

val USER_AGENT = "ABACHOBot"


class InstagramMediaRetriever : MediaRetriever {
    override fun retrieveMedia(url: String): List<Content> {
        logger.info { "trying to retrieve instagram media from:$url" }

        val page = Jsoup.connect(url).userAgent(USER_AGENT).get()
        logger.info { "$page" }
        return listOf("meta[property=og:video]", "meta[property=og:image]")
            .mapNotNull {
                page.select(it).first()?.attr("content")
            }
            .map {
                logger.info { it }
                convertUrlToContentObject(it)
            }
            .take(1)
    }

}