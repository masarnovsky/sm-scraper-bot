import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

class TwitterMediaRetriever : MediaRetriever {
    override fun retrieveMedia(url: String): List<Content> {
        val twitId = getLastPathValue(url)

        val twitter = TwitterFactory(
            ConfigurationBuilder().setDebugEnabled(true)
                .setOAuthConsumerKey(twitterApiKey)
                .setOAuthConsumerSecret(twitterApiSecret)
                .setOAuthAccessToken(twitterAccessToken)
                .setOAuthAccessTokenSecret(twitterAccessSecret)
                .build()
        ).instance

        val status = twitter.showStatus(twitId.toLong())!!
        val media = status.mediaEntities

        return media
            .mapNotNull { mediaEntity ->
                if (mediaEntity.type == "photo") {
                    mediaEntity.mediaURL!!
                } else if (mediaEntity.type == "animated_gif" && mediaEntity.videoVariants.isNotEmpty()) {
                    mediaEntity.videoVariants.mapNotNull { it.url }.first()
                } else {
                    null
                }
            }
            .map {
                convertUrlToContentObject(it)
            }
    }
}