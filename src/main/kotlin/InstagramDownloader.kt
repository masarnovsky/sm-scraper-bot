import org.jsoup.Jsoup
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.StandardCopyOption

var directory = "D:\\downloads"
val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36";


fun downloadVideo(url: String) {
    val page = Jsoup.connect(url).userAgent(USER_AGENT).get()
    val videoUrl = page.select("meta[property=og:video]").first()?.attr("content")!!
    download(videoUrl)
}

fun download(url:String) {
    val tempName = url.split("/")
    val filename = tempName[tempName.lastIndex].split("?")[0]

    URI.create(url).toURL().openStream()?.use { inputStream ->
        val toPath = File(directory.plus(File.separator).plus(filename)).toPath()
        Files.copy(inputStream, toPath, StandardCopyOption.REPLACE_EXISTING)

    }
}