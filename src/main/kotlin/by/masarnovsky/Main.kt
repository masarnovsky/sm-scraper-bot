package by.masarnovsky

import com.elbekD.bot.Bot
import mu.KotlinLogging
import java.io.FileInputStream
import java.util.*

const val IS_PROD = "IS_PROD"
const val BOT_TOKEN = "BOT_TOKEN"
const val BOT_USERNAME = "BOT_USERNAME"
const val OWNER_ID = "OWNER_ID"
const val TWITTER_API_KEY = "TWITTER_API_KEY"
const val TWITTER_API_SECRET = "TWITTER_API_SECRET"
const val TWITTER_ACCESS_TOKEN = "TWITTER_ACCESS_TOKEN"
const val TWITTER_ACCESS_SECRET = "TWITTER_ACCESS_SECRET"

lateinit var token: String
lateinit var username: String
lateinit var ownerId: String

var isProd = false

private val logger = KotlinLogging.logger {}

lateinit var bot: Bot

val URL_PATTERN =
    "(https?://(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})"

fun main() {
    loadProperties()
    bot = Bot.createPolling(username, token)
    setBehaviour()
    bot.start()
}

private fun loadProperties() {
    if (System.getenv()[IS_PROD].toString() != "null") {
        logger.info { "setup prod environment" }
        isProd = true
        token = System.getenv()[BOT_TOKEN].toString()
        username = System.getenv()[BOT_USERNAME].toString()
        ownerId = System.getenv()[OWNER_ID].toString()
        twitterApiKey = System.getenv()[TWITTER_API_KEY].toString()
        twitterApiSecret = System.getenv()[TWITTER_API_SECRET].toString()
        twitterAccessToken = System.getenv()[TWITTER_ACCESS_TOKEN].toString()
        twitterAccessSecret = System.getenv()[TWITTER_ACCESS_SECRET].toString()
    } else {
        logger.info { "setup test environment" }
        val properties = Properties()
        val propertiesFile = System.getProperty("user.dir") + "\\test_env.properties"
        val inputStream = FileInputStream(propertiesFile)
        properties.load(inputStream)
        token = properties.getProperty(BOT_TOKEN)
        username = properties.getProperty(BOT_USERNAME)
        ownerId = properties.getProperty(OWNER_ID)
        twitterApiKey = properties.getProperty(TWITTER_API_KEY)
        twitterApiSecret = properties.getProperty(TWITTER_API_SECRET)
        twitterAccessToken = properties.getProperty(TWITTER_ACCESS_TOKEN)
        twitterAccessSecret = properties.getProperty(TWITTER_ACCESS_SECRET)
    }
}

private fun setBehaviour() {
    onChannelPost()
}

fun onChannelPost() {
    val instagram = InstagramMediaRetriever()
    val twitter = TwitterMediaRetriever()
    val other = OtherMediaRetriever()
    bot.onChannelPost { message ->
        val (chatId, messageId, url) = getChatIdAndMessageIdAndTextFromMessage(message)
        if (url != null && validateURL(url) && !validateMediaType(url)) {
            val mediaContent = when (mediaSource(url)) {
                MediaSource.INSTAGRAM -> instagram.retrieveMedia(url)
                MediaSource.TWITTER -> twitter.retrieveMedia(url)
                else -> other.retrieveMedia(url)
            }

            if (mediaContent.size == 1) {
                val content = mediaContent.first()
                when (content.mediaType) {
                    MediaType.VIDEO -> sendVideo(chatId, messageId, content.url)
                    MediaType.IMAGE -> sendImage(chatId, messageId, content.url)
                }
            } else if (mediaContent.size > 1) {
                mediaContent.chunked(10).map { mediaGroup ->
                    sendMediaGroup(chatId, messageId, mediaGroup)
                }
            }
        }
    }
}