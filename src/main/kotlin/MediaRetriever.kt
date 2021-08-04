interface MediaRetriever {
    fun retrieveMedia(url: String): List<Content>

    fun convertUrlToContentObject(url: String): Content {
        val filename = getLastPathValue(url)
        return Content(url, filename, mediaType(filename))
    }
}