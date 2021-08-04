import com.elbekD.bot.Bot
import com.elbekD.bot.types.Message
import mu.KotlinLogging
import java.io.File
import java.io.FileInputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*

const val IS_PROD = "IS_PROD"
const val BOT_TOKEN = "BOT_TOKEN"
const val BOT_USERNAME = "BOT_USERNAME"
const val OWNER_ID = "OWNER_ID"

var directory = "D:\\00scraper"
val USER_AGENT =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36";

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
    } else {
        logger.info { "setup test environment" }
        val properties = Properties()
        val propertiesFile = System.getProperty("user.dir") + "\\test_env.properties"
        val inputStream = FileInputStream(propertiesFile)
        properties.load(inputStream)
        token = properties.getProperty(BOT_TOKEN)
        username = properties.getProperty(BOT_USERNAME)
        ownerId = properties.getProperty(OWNER_ID)
    }
}

private fun setBehaviour() {
    onChannelPost()
}

fun onChannelPost() {
    val instagram = InstagramMediaRetriever()
    bot.onChannelPost { message ->
        val (chatId, messageId, url) = getChatIdAndMessageIdAndTextFromMessage(message)
        if (url != null && validateURL(url) && !validateMediaType(url)) {
            val mediaUrl = when (mediaSource(url)) {
                MediaSource.INSTAGRAM -> instagram.retrieveMedia(url)
                MediaSource.TWITTER -> retrieveTwitterMedia(url)
                else -> retrieveOtherMedia(url)
            }
            mediaUrl.forEach {
                when (it.mediaType) {
                    MediaType.VIDEO -> sendVideo(chatId, messageId, it.url)
                    MediaType.IMAGE -> sendImage(chatId, messageId, it.url)
                }
            }
        }
    }
}

fun retrieveOtherMedia(url: String): List<Content> {
    return listOf()
}

fun sendMessage(chatId: Long, text: String) {
    bot.sendMessage(chatId = chatId, text = text, parseMode = "HTML")
}

fun sendVideo(chatId: Long, messageId: Int, videoUrl: String) {
    logger.info { "sendVideo($chatId, $videoUrl)" }
    bot.sendVideo(chatId = chatId, video = videoUrl)
    deleteMessage(chatId, messageId)
}

fun sendImage(chatId: Long, messageId: Int, imageUrl: String) {
    logger.info { "sendImage($chatId, $imageUrl)" }
    bot.sendPhoto(chatId = chatId, photo = imageUrl)
    deleteMessage(chatId, messageId)
}

fun deleteMessage(chatId: Long, messageId: Int) {
    bot.deleteMessage(chatId, messageId)
}

private fun getChatIdAndTextFromMessage(message: Message): ChatIdAndText {
    return ChatIdAndText(message.chat.id, message.text)
}

private fun getChatIdAndMessageIdAndTextFromMessage(message: Message): ChatIdAndMessageIdAndText {
    return ChatIdAndMessageIdAndText(message.chat.id, message.message_id, message.text)
}

private data class ChatIdAndText(val chatId: Long, val text: String?)

private data class ChatIdAndMessageIdAndText(val chatId: Long, val messageId: Int, val text: String?)

fun download(url: String) {
    val tempName = url.split("/")
    val filename = tempName[tempName.lastIndex].split("?")[0]

    URI.create(url).toURL().openStream()?.use { inputStream ->
        val toPath = File(directory.plus(File.separator).plus(filename)).toPath()
        Files.copy(inputStream, toPath, StandardCopyOption.REPLACE_EXISTING)
    }
}

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

enum class MediaSource {
    INSTAGRAM, TWITTER, OTHER
}

data class Content(val url: String, val fileName: String, val mediaType: MediaType)
