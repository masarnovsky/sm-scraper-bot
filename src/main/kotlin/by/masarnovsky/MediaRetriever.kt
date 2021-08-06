package by.masarnovsky

import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.StandardCopyOption

interface MediaRetriever {
    fun retrieveMedia(url: String): List<Content>

    fun convertUrlToContentObject(url: String): Content {
        val filename = getLastPathValue(url)
        return Content(url, filename, mediaType(filename))
    }

    fun download(url: String, directory: String) {
        val tempName = url.split("/")
        val filename = tempName[tempName.lastIndex].split("?")[0]

        URI.create(url).toURL().openStream()?.use { inputStream ->
            val toPath = File(directory.plus(File.separator).plus(filename)).toPath()
            Files.copy(inputStream, toPath, StandardCopyOption.REPLACE_EXISTING)
        }
    }
}

data class Content(val url: String, val fileName: String, val mediaType: MediaType)