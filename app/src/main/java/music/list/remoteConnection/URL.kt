package music.list.remoteConnection

object URL {

    fun getAccessTokenUrl(): String {
        var url = "gateway/token/client"
        url = url.replace(" ".toRegex(), "%20")
        return url
    }

    fun getMusicListUrl(): String {
        var url = "sayt/flat"
        url = url.replace(" ".toRegex(), "%20")
        return url
    }

    fun getCharacterDetailsUrl(id: Int): String {
        var url = "characters/$id"
        url = url.replace(" ".toRegex(), "%20")
        return url
    }

    fun getCharacterComicsUrl(id: Int): String {
        var url = "characters/$id/comics"
        url = url.replace(" ".toRegex(), "%20")
        return url
    }


}