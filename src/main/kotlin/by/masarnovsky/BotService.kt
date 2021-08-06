package by.masarnovsky

import com.elbekD.bot.types.Message
import mu.KotlinLogging
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.StandardCopyOption

private val logger = KotlinLogging.logger {}

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

fun sendMediaGroup(chatId: Long, messageId: Int, content: List<Content>) {
    bot.sendMediaGroup(chatId, content.mapNotNull {
        when (it.mediaType) {
            MediaType.VIDEO -> bot.mediaVideo(it.url)
            MediaType.IMAGE -> bot.mediaPhoto(it.url)
            else -> null
        }
    })
    deleteMessage(chatId, messageId)
}

fun deleteMessage(chatId: Long, messageId: Int) {
    bot.deleteMessage(chatId, messageId)
}

fun getChatIdAndTextFromMessage(message: Message): ChatIdAndText {
    return ChatIdAndText(message.chat.id, message.text)
}

fun getChatIdAndMessageIdAndTextFromMessage(message: Message): ChatIdAndMessageIdAndText {
    return ChatIdAndMessageIdAndText(message.chat.id, message.message_id, message.text)
}

data class ChatIdAndText(val chatId: Long, val text: String?)

data class ChatIdAndMessageIdAndText(val chatId: Long, val messageId: Int, val text: String?)