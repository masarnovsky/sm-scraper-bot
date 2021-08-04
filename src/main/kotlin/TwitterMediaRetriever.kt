import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder


//https://twitter.com/williamapan/status/1395466035617280000?s=09
// https://video.twimg.com/tweet_video/E12w5xvWYAQxK8K.mp4

fun downloadTwitterVideo(url: String) {

    val cb = ConfigurationBuilder()
    cb.setDebugEnabled(true)
        .setOAuthConsumerKey("your consumer key")
        .setOAuthConsumerSecret("your consumer secret")
        .setOAuthAccessToken("your access token")
        .setOAuthAccessTokenSecret("your access token secret")
    val tf = TwitterFactory(cb.build())
    val twitter = tf.instance
    val status = twitter.showStatus("1395466035617280000".toLong())!!

    val media = status.mediaEntities
//    val extendedMediaEntities: Array<ExtendedMediaEntity> = status.
//    for (i in extendedMediaEntities.indices) {
//        val extendedMediaEntity: ExtendedMediaEntity = extendedMediaEntities[i]
//        val variant: Array<ExtendedMediaEntity.Variant> = extendedMediaEntity.getVideoVariants()
//        if (extendedMediaEntity.getType().equals("video")) {
//            for (j in variant.indices) {
//                System.out.println("variant url: " + variant[j].getUrl())
//            }
//            val url: String = extendedMediaEntity.getURL()
//            System.out.println("extended url: " + extendedMediaEntity.getExpandedURL())
//        }
//    }

//    val page = Jsoup.connect(url).userAgent(USER_AGENT).get()
//    val videoUrl = page.select("meta[property=og:video]").first()?.attr("src")!!
//    download(videoUrl)
}

fun retrieveTwitterMedia(url: String): List<Content> {
    return listOf()
}