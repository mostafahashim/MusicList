package music.list.model

import java.io.Serializable
import java.util.Comparator

data class URLModel(
    var type: String? = "",
    var url: String? = ""
) : Serializable
