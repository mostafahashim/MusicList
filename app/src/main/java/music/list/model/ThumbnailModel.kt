package music.list.model

import java.io.Serializable
import java.util.Comparator

data class ThumbnailModel(
    var path: String? = "",
    var extension: String? = ""
) : Serializable
