package by.masarnovsky

class OtherMediaRetriever : MediaRetriever {
    override fun retrieveMedia(url: String): List<Content> {
        return listOf()
    }
}