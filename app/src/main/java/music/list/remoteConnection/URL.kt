package music.list.remoteConnection

object URL {

    fun getAccessTokenUrl(): String {
        var url = "v0/api/gateway/token/client"
        url = url.replace(" ".toRegex(), "%20")
        return url
    }

    fun getMusicListUrl(): String {
        var url = "v2/api/sayt/flat"
        url = url.replace(" ".toRegex(), "%20")
        return url
    }

}