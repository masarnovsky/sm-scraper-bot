package by.masarnovsky

import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

lateinit var twitterApiKey: String
lateinit var twitterApiSecret: String
lateinit var twitterAccessToken: String
lateinit var twitterAccessSecret: String

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
                } else if ((mediaEntity.type == "animated_gif" || mediaEntity.type == "video") && mediaEntity.videoVariants.isNotEmpty()) {
                    mediaEntity.videoVariants
                        .filter { it.contentType == "video/mp4" }
                        .sortedByDescending { it.bitrate }
                        .mapNotNull { it.url }
                        .first()
                } else {
                    null
                }
            }
            .map {
                convertUrlToContentObject(it)
            }
    }
}