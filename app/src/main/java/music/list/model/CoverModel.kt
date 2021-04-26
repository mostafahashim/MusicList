package music.list.model

import java.io.Serializable
import java.util.Comparator

data class CoverModel(
    var tiny: String? = "",
    var small: String? = "",
    var medium: String? = "",
    var large: String? = "",
    var default: String? = "",
    var template: String? = "",
) : Serializable
