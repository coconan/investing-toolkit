import sttp.client4._

object Main extends App {
    def get(): Response[Either[String, String]] = {
        val cubeSymbol = "SP1005282"
        val since = 1689150944000L
        val until = 1696926944000L
        
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

    val response = get()

    println(response.header("Content-Type"))

    println(response.body)
}