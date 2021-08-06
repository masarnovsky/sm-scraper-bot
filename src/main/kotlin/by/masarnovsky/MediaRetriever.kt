package by.masarnovsky

interface MediaRetriever {
    fun retrieveMedia(url: String): List<Content>

    fun convertUrlToContentObject(url: String): Content {
        val filename = getLastPathValue(url)
        return Content(url, filename, mediaType(filename))
    }
}

data class Content(val url: String, val fileName: String, val mediaType: MediaType)