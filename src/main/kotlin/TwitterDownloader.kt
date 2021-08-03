import org.jsoup.Jsoup
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.Twitter

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
    val showStatus = twitter.showStatus("1395466035617280000".toLong())
//    val page = Jsoup.connect(url).userAgent(USER_AGENT).get()
//    val videoUrl = page.select("meta[property=og:video]").first()?.attr("src")!!
//    download(videoUrl)
}