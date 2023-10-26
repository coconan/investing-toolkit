import scala.util.{Try, Success, Failure}
import sttp.client4._
import ujson.Obj
import org.jsoup.Jsoup
import java.time.LocalDateTime
import java.time.ZoneId

object Main extends App {
    def getPage(cubeSymbol: String): Response[Either[String, String]] = {
        val source = scala.io.Source.fromFile("target/cookie.txt")
        val cookieString = try source.mkString finally source.close()
        val cookies = cookieString
                .split(";")
                .map(s => s.trim())
                .map(s => s.split("="))
                .map(arr => arr(0) -> arr(1))

        val request = basicRequest
                .cookies(cookies:_*)
                .get(uri"https://xueqiu.com/P/$cubeSymbol")

        val backend = DefaultSyncBackend()
        val response = request.send(backend)
        response
    }

    def getAll(cubeSymbol: String, since: Long, until: Long): Response[Either[String, String]] = {
        val source = scala.io.Source.fromFile("target/cookie.txt")
        val cookieString = try source.mkString finally source.close()
        val cookies = cookieString
                .split(";")
                .map(s => s.trim())
                .map(s => s.split("="))
                .map(arr => arr(0) -> arr(1))

        val request = basicRequest
                .cookies(cookies:_*)
                .get(uri"https://xueqiu.com/service/tc/snowx/PAMID/cubes/nav_daily/all?cube_symbol=$cubeSymbol&since=$since&until=$until")

        val backend = DefaultSyncBackend()
        val response = request.send(backend)
        response
    }

    def crawl(cubeSymbol: String, since: Long, until: Long) = {
        val pageResponse = getPage(cubeSymbol)
        val pageResponseBodyString = pageResponse.body match {
            case Left(value) => value
            case Right(value) => value
        }
        val doc = Jsoup.parse(pageResponseBodyString)
        val isClosed = !doc.select("div.cube-closed").isEmpty()
        val name = doc.select("div.cube-title span.name").text()

        if (!isClosed) {
            val profit = doc.select("div.cube-profit-year span.per").text() + "%" 
            val response = getAll(cubeSymbol, since, until)

            val responseBodyString = response.body match {
                case Left(value) => throw new RuntimeException(value)
                case Right(value) => value
            }
            val responseJson = ujson.read(responseBodyString)
            val success = responseJson match {
                case Obj(value) => responseJson("success").bool
                case _ => true
            }
            if (!success) {
                println(cubeSymbol + ", " + name + ", " + "访问出错")
            } else {
                println(cubeSymbol + ", " + name + ", " + profit)
            }
        } else {
            println(cubeSymbol + ", " + name + ", " + "已关停")
        }
    }

    val since = LocalDateTime.now().minusYears(1).atZone(ZoneId.systemDefault()).toEpochSecond() * 1000
    val until = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000
    for (serialNo <- 1005282 to 1006000) {
        val cubeSymbol = "SP" + serialNo
        try {
            crawl(cubeSymbol, since, until)
        } catch {
            case e: SttpClientException.TimeoutException => println(cubeSymbol + ", " + "访问超时")
        }
        Thread.sleep(6000)
    }
}
