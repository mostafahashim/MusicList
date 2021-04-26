package music.list.model

import java.io.Serializable
import java.util.Comparator

data class ArtistModel(
    var id: String? = "",
    var name: String? = "",
) : Serializable