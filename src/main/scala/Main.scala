import sttp.client4._

object Main extends App {
    def get(): Response[Either[String, String]] = {
        val cubeSymbol = "SP1005282"
        val since = 1689150944000L
        val until = 1696926944000L
        val cookies = "device_id=c9eb2f436cbf806229e8db9419e28e26; s=dd122wgl1w; bid=af0a4c21fd07a8b356be4ad114e84529_lfkoexbt; __utmc=1; snbim_minify=true; xq_is_login=1; u=9581170017; cookiesu=511692587654787; Hm_lvt_1db88642e346389874251b5a1eded6e3=1694497325; xq_a_token=72a7eb611571990b9ff94e686591d6d6c17ca8a5; xqat=72a7eb611571990b9ff94e686591d6d6c17ca8a5; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjk1ODExNzAwMTcsImlzcyI6InVjIiwiZXhwIjoxNjk4NDYyMzA0LCJjdG0iOjE2OTU4NzAzMDQxNzQsImNpZCI6ImQ5ZDBuNEFadXAifQ.KoHpU7Om9F_M4pj_2_VuWYAD5C9B_CsH-ut4FhOIThwPvCMrcAcVVarL78VYSHBJsfrUuU2mCgiQgiTrOxWeb3MOfQ34hwre_Cku2flpRkydEA_hP1bNHjjNIVOsr7IX1RYtudNODL8SdrMSmtAoYYTw5T2o0Im0EWRYoZCtNVOug8E0tRMOtRIuI4AZBmHp_mgitJ8pocJY9zcUzcVdeA9lDcNKGS9nmhUQPDGVSwx9LWRMPvvZwG3wito2Zb0LPTbRYE9vVaDI93jK2zwYFTqNgxiIYlj_whbnALD3uOs05Qa6h-XJjCufXNjis9bFpAeEFashHQhnThUXDWlgGg; xq_r_token=25ece9b74b123240577f61848a48cde0500f7d3e; __utmz=1.1695880912.8.2.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; acw_tc=2760826a16969257962003647efe8ee1cc77c8f534df7723b3a2095ca5a8c9; __utma=1.532829289.1679549572.1696923136.1696926627.14; __utmt=1; is_overseas=0; __utmb=1.31.9.1696926894767;"
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